package com.psh10066.excelconverter.sql.controller;

import com.psh10066.excelconverter.sql.SqlConverter;
import com.psh10066.excelconverter.sql.type.Oracle;
import com.psh10066.excelconverter.util.ExcelReader;
import com.psh10066.excelconverter.util.ExcelRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sql/oracle")
public class OracleController {

    private final SqlConverter sqlConverter = new SqlConverter(new Oracle());

    @GetMapping("/insert")
    public String insert(@RequestParam String tableName,
                         @RequestParam(required = false) String createdAtName,
                         @RequestParam MultipartFile file) throws IOException {

        ExcelRecord record = new ExcelReader().read(file.getInputStream());
        return sqlConverter.insert(tableName, createdAtName, record);
    }
}
