package com.psh10066.excelconverter.sql.controller;

import com.psh10066.excelconverter.util.ExcelReader;
import com.psh10066.excelconverter.util.ExcelWriter;
import com.psh10066.excelconverter.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Formatted Excel", description = "Excel to formatted excel")
@RestController
@RequestMapping("/format")
public class ExcelFormatter {

    @Operation(summary = "Excel to formatted excel", description = "Excel to formatted excel",
        responses = @ApiResponse(responseCode = "200", description = "Excel",
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(implementation = String.class))
        )
    )
    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InputStreamResource> formatExcel(@Parameter(description = "Excel file to be read") @RequestPart MultipartFile file) throws IOException {

        Workbook workbook = new ExcelReader().getFormattedWorkbook(file.getInputStream());
        byte[] excel;
        try (ExcelWriter writer = new ExcelWriter(workbook)) {
            excel = writer.getExcelFileBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileName = "data.xlsx";
        return ResponseUtil.fileResponse(fileName, MediaType.APPLICATION_OCTET_STREAM, excel);
    }
}
