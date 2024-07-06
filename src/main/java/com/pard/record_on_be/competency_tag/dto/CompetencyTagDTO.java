package com.pard.record_on_be.competency_tag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.competency_tag.entity.CompetencyTag;
import lombok.Getter;
import lombok.Setter;

public class CompetencyTagDTO {
    @Getter
    @Setter
    public static class Create {
        private String name;
    }


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private Integer id;
        private String name;

        public Read(CompetencyTag competencyTag){
            this.id = competencyTag.getId();
            this.name = competencyTag.getName();
        }
    }
}
