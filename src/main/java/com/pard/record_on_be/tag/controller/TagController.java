package com.pard.record_on_be.tag.controller;


import com.pard.record_on_be.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
}
