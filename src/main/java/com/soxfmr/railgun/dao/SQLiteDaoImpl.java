package com.soxfmr.railgun.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.soxfmr.railgun.db.SQLiteDataType;
import com.soxfmr.railgun.meta.FieldInfo;
import com.soxfmr.railgun.model.Model;
import com.soxfmr.railgun.utils.Args;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDaoImpl<E extends Model> extends BaseDaoImpl<E> {

    private SQLiteDatabase sqLiteDatabase;

    public SQLiteDaoImpl(SQLiteDatabase sqLiteDatabase, Class<E> cls) {
        super(cls);
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public List<E> all() {
        return all(null, null);
    }

    @Override
    public <S, T> List<E> where(S source, T target) {
        return all(String.valueOf(source) + "=?", new String[]{ String.valueOf(target) });
    }

    @Override
    public E get(int index) {
        E instance = null;
        Cursor cursor = null;
        try {
            cursor = query(null, null);
            if (cursor != null && cursor.moveToFirst()) {
                instance = resolveRecord(cursor);
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return instance;
    }

    @Override
    public boolean remove(int index) {
        return remove(Model.PRIMARY_KEY + "=?", new String[]{index + ""});
    }

    @Override
    public boolean remove(E e) {
        return remove(e.getId());
    }

    @Override
    public boolean insert(E e) {
        return insert(resolveEntity(e));
    }

    @Override
    public boolean update(E e) {
        return update(resolveEntity(e), Model.PRIMARY_KEY + "=?", new String[]{e.getId() + ""});
    }

    private List<E> all(String selection, String[] selectionArgs) {
        List<E> list = null;
        Cursor cursor = null;
        try {
            cursor = query(selection, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                list = new ArrayList<>();

                while (!cursor.isAfterLast()) {
                    list.add(resolveRecord(cursor));
                    cursor.moveToNext();
                }
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return list;
    }

    private boolean remove(String selection, String[] selectionArgs) {
        return Args.intToBool(sqLiteDatabase.delete(tableName, selection, selectionArgs));
    }

    private boolean insert(ContentValues contentValues) {
        // Auto increment
        // contentValues.putNull( Model.PRIMARY_KEY );
        return Args.intToBool((int) sqLiteDatabase.insert(tableName, null, contentValues));
    }

    private boolean update(ContentValues contentValues, String selection, String[] selectionArgs) {
        return Args.intToBool(sqLiteDatabase.update(tableName, contentValues, selection, selectionArgs));
    }

    private Cursor query(String selection, String[] selectionArgs) {
        return query(null, selection, selectionArgs, null, null, null);
    }

    private Cursor query(String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy) {
        if (Args.isEmpty(tableName))
            return null;

        if (fieldInfoList.size() == 0)
            return null;

        return sqLiteDatabase.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private ContentValues resolveEntity(E e) {
        ContentValues contentValues = null;
        try {
            Object value;
            contentValues = new ContentValues();
            for (FieldInfo fieldInfo : fieldInfoList) {
                // Ignore the primary key
                if (fieldInfo.getField().equals(primary))
                    continue;

                value = retrieve(e, fieldInfo);
                if (value == null) continue;

                switch (fieldInfo.getDataType()) {
                    case SQLiteDataType.TYPE_INTEGER:
                        contentValues.put(fieldInfo.getField(), (int) value);
                        break;
                    case SQLiteDataType.TYPE_FLOAT:
                        contentValues.put(fieldInfo.getField(), (float) value);
                        break;
                    case SQLiteDataType.TYPE_STRING:
                        contentValues.put(fieldInfo.getField(), (String) value);
                        break;
                    case SQLiteDataType.TYPE_BLOB:
                        contentValues.put(fieldInfo.getField(), (byte[]) value);
                        break;
                    case SQLiteDataType.TYPE_BOOLEAN:
                        contentValues.put(fieldInfo.getField(), (boolean) value);
                        break;
                    default: break;
                }
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return contentValues;
    }

    @SuppressWarnings({"TryWithIdenticalCatches", "unchecked"})
    private E resolveRecord(Cursor cursor) {
        E instance = null;
        Class clsType;
        Object value = null;
        try {
            instance = cls.newInstance();
            for (FieldInfo fieldInfo : fieldInfoList) {
                switch (fieldInfo.getDataType()) {
                    case SQLiteDataType.TYPE_INTEGER:
                        clsType = Integer.class;
                        value = cursor.getInt(cursor.getColumnIndex(fieldInfo.getField()));
                        break;
                    case SQLiteDataType.TYPE_FLOAT:
                        clsType = Float.class;
                        value = cursor.getFloat(cursor.getColumnIndex(fieldInfo.getField()));
                        break;
                    case SQLiteDataType.TYPE_STRING:
                        clsType = String.class;
                        value = cursor.getString(cursor.getColumnIndex(fieldInfo.getField()));
                        break;
                    case SQLiteDataType.TYPE_BLOB:
                        clsType = byte[].class;
                        value = cursor.getBlob(cursor.getColumnIndex(fieldInfo.getField()));
                        break;
                    case SQLiteDataType.TYPE_BOOLEAN:
                        clsType = Boolean.class;
                        value = Args.intToBool(cursor.getInt(cursor.getColumnIndex(fieldInfo.getField())));
                        break;
                    default:
                        clsType = null;
                        break;
                }

                if (fieldInfo.isForeign()) {
                    value = relationshipRely(fieldInfo, value, clsType);
                    if (value != null) clsType = fieldInfo.getNativeType();
                }

                if (clsType == null) continue;

                bind(instance, fieldInfo, value, clsType);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Model relyOn(String references, String relation, Class<E> cls) {
        SQLiteDaoImpl<Model> sqLiteDao = (SQLiteDaoImpl<Model>) new SQLiteDaoImpl<>(sqLiteDatabase, cls);
        List<Model> modelList = sqLiteDao.where(references, relation);

        if (modelList == null || modelList.size() == 0)
            return null;

        return modelList.get(0);
    }
}
