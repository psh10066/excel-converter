package com.psh10066.excelconverter.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter implements AutoCloseable {

    private final Workbook workbook;
    private final Sheet sheet;
    private final ByteArrayOutputStream outputStream;

    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet();
        this.outputStream = new ByteArrayOutputStream();
    }

    public ExcelWriter(Workbook workbook) {
        this.workbook = workbook;
        this.sheet = workbook.createSheet();
        this.outputStream = new ByteArrayOutputStream();
    }

    public byte[] getExcelFileBytes() throws IOException {
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    public byte[] getExcelFileBytes(List<List<String>> data) throws IOException {
        this.writeLinesToExcel(data);
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    private void writeLinesToExcel(List<List<String>> data) {
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> rowData = data.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                String value = rowData.get(j);
                row.createCell(j).setCellValue(value);
            }
        }
    }

    @Override
    public void close() throws IOException {
        workbook.close();
        outputStream.close();
    }
}
