package com.psh10066.excelconverter.sql;

import com.psh10066.excelconverter.sql.type.DBMSType;
import com.psh10066.excelconverter.util.ExcelRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

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
        record.rows().forEach(row ->
            query
                .append(tableInfo)
                .append(String.join(", ", this.dbmsType.insertBody(row, createdAtName)))
                .append(");\n")
        );

        return query.toString();
    }
}