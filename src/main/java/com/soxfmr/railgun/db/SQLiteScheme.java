package com.soxfmr.railgun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.soxfmr.railgun.contract.DatabaseTable;
import com.soxfmr.railgun.contract.SchemeComposer;
import com.soxfmr.railgun.contract.SortContract;
import com.soxfmr.railgun.meta.TableInfo;
import com.soxfmr.railgun.utils.Args;
import com.soxfmr.reflectdroid.ReflectDroid;

import java.util.Set;

public class SQLiteScheme implements SortContract<Class<?>> {
    private Context context;
    private String packageName;

    public SQLiteDatabase init(Context context, String name, int version, String packageName) {
        this.context = context;
        this.packageName = packageName;

        GenericDbHelper dbHelper = new GenericDbHelper(this, context, name, null, version);
        return dbHelper.getWritableDatabase();
    }

    public void onScheme(SQLiteDatabase db) {
        ReflectDroid reflections = new ReflectDroid(context, packageName);
        Set<Class<?>> modules = reflections.getTypesAnnotatedWith(DatabaseTable.class);

        if (modules == null || modules.size() == 0)
            return;

        Class<?>[] modelArray = new Class<?>[modules.size()];
        modules.toArray(modelArray);

        sort(modelArray);

        String scheme;
        TableInfo tableInfo;

        SchemeComposer composer = new SQLiteComposer();
        for (int i = modelArray.length - 1; i >= 0; i--) {
            tableInfo = new TableInfo(modelArray[i]);
            scheme = composer.composeTable(tableInfo);
            if (Args.isEmpty(scheme)) continue;

            db.execSQL(scheme);
        }
    }

    @Override
    public Class<?>[] sort(Class<?>[] classes) {
        int len = classes.length;
        if (len <= 1)
            return classes;
        // Retrieve the priority of the array
        int[] priorities = new int[len];
        for (int i = len; i >= 0; i--) {
            priorities[i] = new TableInfo(classes[i]).getPriority();
        }

        Class<?> clsSettle;
        int prioritySettle, desc;
        for (int p = 1; p < len; p++) {
            clsSettle = classes[p];
            prioritySettle = priorities[p];
            for (desc = p; desc > 0 && priorities[desc - 1] > prioritySettle; desc--) {
                classes[desc] = classes[desc - 1];
                priorities[desc] = priorities[desc - 1];
            }
            classes[desc] = clsSettle;
            priorities[desc] = prioritySettle;
        }

        return classes;
    }
}
