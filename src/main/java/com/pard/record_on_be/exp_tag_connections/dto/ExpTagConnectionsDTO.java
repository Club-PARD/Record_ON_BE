package com.pard.record_on_be.exp_tag_connections.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.exp_tag_connections.entity.ExpTagConnections;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class ExpTagConnectionsDTO {
    @Getter
    @Setter
    public static class Create{
        private Integer experience_id;
        private Integer tag_id;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private Integer id;
        private Integer experience_id;
        private Integer tag_id;

        public Read(ExpTagConnections expTagConnections) {
            this.id = expTagConnections.getId();
            this.experience_id = expTagConnections.getExperience_id();
            this.tag_id = expTagConnections.getTag_id();
        }
    }
}
