package com.pard.record_on_be.competency_tag.service;

import com.pard.record_on_be.competency_tag.repo.CompetencyTagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompetencyTagService {
    private final CompetencyTagRepo competencyTagRepo;
}
