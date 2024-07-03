package com.pard.record_on_be.exp_competencyTag_connections.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.exp_competencyTag_connections.entity.ExpCompetencyTagConnections;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class ExpCompetencyTagConnectionsDTO {
    @Getter
    @Setter
    public static class Create {
        private Integer project_id;
        private Integer competency_tag_id;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private Integer id;
        private Integer project_id;
        private Integer competency_tag_id;

        public Read(ExpCompetencyTagConnections expCompetencyTagConnections) {
            this.id = expCompetencyTagConnections.getId();
            this.project_id = expCompetencyTagConnections.getProject_id();
            this.competency_tag_id = expCompetencyTagConnections.getCompetency_tag_id();
        }
    }
}
