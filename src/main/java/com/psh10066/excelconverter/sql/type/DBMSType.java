package com.psh10066.excelconverter.sql.type;

import java.util.List;

public interface DBMSType {

    List<String> insertBody(List<String> row, String createdAtName);
}
