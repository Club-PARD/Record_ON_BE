package com.pard.record_on_be.exp_ans_connections.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.exp_ans_connections.entity.ExpAnsConnections;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class ExpAnsConnectionsDTO {
    @Getter
    @Setter
    public static class Create{
        private Integer experience_id;
        private Integer answer_id;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private Integer id;
        private Integer experience_id;
        private Integer answer_id;

        public Read(ExpAnsConnections expAnsConnections) {
            this.id = expAnsConnections.getId();
            this.experience_id = expAnsConnections.getExperience_id();
            this.answer_id = expAnsConnections.getAnswer_id();
        }
    }
}
