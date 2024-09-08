package com.example.vimeotest.service;

import com.example.vimeotest.dtos.VideoRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class VideoService {

    @Value("${vimeo.video.access_key}")
    private String vimeoToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String VIMEO_UPLOAD_ENDPOINT = "https://api.vimeo.com/me/videos";

    public String uploadVideo(VideoRequest videoRequest) throws IOException {

        // Set up the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(vimeoToken);
        headers.set("Accept", "application/vnd.vimeo.*+json;version=3.4");

        // Create the request body as a JSON object
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> upload = new HashMap<>();
        upload.put("approach", "tus");
        upload.put("size", videoRequest.getFileSize());
        requestBody.put("upload", upload);

        // Create the HttpEntity with the JSON body and headers
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange(
                VIMEO_UPLOAD_ENDPOINT,
                HttpMethod.POST,
                request,
                String.class
        );

        // Check the response and throw an error if necessary
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("Unexpected response code: " + response.getStatusCode());
        }

        // Log the response body (optional)
        String responseBody = response.getBody();
//        System.out.println(response.getBody());

        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON string into a JsonNode object
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract the "link" field from the JSON
            String videoLink = rootNode.path("link").asText();

            // Output the link
            System.out.println("Video URL: " + videoLink);
            return videoLink;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
