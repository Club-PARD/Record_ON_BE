package com.pard.record_on_be.answer_histories.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.answer_histories.entity.AnswerHistories;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class AnswerHistoriesDTO {
    @Getter
    @Setter
    public static class Create {
        private Integer question_id;
        private String content;
        private Integer experience_id;
    }


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private Integer id;
        private Integer question_id;
        private String content;
        private Integer experience_id;

        public Read(AnswerHistories answerHistories) {
            this.id = answerHistories.getId();
            this.question_id = answerHistories.getQuestion_id();
            this.content = answerHistories.getContent();
            this.experience_id = answerHistories.getExperience_id();
        }
    }
}
