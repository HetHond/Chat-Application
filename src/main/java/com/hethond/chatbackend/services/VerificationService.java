package com.hethond.chatbackend.services;

import com.hethond.chatbackend.exceptions.BadVerificationException;
import com.hethond.chatbackend.exceptions.ExpiredVerificationException;
import com.hethond.chatbackend.exceptions.SmsSubmissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {
    private static final Logger logger = LoggerFactory.getLogger(VerificationService.class);

    private static final String SMS_AUTHOR = "Chat App";

    private static final int CODE_LENGTH = 6;
    public static final int LIFESPAN_MIN = 5;

    private final Random random = new Random();
    private final StringRedisTemplate redisTemplate;
    private final SmsService smsService;

    @Autowired
    public VerificationService(StringRedisTemplate redisTemplate,
                               SmsService smsService) {
        this.redisTemplate = redisTemplate;
        this.smsService = smsService;

        logger.info("VerificationService initialized with code length {} and verification lifespan of {} minutes.", CODE_LENGTH, LIFESPAN_MIN);
    }

    public void generateAndSendCode(final String phone) {
        final String code = generateVerificationCode();
        redisTemplate.opsForValue().set(getRedisKey(phone), code, LIFESPAN_MIN, TimeUnit.MINUTES);
        try {
            smsService.sendMessage(SMS_AUTHOR, phone, code);
        } catch (SmsSubmissionException ignored) {
            // TODO find a way to resolve this instead of ignoring
        }
        logger.info("Generated and sent verification code [{}] to phone number [{}].", code, phone);
    }

    public void verifyCode(final String phone, final String code) {
        final String storedCode = redisTemplate.opsForValue().get(getRedisKey(phone));
        if (storedCode == null) {
            logger.warn("Verification attempt with expired or missing code for phone: {}", phone);
            throw new ExpiredVerificationException("Verification code has expired or does not exist.");
        }

        if (!storedCode.equals(code)) {
            logger.warn("Failed verification attempt for phone [{}] with incorrect code provided", phone);
            throw new BadVerificationException("Incorrect verification code.");
        }

        redisTemplate.opsForValue().getAndDelete(getRedisKey(phone));
        logger.info("Verification successful for phone: {}", phone);
    }


    private String generateVerificationCode() {
        long bound = 1;
        for (int i = 0; i < CODE_LENGTH; i++) { bound *= 10; }
        final long origin = bound / 10;

        final long number = random.nextLong(origin, bound);
        return String.valueOf(number);
    }

    private String getRedisKey(final String phone) {
        return "verification:" + phone;
    }
}
