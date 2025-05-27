package com.foodu.Event.Controller;


import com.foodu.Event.Service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventImageController {
    private final S3Service s3Service;
    @PostMapping("/events/presigned-url")
    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestBody Map<String, String> request) {
        String originalFilename = request.get("filename");

        if (originalFilename == null || originalFilename.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일명이 필요합니다."));
        }


        String objectKey = "image/" + originalFilename;

        String uploadUrl = s3Service.generatePresignedUrl(objectKey);

        return ResponseEntity.ok(Map.of(
                "uploadURL", uploadUrl,
                "filePath", originalFilename  // 프론트가 서버로 다시 보낼 파일명
        ));
    }
}
