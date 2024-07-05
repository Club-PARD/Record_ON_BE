package com.pard.record_on_be.projects.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProjectsDTO {
    @Getter
    @Setter
    public static class Create { // create가 dto에서 entity로 저장
        private UUID user_id;
        private String name;
        private Date start_date;
        private Date update_date;
        private Date finish_date;
        private String description;
        private String picture;
        private Integer is_finished;
        private String part;
    }

    @Getter
    @Setter
    public static class ProjectsSearchRequest {
        private UUID user_id;
        private List<String> competency_tag_name;
        private Date start_date;
        private Date finish_date;
        private Integer is_finished;
    }

    @Getter
    @Setter
    public static class ReadDefaultPage {
        private Integer project_id;
        private String project_name;
        private String project_image;
        private Integer is_finished;
        private List<Integer> competency_tag_id;
        private List<String> competency_tag_name;
        private Date start_date;
        private Date finish_date;

        public ReadDefaultPage(User user, Projects projects, List<Integer> competency_tag_id, List<String> competency_tag_name) {
            this.project_id = projects.getId();
            this.project_name = projects.getName();
            this.project_image = projects.getPicture();
            this.is_finished = projects.getIs_finished();
            this.competency_tag_id = competency_tag_id;
            this.competency_tag_name = competency_tag_name;
            this.start_date = projects.getStart_date();
            this.finish_date = projects.getFinish_date();
        }
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadAll {  // read가 eneity에서 읽어와서 dto로 보내주기
        private String user_name;
        private List<ReadDefaultPage> read_default_page;

        public ReadAll(String user_name, List<ReadDefaultPage> read_default_page) {
            this.user_name = user_name;
            this.read_default_page = read_default_page;
        }
    }
}
