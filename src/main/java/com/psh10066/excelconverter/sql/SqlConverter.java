package com.psh10066.excelconverter.sql;

import com.psh10066.excelconverter.sql.type.DBMSType;
import com.psh10066.excelconverter.util.ExcelRecord;
import com.psh10066.excelconverter.util.ExcelWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (row.isEmpty() || !StringUtils.hasText(row.get(0))) {
                query
                    .append("\n");
                continue;
            }
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

    public String update(String tableName, ExcelRecord record) {

        List<String> headers = record.headers();
        int splitIndex = record.getFirstBlankIndex();

        String tableInfo = "UPDATE " + tableName + " SET ";

        StringBuilder query = new StringBuilder();
        for (List<String> row : record.rows()) {
            if (row.isEmpty() || !StringUtils.hasText(row.get(0))) {
                query
                    .append("\n");
                continue;
            }

            List<String> setList = new ArrayList<>();
            for (int i = 0; i < splitIndex; i++) {
                setList.add(headers.get(i) + " = " + this.getValue(row.get(i)));
            }

            List<String> whereList = new ArrayList<>();
            for (int i = splitIndex + 1; i < row.size(); i++) {
                whereList.add(headers.get(i) + " = " + this.getValue(row.get(i)));
            }

            query
                .append(tableInfo)
                .append(String.join(", ", setList))
                .append(" WHERE ")
                .append(String.join(" AND ", whereList))
                .append(";\n");
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

    public byte[] insertExcel(InputStream inputStream) {

        String divider = ") VALUES (";
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            String header = line.substring(line.indexOf("(") + 1, line.indexOf(divider));
            data.add(this.getRow(header));

            while (line != null) {
                if (!StringUtils.hasText(line)) {
                    line = reader.readLine();
                    continue;
                }
                String row = line.substring(line.indexOf(divider) + divider.length(), line.lastIndexOf(")"));
                data.add(this.getRow(row));
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ExcelWriter writer = new ExcelWriter()) {
            return writer.getExcelFileBytes(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getRow(String line) {
        return Arrays.stream(line.split(","))
            .map(value -> {
                value = value.strip();
                value = value.startsWith("'") ? value.substring(1) : value;
                value = value.endsWith("'") ? value.substring(0, value.length() - 1) : value;
                return value;
            }).toList();
    }
}
