package com.psh10066.excelconverter.util;

import org.springframework.util.StringUtils;

import java.util.List;

public record ExcelRecord(List<String> headers, List<List<String>> rows) {

    public int getFirstBlankIndex() {
        for (int i = 0; i < headers.size(); i++) {
            if (!StringUtils.hasText(headers.get(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException("No blank index found");
    }
}
