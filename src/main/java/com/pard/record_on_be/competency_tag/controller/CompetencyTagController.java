package com.pard.record_on_be.competency_tag.controller;

import com.pard.record_on_be.competency_tag.service.CompetencyTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompetencyTagController {
    private final CompetencyTagService competencyTagService;
}
