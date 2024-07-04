package com.pard.record_on_be.stored_info.service;

import com.pard.record_on_be.stored_info.entity.StoredTagInfo;
import com.pard.record_on_be.stored_info.repo.StoredTagInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoredTagInfoService {
    private final StoredTagInfoRepo storedTagInfoRepo;

    public List<StoredTagInfo> getAllTagsWithQuestions(){
        return storedTagInfoRepo.findAll();
    }
}
