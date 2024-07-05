package com.pard.record_on_be.project_data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.project_data.entity.ProjectData;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class ProjectDataDTO {
    @Getter
    @Setter
    public static class Create{
        private Integer experience_id;
        private Integer resources_type;
        private String references_link;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private Integer id;
        private Integer experience_id;
        private Integer resources_type;
        private String references_link;

        public Read(ProjectData projectData) {
            this.id = projectData.getId();
            this.experience_id = projectData.getExperiencesId();
            this.resources_type = projectData.getResources_type();
            this.references_link = projectData.getReferences_link();
        }
    }
}
