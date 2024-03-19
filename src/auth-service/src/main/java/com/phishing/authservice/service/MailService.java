package com.phishing.authservice.service;

import com.phishing.authservice.exception.exceptions.AuthCodeExpireException;
import com.phishing.authservice.exception.exceptions.FailCreateKeyException;
import com.phishing.authservice.exception.exceptions.InvalidAuthCodeException;
import com.phishing.authservice.exception.exceptions.FailSendEmailException;
import com.phishing.authservice.redis.RedisDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisDao redisDao;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpireTime;

    public void sendMail(String toMail) {
        // Send mail
        log.info("Send mail to {}", toMail);

        String title = "PhishingBlock Mail verification";
        SimpleMailMessage message = createEmailForm(toMail, title, createRandomKey());
        try {
            javaMailSender.send(message);
            redisDao.setRedisValues(toMail, message.getText(),
                    Duration.ofMillis(authCodeExpireTime));
        } catch (Exception e) {
            log.error("Failed to send mail", e);
            throw new FailSendEmailException("Failed to send mail");
        }
    }

    public void verifyMail(String toMail, String authCode) {
        // Verify mail
        log.info("Verify mail to {}", toMail);

        String redisAuthCode = redisDao.getRedisValues(toMail);
        if (redisAuthCode == null) {
            log.error("Auth code is expired");
            throw new AuthCodeExpireException("Auth code is expired");
        }
        if (!redisAuthCode.equals(authCode)) {
            log.error("Auth code is not matched");
            throw new InvalidAuthCodeException("Auth code is not matched");
        }
    }

    private SimpleMailMessage createEmailForm(String toMail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    private String createRandomKey() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < length; i++) {
                key.append(random.nextInt(10));
            }
            return key.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to create random key", e);
            throw new FailCreateKeyException("Failed to create random key");
        }
    }


}
