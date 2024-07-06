package com.pard.record_on_be.reference.controller;

import com.pard.record_on_be.reference.dto.ReferenceDTO;
import com.pard.record_on_be.reference.service.ReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReferenceController {
    private final ReferenceService referenceService;

    @PostMapping("/reference/get")
    @Operation(summary = "URL을 메타 데이터로 변환", description = "하나의 링크를 보내주고, 해당 링크에 대한 사이트 이름과, 대표 이미지를 받습니다.")
    public ReferenceDTO.UrlMetadata getMetadata(@RequestBody ReferenceDTO.UrlRequest urlRequest) throws IOException {
        return referenceService.fetchMetadata(urlRequest.getUrl());
    }
}
