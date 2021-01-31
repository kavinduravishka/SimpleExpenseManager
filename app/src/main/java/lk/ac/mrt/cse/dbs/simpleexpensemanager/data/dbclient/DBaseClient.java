package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import androidx.annotation.Nullable;

public class DBaseClient extends SQLiteOpenHelper {
    public DBaseClient(Context context) {
        super(context, "180012M.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("");
    }
}
