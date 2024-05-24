package com.psh10066.excelconverter.sql.type;

public class Oracle implements DBMSType {

    @Override
    public String getValue(String value) {
        return value.replaceAll("'", "''");
    }

    @Override
    public String nowFunction() {
        return "SYSDATE";
    }
}
