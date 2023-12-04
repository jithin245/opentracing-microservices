package com.example.ans;

import io.jaegertracing.internal.JaegerSpanContext;

import java.math.BigInteger;

public class SpanContextUtils {
    public static JaegerSpanContext getJaegerSpanContext(String uberTraceId) {
        String[] parts = uberTraceId.split(":");
        String traceIdString = parts[0];
        String parentSpanIdString = parts[1];
        String spanIdString = parts[2];

        long traceId = new BigInteger(traceIdString, 16).longValue();
        long spanId = new BigInteger(parentSpanIdString, 16).longValue();

        JaegerSpanContext spanContext = new JaegerSpanContext(0L, traceId, spanId, traceId, (byte) 1);
        return spanContext;
    }
}
