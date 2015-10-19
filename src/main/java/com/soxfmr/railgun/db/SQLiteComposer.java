package com.soxfmr.railgun.db;

import com.soxfmr.railgun.contract.SchemeComposer;
import com.soxfmr.railgun.meta.FieldInfo;
import com.soxfmr.railgun.meta.ForeignConstrictInfo;
import com.soxfmr.railgun.meta.TableInfo;
import com.soxfmr.railgun.utils.Args;

public class SQLiteComposer implements SchemeComposer {

    private static final String SCHEME_TEMPLATE_DEFAULT = "%s %s";

    private static final String SCHEME_TEMPLATE_PRIMARY = "%s %s PRIMARY KEY";

    private static final String SCHEME_TEMPLATE_NULLABLE = "%s %s NULLABLE";

    private static final String SCHEME_TEMPLATE_UNIQUE = "%s %s UNIQUE";

    private static final String SCHEME_TEMPLATE_FOREIGN = "FOREIGN KEY(%s) REFERENCES %s(%s)";

    private static final String SCHEME_TEMPLATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (%s)";

    @Override
    public String composeTable(TableInfo tableInfo) {
        if (tableInfo == null)
            return null;

        if (Args.isEmpty(tableInfo.getTableName()))
            throw new IllegalArgumentException("Invalid table name");

        String template;
        ForeignConstrictInfo fcInfo;
        StringBuilder builder = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            if (fieldInfo.isPrimary(tableInfo.getPrimary())) {
                template = SCHEME_TEMPLATE_PRIMARY;
            } else if (fieldInfo.isNullable()) {
                template = SCHEME_TEMPLATE_NULLABLE;
            } else if (fieldInfo.isUnique()) {
                template = SCHEME_TEMPLATE_UNIQUE;
            } else {
                template = SCHEME_TEMPLATE_DEFAULT;
            }

            builder.append( String.format( template, fieldInfo.getField(),
                            SQLiteDataType.translate(fieldInfo.getDataType()) )
            );
            builder.append(",");

            if (fieldInfo.isForeign()) {
                fcInfo = fieldInfo.getForeignConstrictInfo();
                builder.append(String.format(SCHEME_TEMPLATE_FOREIGN, fieldInfo.getField(),
                        fcInfo.getRelateTable(), fcInfo.getReferences()));
                builder.append(",");
            }
        }

        // Remove a comma in the end of string
        String columnScheme = builder.toString();
        columnScheme = columnScheme.substring(0, columnScheme.length() - 1);

        return String.format(SCHEME_TEMPLATE_TABLE, tableInfo.getTableName(), columnScheme);
    }
}
