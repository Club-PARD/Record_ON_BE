package com.pard.record_on_be.free_content.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.free_content.entity.FreeContent;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class FreeContentDTO {
    @Getter
    @Setter
    public static class Create{
        private Integer experience_id;
        private String content;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {
        private Integer id;
        private Integer experience_id;
        private String content;

        public Read(FreeContent freeContent) {
            this.id = freeContent.getId();
            this.experience_id = freeContent.getExperience_id();
            this.content = freeContent.getContent();
        }
    }
}
