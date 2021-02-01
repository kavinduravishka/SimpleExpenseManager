package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient.*;

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

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }
}
