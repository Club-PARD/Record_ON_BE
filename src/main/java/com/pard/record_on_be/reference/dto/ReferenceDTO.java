package com.pard.record_on_be.reference.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ReferenceDTO {


    @Getter
    @Setter
    public static class UrlCollectRequest {
        private UUID user_id;
        private Integer project_id;
    }

    @Getter
    @Setter
    public static class UrlRequest {
        private String url;
    }

    @Getter
    @Setter
    public static class UrlMetadata {
        private String title;
        private String imageUrl;

        public UrlMetadata(String title, String imageUrl) {
            this.title = title;
            this.imageUrl = imageUrl;
        }
    }
}
