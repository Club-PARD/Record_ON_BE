package com.pard.record_on_be.experiences.controller;


import com.pard.record_on_be.experiences.service.ExperiencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExperiencesController {
    private final ExperiencesService experiencesService;
}
