package com.psh10066.excelconverter.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public ExcelRecord read(InputStream inputStream) {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("엑셀 읽기에 실패했습니다.");
        }
        Sheet sheet = workbook.getSheetAt(0);

        List<List<String>> rows = new ArrayList<>();

        DataFormatter dataFormatter = new DataFormatter();
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            List<String> cells = new ArrayList<>();
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cells.add(dataFormatter.formatCellValue(row.getCell(j)).strip());
                }
            }
            rows.add(cells);
        }

        List<String> headers = rows.removeFirst();
        return new ExcelRecord(headers, rows);
    }
}
