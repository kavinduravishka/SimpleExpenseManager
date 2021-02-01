package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

//import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient.*;

import android.content.Context;

public class PersistentExpenseManager extends ExpenseManager{

    private DBaseClient DB;
    public PersistentExpenseManager(Context context) {
        this.DB = new DBaseClient(context);
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        PersistentTransactionDAO transactionDAO  = new PersistentTransactionDAO();
        transactionDAO.setDBclient(this.DB);
        TransactionDAO persistentTransactionDAO = transactionDAO;
        setTransactionsDAO(persistentTransactionDAO);

        PersistentAccountDAO accountDAO = new PersistentAccountDAO();
        accountDAO.setDBclient(this.DB);
        AccountDAO persistentAccountDAO = accountDAO;
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/
    }

}
