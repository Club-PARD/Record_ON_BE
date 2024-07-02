package com.pard.record_on_be.projects.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.projects.entity.Projects;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

public class ProjectsDTO {
    @Getter
    @Setter
    public static class Create { // create가 dto에서 entity로 저장
        private UUID user_id;
        private String name;
        private Date create_date;
        private Date update_date;
        private Date finish_date;
        private String description;
        private String picture;
        private Integer is_finished;
    }


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read {  // read가 eneity에서 읽어와서 dto로 보내주기
        private Integer id;
        private UUID user_id;
        private String name;
        private Date create_date;
        private Date update_date;
        private Date finish_date;
        private String description;
        private String picture;
        private Integer is_finished;

        public Read(Projects projects) {
            this.id = projects.getId();
            this.user_id = projects.getUser_id();
            this.name = projects.getName();
            this.create_date = projects.getCreate_date();
            this.update_date = projects.getUpdate_date();
            this.finish_date = projects.getFinish_date();
            this.description = projects.getDescription();
            this.picture = projects.getPicture();
            this.is_finished = projects.getIs_finished();
        }
    }
}
