package com.pard.record_on_be.utill.service;

import com.pard.record_on_be.utill.dto.ReferenceDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.jsoup.*;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReferenceService {
    public ReferenceDTO.UrlMetadata fetchMetadata(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        Element metaImage = doc.select("meta[property=og:image]").first();

        String imageUrl = metaImage != null ? metaImage.attr("content") : "";

        return new ReferenceDTO.UrlMetadata(title, imageUrl);
    }
}
