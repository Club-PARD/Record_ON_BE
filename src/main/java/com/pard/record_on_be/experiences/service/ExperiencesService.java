package com.pard.record_on_be.experiences.service;


import com.pard.record_on_be.experiences.repo.ExperiencesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperiencesService {
    private final ExperiencesRepo experiencesRepo;
}
