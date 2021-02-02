package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient.DBaseClient;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO{

    private final Map<String, Account> accounts;
    private DBaseClient dbclient;
    private Object lock;

    public PersistentAccountDAO() {
        this.accounts = new HashMap<>();
        this.lock = new Object();
    }


    public void setDBclient(DBaseClient dbclient){
        this.dbclient = dbclient;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor =  this.dbclient.getAccountNumbersList();
        return this.extract(cursor,"accountNo");
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor cursor = this.dbclient.getAccountsList();
        return this.rowToAccount(cursor);
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor =  this.dbclient.getAccount(accountNo);
         try{
             return this.rowToAccount(cursor).get(0);
         }catch (Exception e){
             String msg = "Account " + accountNo + " is invalid.";
             throw new InvalidAccountException(msg);
         }
    }

    @Override
    public void addAccount(Account account) {
        this.dbclient.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        List<String> accountNoList = this.getAccountNumbersList();
        if(accountNoList.contains(accountNo)){
            this.dbclient.removeAccount(accountNo);
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        List<String> accountNoList = this.getAccountNumbersList();
        if(accountNoList.contains(accountNo)){
            Account account = this.getAccount(accountNo);
            if(expenseType == ExpenseType.EXPENSE){
                account.setBalance(account.getBalance()-amount);
            }else if(expenseType==ExpenseType.INCOME){
                account.setBalance(account.getBalance()+amount);
            }

            this.dbclient.updateBalance(account);
        }else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    private List<Account> rowToAccount(Cursor cursor){
        List<Account> fetchedlist = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{

                Account account = new Account(null,null,null,0);

                account.setAccountNo(cursor.getString(cursor.getColumnIndex("accountNo")));
                account.setBankName(cursor.getString(cursor.getColumnIndex("bankName")));
                account.setAccountHolderName(cursor.getString(cursor.getColumnIndex("accountHolderName")));
                account.setBalance(Double.parseDouble(cursor.getString(cursor.getColumnIndex("balance"))));

                fetchedlist.add(account);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return fetchedlist;
    }

    private List<String> extract(Cursor cursor, String field){
        List<String> extractedlist= new ArrayList<>();

        if (cursor.moveToFirst()){
            do{
                extractedlist.add(cursor.getString(cursor.getColumnIndex(field)));
            }while(cursor.moveToNext());
        }
        cursor.close();

        return extractedlist;
    }
}
