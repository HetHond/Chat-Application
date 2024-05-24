package com.hethond.chatbackend.services;

import com.hethond.chatbackend.exceptions.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    private static final long SESSION_LIFESPAN_MIN = 15;

    private final SecureRandom randomGenerator = new SecureRandom();
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public SessionService(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        logger.info("SessionService initialized with a session lifespan of {} minutes.", SESSION_LIFESPAN_MIN);
    }

    public String createSession(final UUID userId) {
        final byte[] randomBytes = new byte[128];
        randomGenerator.nextBytes(randomBytes);

        final String randomToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        redisTemplate.opsForValue().set(
                getRedisKey(randomToken),
                userId.toString(),
                SESSION_LIFESPAN_MIN,
                TimeUnit.MINUTES);

        logger.debug("Created new session for user id [{}] with token [{}]", userId, randomToken);
        return randomToken;
    }

    public UUID getUserIdBySession(final String token) {
        final String uuid = redisTemplate.opsForValue().get(getRedisKey(token));
        if (uuid == null) {
            logger.warn("Attempted to retrieve session with token [{}] but it was not found or expired.", token);
            throw new SessionNotFoundException("Session token not found or expired.");
        }
        logger.debug("Retrieved user id [{}] from session token [{}]", uuid, token);
        return UUID.fromString(uuid);
    }

    private String getRedisKey(final String sessionToken) {
        return "session:" + sessionToken;
    }
}
