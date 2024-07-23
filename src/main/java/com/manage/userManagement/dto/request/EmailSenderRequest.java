package com.manage.userManagement.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailSenderRequest {
    String to;
    String subject;
    String body;

    public EmailSenderRequest() {
    }

    @JsonCreator
    public EmailSenderRequest(@JsonProperty("to") String to, @JsonProperty("subject") String subject, @JsonProperty("body") String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

}
