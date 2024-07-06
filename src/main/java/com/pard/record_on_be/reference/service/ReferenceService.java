package com.pard.record_on_be.reference.service;

import com.pard.record_on_be.reference.dto.ReferenceDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ReferenceService {
    public ReferenceDTO.UrlMetadata fetchMetadata(String url) throws IOException {
        String decodedUrl, title = "", imageUrl = "";
        try {
            // URL 디코딩
            decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
            System.out.println("디코딩된 URL: " + decodedUrl);

            // URL에서 메타데이터 추출
            Document doc = Jsoup.connect(decodedUrl).get();
            title = doc.title();
            Element metaImage = doc.select("meta[property=og:image]").first();
            imageUrl = metaImage != null ? metaImage.attr("content") : "";
        } catch (Exception e) {
            e.printStackTrace();
            // 예외가 발생한 경우 적절한 메시지와 함께 빈 값을 반환
            return new ReferenceDTO.UrlMetadata("Error fetching metadata", "");
        }

        // 메타데이터를 담은 객체 반환
        return new ReferenceDTO.UrlMetadata(title, imageUrl);
    }
}
