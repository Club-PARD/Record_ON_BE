package com.pard.record_on_be.job.service;

import com.pard.record_on_be.job.repo.JobRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepo jobRepo;
}
