package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    private List<Transaction> rowToTransaction(Cursor cursor){
        List<Transaction> fetchedList = new ArrayList<Transaction>();

        if (cursor.moveToFirst()){
            do{

                Transaction transaction = new Transaction(null,null,null,0);

                transaction.setDate(Date.valueOf(cursor.getString(cursor.getColumnIndex("date"))));
                transaction.setAccountNo(cursor.getString(cursor.getColumnIndex("accountNo")));
                transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("expenseType"))));
                transaction.setAmount(Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount"))));

                fetchedList.add(transaction);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return fetchedList;
    }

    public List<Transaction> getAllTransaction(){
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT  * from transactionLog",null);
        List<Transaction> fetchedlist= this.rowToTransaction(cursor);

        return fetchedlist;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT  * from transactionLog ORDER BY transactionID DESC LIMIT ?",new String[]{String.valueOf(limit)});
        List<Transaction> fetchedlist= this.rowToTransaction(cursor);

        return fetchedlist;
    }
}
