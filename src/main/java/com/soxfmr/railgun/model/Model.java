package com.soxfmr.railgun.model;

import com.soxfmr.railgun.contract.DatabaseField;
import com.soxfmr.railgun.db.SQLiteDataType;

public abstract class Model {
    public static final String PRIMARY_KEY = "_id";

    @DatabaseField(field = PRIMARY_KEY, type = SQLiteDataType.TYPE_INTEGER, primary = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
