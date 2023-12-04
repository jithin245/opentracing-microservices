package com.example.ans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KafkaConsumer {
    @Autowired
    private Tracer tracer;

    @Autowired
    private RestTemplate restTemplate;

    private static final String TOPIC = "my-topic";

    @KafkaListener(topics = TOPIC, groupId = "group_id")
    public void consume(String message) throws JsonProcessingException {
        ActivityPayload activityPayload = new ObjectMapper().readValue(message, ActivityPayload.class);

        String uberTraceId = activityPayload.getUberTraceId();
        Span span = null;
        if (!uberTraceId.isEmpty()) {
            JaegerSpanContext spanContext = SpanContextUtils.getJaegerSpanContext(uberTraceId);
            span = this.tracer
                    .buildSpan("message received in kafka consumer  " + message)
                    .asChildOf(spanContext)
                    .start();
        } else {
            span = this.tracer
                    .buildSpan("message received in kafka consumer  " + message)
                    .start();
        }

        System.out.println("parent trace id " + uberTraceId);
        String[] parts = uberTraceId.split(":");
        parts[1] = span.context().toSpanId();
        parts[2] = generateRandomSpanId();
        String childTraceId = String.join(":", parts);

        System.out.println("parent uber trace id " + uberTraceId);
        System.out.println("child uber trace id  " + childTraceId);

        HttpHeaders headers = new HttpHeaders();
        headers.remove("uber-trace-id");
        headers.add("uber-trace-id", childTraceId);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        setInterceptor();

        ResponseEntity<String> solr = restTemplate.exchange("http://ssi-service:8090/api/v1/ssi/random",
                HttpMethod.GET, entity, String.class);

        System.out.println("made solr api response " + solr.getBody());
        span.finish();
    }

    private void setInterceptor() {
        ClientHttpRequestInterceptor customInterceptor = new CustomInterceptor();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(customInterceptor);
        restTemplate.setInterceptors(interceptors);
    }

    private String generateRandomSpanId() {
        UUID randomUuid = UUID.randomUUID();
        long leastSigBits = randomUuid.getLeastSignificantBits();
        return String.format("%016x", leastSigBits);
    }

}