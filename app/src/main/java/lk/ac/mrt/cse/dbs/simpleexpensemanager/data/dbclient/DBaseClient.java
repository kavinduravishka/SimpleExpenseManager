package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.*;

//import androidx.annotation.Nullable;

public class DBaseClient extends SQLiteOpenHelper {
    public DBaseClient(Context context) {

        super(context, "180012M.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE account(accountNo varchar(15), bankName varchar(30), accountHolderName varchar(100), balance numeric(12,2),PRIMARY KEY(accountNo,bankName))");
        DB.execSQL("CREATE TABLE transactionLog(transactionID INTEGER PRIMARY KEY autoincrement, date DATE, accountNo varchar(15), expenseType TEXT, amount numeric(12,2), FOREIGN KEY (accountNo) REFERENCES account(accountNo))");
        DB.execSQL("pragma foreign_keys = ON");
        DB.execSQL("create unique index acc_no on account(accountNo)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists account");
        DB.execSQL("drop Table if exists transactionLog");
    }

    public void logTransaction(Transaction transaction){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", String.valueOf(transaction.getDate()));
        contentValues.put("accountNo", transaction.getAccountNo());
        contentValues.put("expenseType", String.valueOf(transaction.getExpenseType()));
        contentValues.put("amount",transaction.getAmount());
        DB.insert("transactionLog", null, contentValues);
    }

    public Cursor getAllTransaction(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT  * from transactionLog",null);
        return cursor;
    }

    public Cursor getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT  * from transactionLog ORDER BY transactionID DESC LIMIT ?",new String[]{String.valueOf(limit)});
        return cursor;
    }

    public Cursor getAccountNumbersList(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT accountNo from account",null);
        return cursor;
    }

    public Cursor getAccountsList() {
        SQLiteDatabase DB  = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * from account",null);
        return cursor;
    }

    public Cursor getAccount(String accountNo){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * from account where accountNo=? LIMIT 1",new String[]{accountNo});
        return cursor;
    }

    public void addAccount(Account account) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        DB.insert("account",null,contentValues);
    }

    public void removeAccount(String accountNo){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("DELETE from account WHERE accontNo=?",new String[]{accountNo});
    }

    public void updateBalance(Account account){
        SQLiteDatabase DB = this.getWritableDatabase();
        String accountNo = account.getAccountNo();
        String bankName = account.getBankName();
        String amount = String.valueOf(account.getBalance());
        DB.rawQuery("UPDATE account SET amount=? where accountNo=? AND bankName=?",new String[]{amount,accountNo,bankName});
    }


}
