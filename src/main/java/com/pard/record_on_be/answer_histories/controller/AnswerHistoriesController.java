package com.pard.record_on_be.answer_histories.controller;

import com.pard.record_on_be.answer_histories.service.AnswerHistoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerHistoriesController {
    private final AnswerHistoriesService answerHistoriesService;
}
