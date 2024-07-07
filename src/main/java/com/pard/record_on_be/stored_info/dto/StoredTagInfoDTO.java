package com.pard.record_on_be.stored_info.dto;

import lombok.Getter;

import java.util.List;

public class StoredTagInfoDTO {
    @Getter
    public static class ReadStoredTag {
        private String tag_name;
        private Integer tag_id;
        private List<String> questions;
        private List<Integer> question_ids;

        public ReadStoredTag(String tag_name, Integer tag_id, List<String> questions, List<Integer> question_ids) {
            this.tag_name = tag_name;
            this.tag_id = tag_id;
            this.questions = questions;
            this.question_ids = question_ids;
        }
    }
}
