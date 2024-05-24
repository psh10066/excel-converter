package com.psh10066.excelconverter.util;

import org.apache.poi.ss.usermodel.DataFormatter;
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

        DataFormatter dataFormatter = new DataFormatter();
        sheet.rowIterator().forEachRemaining(row -> {
            List<String> cells = new ArrayList<>();
            for (int i = 0; i < row.getLastCellNum(); i++) {
                cells.add(dataFormatter.formatCellValue(row.getCell(i)).strip());
            }
            rows.add(cells);
        });

        List<String> headers = rows.removeFirst();
        return new ExcelRecord(headers, rows);
    }
}
