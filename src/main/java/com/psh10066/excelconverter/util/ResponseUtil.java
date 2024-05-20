package com.psh10066.excelconverter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtil {

    public static ResponseEntity<InputStreamResource> fileResponse(String fileName, MediaType mediaType, byte[] bytes) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentLength(bytes.length)
            .contentType(mediaType)
            .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
    }
}
