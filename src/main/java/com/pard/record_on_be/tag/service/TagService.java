package com.pard.record_on_be.tag.service;


import com.pard.record_on_be.tag.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepo tagRepo;
}
