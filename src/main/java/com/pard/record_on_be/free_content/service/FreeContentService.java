package com.pard.record_on_be.free_content.service;


import com.pard.record_on_be.free_content.repo.FreeContentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreeContentService {
    private final FreeContentRepo freeContentRepo;
}
