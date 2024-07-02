package com.pard.record_on_be.job.controller;

import com.pard.record_on_be.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
}
