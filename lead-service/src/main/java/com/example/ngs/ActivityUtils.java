package com.example.ngs;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class ActivityUtils {

    public static ActivityPayload generateRandomActivity() {
        return ActivityPayload.builder()
                .id(RandomStringUtils.randomAlphanumeric(10))
                .firstName(RandomStringUtils.randomAlphabetic(5))
                .lastName(RandomStringUtils.randomAlphabetic(5))
                .leadName(RandomStringUtils.randomAlphabetic(3))
                .activityId(12)
                .build();
    }
}
