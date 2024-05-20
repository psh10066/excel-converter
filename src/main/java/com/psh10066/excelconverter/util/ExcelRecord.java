package com.psh10066.excelconverter.util;

import java.util.List;

public record ExcelRecord(List<String> headers, List<List<String>> rows) {
}
