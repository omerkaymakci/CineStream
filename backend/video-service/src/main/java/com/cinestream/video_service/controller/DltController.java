package com.cinestream.video_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dlt")
@RequiredArgsConstructor
public class DltController {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @PostMapping("/replay")
    public String replayMovieEvent(@RequestParam String dltTopic, @RequestParam String mainTopic) {
        return "Replay triggered from " + dltTopic + " to " + mainTopic;
    }
}
