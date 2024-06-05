package com.psh10066.excelconverter.sql.controller;

import com.psh10066.excelconverter.sql.SqlConverter;
import com.psh10066.excelconverter.sql.type.Oracle;
import com.psh10066.excelconverter.util.ExcelReader;
import com.psh10066.excelconverter.util.ExcelRecord;
import com.psh10066.excelconverter.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Oracle", description = "Excel to oracle SQL")
@RestController
@RequestMapping("/sql/oracle")
public class OracleController {

    private final SqlConverter sqlConverter = new SqlConverter(new Oracle());

    @Operation(summary = "Excel to oracle insert query", description = "Excel to oracle insert query",
        responses = @ApiResponse(responseCode = "200", description = "Oracle insert query",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(implementation = String.class))
        )
    )
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InputStreamResource> insert(@Parameter(description = "Table name") @RequestPart String tableName,
                                                      @Parameter(description = "Created at field name") @RequestPart(required = false) String createdAtName,
                                                      @Parameter(description = "Excel file to be read") @RequestPart MultipartFile file) throws IOException {

        ExcelRecord record = new ExcelReader().read(file.getInputStream());
        String query = sqlConverter.insert(tableName, createdAtName, record);

        String fileName = tableName + ".sql";
        return ResponseUtil.fileResponse(fileName, MediaType.TEXT_PLAIN, query.getBytes());
    }

    @Operation(summary = "Excel to oracle update query", description = "Excel to oracle update query",
        responses = @ApiResponse(responseCode = "200", description = "Oracle update query",
            content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(implementation = String.class))
        )
    )
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InputStreamResource> update(@Parameter(description = "Table name") @RequestPart String tableName,
                                                      @Parameter(description = "Excel file to be read") @RequestPart MultipartFile file) throws IOException {

        ExcelRecord record = new ExcelReader().read(file.getInputStream());
        String query = sqlConverter.update(tableName, record);

        String fileName = tableName + ".sql";
        return ResponseUtil.fileResponse(fileName, MediaType.TEXT_PLAIN, query.getBytes());
    }

    @Operation(summary = "Oracle insert query to excel", description = "Oracle insert query to excel",
        responses = @ApiResponse(responseCode = "200", description = "Excel",
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(implementation = String.class))
        )
    )
    @PostMapping(value = "/insert/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InputStreamResource> insertExcel(@Parameter(description = "Oracle insert query file to be read") @RequestPart MultipartFile file) throws IOException {

        byte[] excel = sqlConverter.insertExcel(file.getInputStream());

        String fileName = "data.xlsx";
        return ResponseUtil.fileResponse(fileName, MediaType.APPLICATION_OCTET_STREAM, excel);
    }
}
