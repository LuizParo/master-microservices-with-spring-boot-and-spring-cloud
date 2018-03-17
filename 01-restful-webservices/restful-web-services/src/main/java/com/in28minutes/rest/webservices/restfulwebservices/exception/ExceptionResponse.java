package com.in28minutes.rest.webservices.restfulwebservices.exception;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class ExceptionResponse {
    private final Date timestamp;
    private final String message;
    private final String details;

    public ExceptionResponse(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public ExceptionResponse(Date timestamp, String message, Collection<String> errors) {
        this(timestamp, message, errors.stream().collect(Collectors.joining(", ", "{", "}")));
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}