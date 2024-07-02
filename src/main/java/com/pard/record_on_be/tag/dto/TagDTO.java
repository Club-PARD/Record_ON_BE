package com.pard.record_on_be.tag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.tag.entity.Tag;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class TagDTO {
    @Getter
    @Setter
    public static class Create {
        private String name;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {
        private Integer id;
        private String name;

        public Read(Tag tag) {
            this.id = tag.getId();
            this.name = tag.getName();
        }
    }
}
