package com.psh10066.excelconverter.sql.type;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Oracle implements DBMSType {

    @Override
    public List<String> insertBody(List<String> row, String createdAtName) {
        row = new ArrayList<>(row.stream()
            .map(cell -> {
                if (StringUtils.hasText(cell)) {
                    return "'" + cell.replaceAll("'", "''") + "'";
                } else {
                    return "null";
                }
            })
            .toList());

        if (StringUtils.hasText(createdAtName)) {
            row.add("SYSDATE");
        }

        return row;
    }
}
