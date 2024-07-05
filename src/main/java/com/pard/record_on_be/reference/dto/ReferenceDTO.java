package com.pard.record_on_be.utill.dto;

import lombok.Getter;
import lombok.Setter;

public class ReferenceDTO {
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
