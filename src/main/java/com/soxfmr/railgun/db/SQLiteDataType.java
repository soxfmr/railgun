package com.soxfmr.railgun.db;

public class SQLiteDataType {
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_STRING = 3;
    public static final int TYPE_BLOB = 4;
    public static final int TYPE_BOOLEAN = 5;

    public static String translate(int type) {
        switch(type) {
            case TYPE_INTEGER:
            case TYPE_BOOLEAN:
                return "INTEGER";
            case TYPE_FLOAT:
                return "REAL";
            case TYPE_STRING:
                return "TEXT";
            case TYPE_BLOB:
                return "BLOB";
            default: return "NULL";
        }
    }
}
