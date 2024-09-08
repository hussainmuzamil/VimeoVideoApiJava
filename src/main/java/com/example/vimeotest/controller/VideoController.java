package com.example.vimeotest.controller;

import com.example.vimeotest.dtos.VideoRequest;
import com.example.vimeotest.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestBody VideoRequest videoRequest) throws IOException {

        String videoLink = videoService.uploadVideo(videoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(videoLink);
    }

}
