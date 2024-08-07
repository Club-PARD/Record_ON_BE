package com.pard.record_on_be.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class UserDTO {
    @Getter
    @Setter
    public static class Create{
        private String name;
        private String email;
        private String picture;
        private String job;

        public Create(User user) {
            this.name = user.getName();
            this.email = user.getEmail();
            this.picture = user.getPicture();
            this.job = user.getJob();
        }
    }

    @Getter
    @Setter
    public static class RegisterInfo{
        private UUID id;
        private String name;
        private String job;
    }

    @Getter
    @Setter
    public static class UserIdDTO{
        private UUID user_id;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Read{
        private UUID id;
        private String name;
        private String email;
        private String picture;
        private String job;

        public Read(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.picture = user.getPicture();
            this.job = user.getJob();
        }
    }
}
