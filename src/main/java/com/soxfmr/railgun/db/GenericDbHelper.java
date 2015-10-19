package com.soxfmr.railgun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDbHelper extends SQLiteOpenHelper {

    private SQLiteScheme scheme;

    public GenericDbHelper(SQLiteScheme scheme, Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.scheme = scheme;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        scheme.onScheme(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
