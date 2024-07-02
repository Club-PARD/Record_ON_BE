package com.pard.record_on_be.free_content.controller;


import com.pard.record_on_be.free_content.service.FreeContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FreeContentController {
    private final FreeContentService freeContentService;
}
