package com.soxfmr.railgun.meta;

public class FieldInfo {
    private String field;
    private int dataType;
    private String name;
    private Class<?> nativeType;
    private ForeignConstrictInfo foreignConstrictInfo;
    private boolean nullable;
    private boolean unique;

    public FieldInfo(String field, int dataType, String name, Class<?> nativeType,
                     boolean nullable, boolean unique) {
        this(field, dataType, name, nativeType, nullable, unique, null);
    }

    public FieldInfo(String field, int dataType, String name, Class<?> nativeType,
                     boolean nullable, boolean unique, ForeignConstrictInfo foreignConstrictInfo) {
        this.field = field;
        this.dataType = dataType;
        this.name = name;
        this.nativeType = nativeType;
        this.foreignConstrictInfo = foreignConstrictInfo;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getNativeType() {
        return nativeType;
    }

    public void setNativeType(Class<?> nativeType) {
        this.nativeType = nativeType;
    }

    public ForeignConstrictInfo getForeignConstrictInfo() {
        return foreignConstrictInfo;
    }

    public void setForeignConstrictInfo(ForeignConstrictInfo foreignConstrictInfo) {
        this.foreignConstrictInfo = foreignConstrictInfo;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isForeign() {
        return foreignConstrictInfo != null;
    }

    public boolean isPrimary(String primary) {
        return field.equals(primary);
    }
}
