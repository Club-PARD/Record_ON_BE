package com.pard.record_on_be.stored_info.dto;

import lombok.Getter;

import java.util.List;

public class StoredTagInfoDTO {
    @Getter
    public static class Read {
        private String tag_name;
        private List<String> questions;

        public Read(String tag_name, List<String> questions) {
            this.tag_name = tag_name;
            this.questions = questions;
        }
    }
}
