package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient.*;

import android.database.Cursor;

public class PersistentTransactionDAO implements TransactionDAO{

    private final List<Transaction> transactions;
    private DBaseClient dbclient;

    public PersistentTransactionDAO() {
        transactions = new LinkedList<>();
    }

    public void setDBclient(DBaseClient dbclient){
        this.dbclient = dbclient;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        this.dbclient.logTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor cursor = this.dbclient.getAllTransaction();
        return this.rowToTransaction(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor cursor =  this.dbclient.getPaginatedTransactionLogs(limit);
        return this.rowToTransaction(cursor);
    }

    private List<Transaction> rowToTransaction(Cursor cursor){
        List<Transaction> fetchedlist = new ArrayList<Transaction>();

        if (cursor.moveToFirst()){
            do{

                Transaction transaction = new Transaction(null,null,null,0);

                transaction.setDate(java.sql.Date.valueOf(cursor.getString(cursor.getColumnIndex("date"))));
                transaction.setAccountNo(cursor.getString(cursor.getColumnIndex("accountNo")));
                transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("expenseType"))));
                transaction.setAmount(Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount"))));

                fetchedlist.add(transaction);
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
