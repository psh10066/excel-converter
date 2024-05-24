package com.psh10066.excelconverter.sql;

import com.psh10066.excelconverter.sql.type.DBMSType;
import com.psh10066.excelconverter.util.ExcelRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SqlConverter {

    private final DBMSType dbmsType;

    public String insert(String tableName, String createdAtName, ExcelRecord record) {

        List<String> headers = record.headers();
        if (StringUtils.hasText(createdAtName)) {
            headers.add(createdAtName);
        }

        String tableInfo = "INSERT INTO " + tableName + " (" + String.join(", ", headers) + ") VALUES (";

        StringBuilder query = new StringBuilder();
        for (List<String> row : record.rows()) {
            row = new ArrayList<>(row.stream()
                .map(this::getValue)
                .toList());

            if (StringUtils.hasText(createdAtName)) {
                row.add(this.dbmsType.nowFunction());
            }

            query
                .append(tableInfo)
                .append(String.join(", ", row))
                .append(");\n");
        }

        return query.toString();
    }

    private String getValue(String value) {
        if (StringUtils.hasText(value)) {
            return "'" + this.dbmsType.getValue(value) + "'";
        } else {
            return "null";
        }
    }
}
