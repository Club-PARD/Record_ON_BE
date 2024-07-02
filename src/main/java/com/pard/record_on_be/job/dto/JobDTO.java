package com.pard.record_on_be.job.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.job.entity.Job;
import lombok.Getter;
import lombok.Setter;

public class JobDTO {
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {
        private Integer id;
        private String name;

        public Read(Job job){
            this.id = job.getId();
            this.name = job.getName();
        }
    }
}
