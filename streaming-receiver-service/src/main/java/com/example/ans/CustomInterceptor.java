package com.example.ans;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Intercepting request: " + request.getURI());

        List<String> traceIdHeaders = request.getHeaders().get("uber-trace-id");
        request.getHeaders().remove("uber-trace-id");

        List<String> newTraceIdHeader = Arrays.asList(traceIdHeaders.get(0));
        request.getHeaders().set("uber-trace-id", traceIdHeaders.get(0));

        ClientHttpResponse response = execution.execute(request, body);

        System.out.println("Intercepting response: " + response.getStatusCode());

        return response;
    }
}
