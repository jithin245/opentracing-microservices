package com.example.ans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityPayload {
    private String id;
    private String leadName;
    private String firstName;
    private String lastName;
    private Integer activityId;
    private String uberTraceId;
}