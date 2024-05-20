package com.psh10066.excelconverter.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

        sheet.rowIterator().forEachRemaining(row -> {
            List<String> cells = new ArrayList<>();
            row.cellIterator().forEachRemaining(cell -> {
                String value = "";
                switch (cell.getCellType()) {
                    case BOOLEAN -> value = String.valueOf(cell.getBooleanCellValue());
                    case NUMERIC -> value = String.valueOf(cell.getNumericCellValue());
                    case STRING -> value = cell.getStringCellValue();
                    case FORMULA -> value = cell.getCellFormula();
                }
                cells.add(value.trim());
            });
            rows.add(cells);
        });

        List<String> headers = rows.removeFirst();
        return new ExcelRecord(headers, rows);
    }
}
