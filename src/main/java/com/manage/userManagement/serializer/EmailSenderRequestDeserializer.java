package com.manage.userManagement.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manage.userManagement.dto.request.EmailSenderRequest;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;

public class EmailSenderRequestDeserializer implements RedisSerializer<EmailSenderRequest> {

    private final ObjectMapper objectMapper;

    public EmailSenderRequestDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(EmailSenderRequest emailSenderRequest) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(emailSenderRequest);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing EmailSenderRequest", e);
        }
    }

    @Override
    public EmailSenderRequest deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, EmailSenderRequest.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing EmailSenderRequest", e);
        }
    }
}
