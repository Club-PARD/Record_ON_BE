package com.pard.record_on_be.stored_info.controller;

import com.pard.record_on_be.stored_info.dto.StoredTagInfoDTO;
import com.pard.record_on_be.stored_info.entity.StoredQuestionInfo;
import com.pard.record_on_be.stored_info.entity.StoredTagInfo;
import com.pard.record_on_be.stored_info.service.StoredTagInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class StoredTagInfoController {
    private final StoredTagInfoService storedTagInfoService;

    @GetMapping("/get")
    @Operation(summary = "모든 태그와, 질문 불러오기", description = "모든 태그들을 불러오고, 각각의 태그별로 가지고 있는 질문들을 배열로 가지고 옵니다.")
    public List<StoredTagInfoDTO.Read> storedTagInfoDTOList() {
        List<StoredTagInfo> storedTagInfoList = storedTagInfoService.getAllTagsWithQuestions();
        return storedTagInfoList.stream().map(storedTagInfo ->
                new StoredTagInfoDTO.Read(
                        storedTagInfo.getTagName(), storedTagInfo
                        .getQuestionInfoList()
                        .stream()
                        .map(StoredQuestionInfo::getQuestionText)
                        .collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
    }
}
