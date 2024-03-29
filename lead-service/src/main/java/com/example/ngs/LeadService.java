package com.example.ngs;

import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static strman.Strman.toKebabCase;

@SpringBootApplication
@EnableFeignClients
public class LeadService {
    public static void main(String[] args) {
        SpringApplication.run(LeadService.class, args);
    }

}


@FeignClient(name = "activity-service-client", url = "${activity.service.prefix.url}")
interface ActivityServiceClient {

    @PostMapping("/api/v1/activities/random")
    String randomActivity(ActivityPayload activityPayload);

}

@RestController
@RequestMapping("/api/v1/leads")
class NameResource {

    @Autowired
    private ActivityServiceClient activityServiceClient;

    @Autowired
    private Tracer tracer;

    @GetMapping(path = "/random")
    public ResponseEntity<String> name() throws Exception {
        String traceId = tracer.activeSpan().context().toTraceId();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Trace-Id", traceId);

        String activity = activityServiceClient.randomActivity(ActivityUtils.generateRandomActivity());
        String name = toKebabCase(activity);

        return new ResponseEntity<>(name, headers, HttpStatus.OK);
    }


}