package com.example.ans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.cloud.ExtensionTags;
import io.opentracing.tag.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
public class ActivityService {

    public static void main(String[] args) {
        SpringApplication.run(ActivityService.class, args);
    }

}

@RestController
@RequestMapping("/api/v1/activities")
class ActivityResource {

    private static final Logger logger = LoggerFactory.getLogger(ActivityResource.class);

    private final List<String> activityNames;
    private Random random;

    @Autowired
    private Tracer tracer;

    @Autowired
    private KafkaProducer kafkaProducer;


    public ActivityResource() throws IOException {
        InputStream inputStream = new ClassPathResource("/acitvities.txt").getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            activityNames = reader.lines().collect(Collectors.toList());
        }
        random = new Random();
    }

    @PostMapping(path = "/random")
    public String name(@RequestHeader HttpHeaders headers, @RequestBody  ActivityPayload activityPayload) throws JsonProcessingException {
        Span span = this.tracer.buildSpan("Activity received from lead service  " + new ObjectMapper().writeValueAsString(activityPayload))
                .start();
        span.finish();

        String uberTraceId = headers.getOrEmpty("uber-trace-id").stream().findFirst().orElse("");
        System.out.println("parent trace id " + uberTraceId);
        String[] parts = uberTraceId.split(":");
        parts[1] = span.context().toSpanId();
        parts[2] = generateRandomSpanId();
        String childTraceId = String.join(":", parts);
        activityPayload.setUberTraceId(childTraceId);
        kafkaProducer.sendMessage(new ObjectMapper().writeValueAsString(activityPayload));
        String name = activityNames.get(random.nextInt(activityNames.size()));

        return name;
    }

    private String generateRandomSpanId() {
        UUID randomUuid = UUID.randomUUID();
        long leastSigBits = randomUuid.getLeastSignificantBits();
        return String.format("%016x", leastSigBits);
    }

}

