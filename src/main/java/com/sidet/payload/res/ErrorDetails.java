package com.sidet.payload.res;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private int status;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, int status, String message, String details) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
