package com.example.ans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @Autowired
    private Tracer tracer;

    private static final String TOPIC = "my-topic";

    @KafkaListener(topics = TOPIC, groupId = "group_id")
    public void consume(String message) throws JsonProcessingException {
        ActivityPayload activityPayload = new ObjectMapper().readValue(message, ActivityPayload.class);

        String uberTraceId = activityPayload.getUberTraceId();

        if (!uberTraceId.isEmpty()) {
            JaegerSpanContext spanContext = SpanContextUtils.getJaegerSpanContext(uberTraceId);
            this.tracer
                    .buildSpan("message received in kafka consumer  " + message)
                    .asChildOf(spanContext)
                    .start()
                    .finish();
        } else {
            this.tracer
                    .buildSpan("message received in kafka consumer  " + message)
                    .withTag("my-trace-id", activityPayload.getTraceId())
                    .start()
                    .finish();
        }
    }

}