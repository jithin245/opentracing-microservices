package com.example.sns;

import io.jaegertracing.internal.JaegerSpanContext;

import java.math.BigInteger;

public class SpanContextUtils {
    public static JaegerSpanContext getJaegerSpanContext(String uberTraceId) {
        String[] parts = uberTraceId.split(":");
        String traceIdString = parts[0];
        String spanIdString = parts[1];

        long traceId = new BigInteger(traceIdString, 16).longValue();
        long spanId = new BigInteger(spanIdString, 16).longValue();

        JaegerSpanContext spanContext = new JaegerSpanContext(0L, traceId, spanId, traceId, (byte) 1);
        return spanContext;
    }
}
