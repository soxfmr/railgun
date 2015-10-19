package com.soxfmr.railgun.dao;

import com.soxfmr.railgun.contract.BaseDaoContract;
import com.soxfmr.railgun.contract.RelationshipContract;
import com.soxfmr.railgun.meta.FieldInfo;
import com.soxfmr.railgun.meta.ForeignConstrictInfo;
import com.soxfmr.railgun.meta.TableInfo;
import com.soxfmr.railgun.model.Model;
import com.soxfmr.railgun.utils.Args;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class BaseDaoImpl<E extends Model> implements BaseDaoContract<E>,
        RelationshipContract<E> {

    protected Class<E> cls;
    protected String tableName;
    protected String primary;
    protected List<FieldInfo> fieldInfoList;

    public BaseDaoImpl(Class<E> cls) {
        this.cls = cls;
        init(cls);
    }

    private void init(Class<E> cls) {
        TableInfo tableInfo = new TableInfo(cls);
        tableName = tableInfo.getTableName();
        primary = tableInfo.getPrimary();
        fieldInfoList = tableInfo.getFieldInfoList();
    }

    /**
     * Binding the value to a field in object instance
     * @param e The instance of model
     * @param fieldInfo The description of the field
     * @param value The value which will be assign to the field
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected void bind(E e, FieldInfo fieldInfo, Object value, Class type) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        String name = Args.capitalize(fieldInfo.getName());
        Method method = cls.getMethod("set" + name, fieldInfo.getNativeType());
        method.invoke(e, type.cast(value));
    }

    /**
     * Retrieving the value from the specific field
     * @param e The instance of model
     * @param fieldInfo The description of the field
     * @return The value of the field specify
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected Object retrieve(E e, FieldInfo fieldInfo) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        String name = Args.capitalize(fieldInfo.getName());
        Method method = cls.getMethod("get" + name);
        return method.invoke(e);
    }

    @SuppressWarnings("unchecked")
    protected Object relationshipRely(FieldInfo fieldInfo, Object value, Class type) {
        Class nativeType = fieldInfo.getNativeType();
        if (nativeType.getSuperclass() == Model.class) {
            ForeignConstrictInfo csInfo = fieldInfo.getForeignConstrictInfo();

            return relyOn(csInfo.getReferences(), String.valueOf(type.cast(value)), nativeType);
        }
        return null;
    }

    /**
     * Give the value of primary key from a model instance
     * @param e The instance of model
     * @return The value of the primray key
     */
    /*@SuppressWarnings("TryWithIdenticalCatches")
    protected int reflectPrimaryKey(E e) {
        int id = -1;
        if (fieldInfoList == null || Args.isEmpty(primary))
            return id;

        try {
            for (FieldInfo fieldInfo : fieldInfoList) {
                if (! fieldInfo.getField().equals(primary)) continue;
                id = (int) retrieve(e, fieldInfo);
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        return id;
    }*/

}
