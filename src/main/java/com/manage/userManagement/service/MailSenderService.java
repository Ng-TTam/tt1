package com.manage.userManagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manage.userManagement.dto.request.EmailSenderRequest;
import io.lettuce.core.RedisBusyException;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MailSenderService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${stream.key}")
    private String streamKey;

    @Value("${group.name}")
    private String groupName;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;

    ObjectMapper objectMapper = new ObjectMapper();

    public String sendNewMail(EmailSenderRequest emailSenderRequest){
        addEmailToStream(emailSenderRequest);
        return "Email event sent!!!";
    }

    private void addEmailToStream(EmailSenderRequest emailSenderRequest) {// using redis storage streams
        String body = emailSenderRequest.getBody();
        for (int i = 1; i <= 50; i++) {
            emailSenderRequest.setBody(body + String.valueOf(i));
            try {
                String json = objectMapper.writeValueAsString(emailSenderRequest);
                Map<String, String> emailMap = new HashMap<>();
                emailMap.put("email", json);
                redisTemplate.opsForStream().add(streamKey, emailMap);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
    }

    @PostConstruct
    public void init() {
        createConsumerGroup();
        createConsumers();
    }

    private void createConsumerGroup() {
        try {
            redisTemplate.opsForStream().createGroup(streamKey, groupName);
            log.info("Consumer group created: {}", groupName);
        } catch (RedisSystemException e) {

            var cause = e.getRootCause();
            if (cause != null && RedisBusyException.class.equals(cause.getClass())) {
                log.info("STREAM - Redis group already exists, skipping Redis group creation: {}", groupName);
            } else throw e;
        }
    }

    private void createConsumers() {
        for (int i = 1; i <= 3; i++) {
            String consumerId = "consumer" + i;
            log.info("create consumer {}", consumerId);
            container.receive(Consumer.from(groupName, consumerId),
                    StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                    this::processMessage
            );
        }
        container.start();
    }

    private void processMessage(MapRecord<String, String, String> record) {
        String jsonString = record.getValue().get("\"email\"");
        jsonString = jsonString.substring(1, jsonString.length() - 1).replaceAll("\\\\", "");

        JSONObject json = new JSONObject(jsonString);
        EmailSenderRequest email = EmailSenderRequest.builder()
                .to(json.getString("to"))
                .subject(json.getString("subject"))
                .body(json.getString("body"))
                .build();

        send(email);

        log.info("Consumer " + Thread.currentThread().getName() + " processed email: "
                    + "To: " + email.getTo()
                    + ", Subject: " + email.getSubject()
                    + ", Body: " + email.getBody());
    
        redisTemplate.opsForStream().acknowledge(streamKey, record);
    }

    private void send(EmailSenderRequest emailSenderRequest) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailSenderRequest.getTo());
            mimeMessageHelper.setSubject(emailSenderRequest.getSubject());
            mimeMessageHelper.setText(emailSenderRequest.getBody());

            mailSender.send(mimeMessage);
            log.info(String.valueOf(emailSenderRequest));
        } catch (Exception e) {
            log.error("Error sending email: " + e.getMessage());
        }
    }
}
