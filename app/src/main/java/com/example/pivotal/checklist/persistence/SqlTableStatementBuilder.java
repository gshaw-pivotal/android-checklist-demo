package com.example.pivotal.checklist.persistence;

import java.util.HashMap;
import java.util.Map;

public class SqlTableStatementBuilder {
    private final String mTableName;
    private Map<String, ColumnType> mColumns = new HashMap<>();
    private String mPrimaryKey = null;
    private boolean mAutoIncrement = false;
    private boolean mOnConflictReplace;

    public SqlTableStatementBuilder(String tableName) {
        mTableName = tableName;
    }

    public void setOnConflictReplace(boolean enableReplace) {
        mOnConflictReplace = enableReplace;
    }

    public void addColumn(String columnName, ColumnType type){
        addColumn(columnName, type, false);
    }

    public void addColumn(String columnName, ColumnType type, boolean isPrimaryKey) {
        addColumn(columnName, type, isPrimaryKey, false);
    }

    public void addColumn(String columnName, ColumnType type, boolean isPrimaryKey, boolean isAutoIncrement){
        if (isPrimaryKey) {
            mPrimaryKey = columnName;
            mAutoIncrement = isAutoIncrement;
        }

        mColumns.put(columnName, type);
    }

    public String[] getColumns() {
        return mColumns.keySet().toArray(new String[mColumns.size()]);
    }

    public String buildCreateTableStatement() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ").append(mTableName).append("( ");
        for (String columnName : mColumns.keySet()) {
            stringBuilder.append(" ").append(columnName)
                    .append(" ").append(mColumns.get(columnName).toString());

            if (columnName.equals(mPrimaryKey)) {
                stringBuilder.append(" PRIMARY KEY");

                if (mAutoIncrement) {
                    stringBuilder.append(" AUTOINCREMENT");
                }
                if (mOnConflictReplace) {
                    stringBuilder.append(" ON CONFLICT REPLACE");
                }
            }

            stringBuilder.append(",");
        }

        int stringLen = stringBuilder.length();
        stringBuilder.replace(stringLen - 1, stringLen, ");");

        return stringBuilder.toString();
    }

    public String buildDropTableStatement() {
        return "DROP TABLE IF EXISTS " + mTableName;
    }

    public enum ColumnType {
        TEXT,
        INT,
        FLOAT,
        DOUBLE;

        @Override
        public String toString() {
            switch (this) {
                case TEXT:
                    return "TEXT";
                case INT:
                    return "INTEGER";
                case FLOAT:
                    return "FLOAT";
                case DOUBLE:
                    return "DOUBLE";
            }

            return super.toString();
        }
    }
}
