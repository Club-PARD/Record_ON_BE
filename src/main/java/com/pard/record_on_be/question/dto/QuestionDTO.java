package com.pard.record_on_be.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.question.entity.Question;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class QuestionDTO {
    @Getter
    @Setter
    public static class Create {
        private Integer tag_id;
        private String content;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {
        private Integer id;
        private Integer tag_id;
        private String content;

        public Read(Question question) {
            this.id = question.getId();
            this.content = question.getContent();
        }
    }
}
