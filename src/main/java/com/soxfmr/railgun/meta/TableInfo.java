package com.soxfmr.railgun.meta;

import com.soxfmr.railgun.contract.DatabaseField;
import com.soxfmr.railgun.contract.DatabaseTable;
import com.soxfmr.railgun.utils.Args;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableInfo {

    private String tableName;
    private String primary;
    private int priority;
    private List<FieldInfo> fieldInfoList;

    public TableInfo(Class cls) {
        init(cls);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private void init(Class cls) {
        fieldInfoList = new ArrayList<>();
        // Retrieve the table name
        Annotation annotation = cls.getAnnotation(DatabaseTable.class);
        if (annotation != null) {
            DatabaseTable databaseTable = (DatabaseTable) annotation;
            tableName = databaseTable.tableName();
            priority = databaseTable.priority();
        }
        // Retrieve all of fields
        Field[] fields = cls.getDeclaredFields();
        Field[] parentField = cls.getSuperclass().getDeclaredFields();
        if (fields == null || parentField == null)
            return;

        FieldInfo fieldInfo;
        ForeignConstrictInfo fcInfo;
        DatabaseField databaseField;

        fields = concat(parentField, fields);

        for (Field field : fields) {
            annotation = field.getAnnotation(DatabaseField.class);
            if (annotation == null) continue;

            databaseField = (DatabaseField) annotation;
            fieldInfo = new FieldInfo(databaseField.field(), databaseField.type(),
                    field.getName(), field.getType(),
                    databaseField.nullable(), databaseField.unique());
            // Retrieve the foreign key information if exists
            if (databaseField.foreign()) {
                TableInfo tableInfo = new TableInfo(field.getType());
                if (! Args.isEmpty(tableInfo.getTableName())) {
                    fcInfo = new ForeignConstrictInfo(databaseField.references(), tableInfo.getTableName());
                    fieldInfo.setForeignConstrictInfo(fcInfo);
                }
            }
            // Store the primary key
            if (databaseField.primary()) primary = databaseField.field();

            fieldInfoList.add(fieldInfo);
        }
    }

    private Field[] concat(Field[]...fieldList) {
        int len = 0;
        for (Field[] fields : fieldList) {
            len += fields.length;
        }

        int position = 0;
        Field[] newFields = new Field[len];
        for (Field[] fields : fieldList) {
            System.arraycopy(fields, 0, newFields, position, fields.length);
            position += fields.length;
        }

        return newFields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<FieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }
}
