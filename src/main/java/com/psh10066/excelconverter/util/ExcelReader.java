package com.psh10066.excelconverter.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private final DataFormatter dataFormatter = new DataFormatter();

    public Workbook getFormattedWorkbook(InputStream inputStream) {
        Workbook workbook = this.getWorkbook(inputStream);
        workbook.forEach(sheet ->
            sheet.forEach(row ->
                row.forEach(cell ->
                    cell.setCellValue(this.formatValue(cell))
                )
            )
        );
        return workbook;
    }

    public ExcelRecord read(InputStream inputStream) {
        Workbook workbook = this.getWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<List<String>> rows = new ArrayList<>();

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            List<String> cells = new ArrayList<>();
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cells.add(this.formatValue(row.getCell(j)));
                }
            }
            rows.add(cells);
        }

        List<String> headers = rows.removeFirst();
        return new ExcelRecord(headers, rows);
    }

    private Workbook getWorkbook(InputStream inputStream) {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
        try {
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("엑셀 읽기에 실패했습니다.");
        }
    }

    private String formatValue(Cell cell) {
        return dataFormatter.formatCellValue(cell).strip();
    }
}
