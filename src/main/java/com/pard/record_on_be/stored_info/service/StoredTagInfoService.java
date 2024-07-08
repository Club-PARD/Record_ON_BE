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

    // 모든 저장되어 있는 태그와 질문을 dto로 만들어 보내줍니다.
    public List<StoredTagInfo> getAllTagsWithQuestions(){
        return storedTagInfoRepo.findAll();
    }
}
