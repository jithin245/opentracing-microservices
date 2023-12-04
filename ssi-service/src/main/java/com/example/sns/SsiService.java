package com.example.sns;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
public class SsiService {

    public static void main(String[] args) {
        SpringApplication.run(SsiService.class, args);
    }

}

@FeignClient(name = "solr-service-client", url = "${solr.service.prefix.url}")
interface SolrServiceClient {

    @GetMapping("/todo")
    String randomCall();
}


@RestController
@RequestMapping("/api/v1/ssi")
class SsiResource {

    @Autowired
    private Tracer tracer;

    private final List<String> ssiNames;
    private Random random;

    @Autowired
    private SolrServiceClient solrServiceClient;

    public SsiResource() throws IOException {
        InputStream inputStream = new ClassPathResource("/ssi.txt").getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            ssiNames = reader.lines().collect(Collectors.toList());
        }
        random = new Random();
    }

    @GetMapping(path = "/random")
    public String name(@RequestHeader HttpHeaders headers) throws JsonProcessingException {
        System.out.println("headers " + new ObjectMapper().writeValueAsString(headers));
        String name = ssiNames.get(random.nextInt(ssiNames.size()));
        solrServiceClient.randomCall();
        return name;
    }
}