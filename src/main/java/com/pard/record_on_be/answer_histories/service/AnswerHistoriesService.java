package com.pard.record_on_be.answer_histories.service;


import com.pard.record_on_be.answer_histories.repo.AnswerHistoriesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerHistoriesService {
    private final AnswerHistoriesRepo answerHistoriesRepo;
}
