package com.in28minutes.microservices.limitsservice.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class LimitConfiguration {
    private final int maximum;
    private final int minimum;

    @JsonCreator
    public LimitConfiguration(@JsonProperty("maximum") int maximum, @JsonProperty("minimum") int minimum) {
        this.maximum = maximum;
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }
}