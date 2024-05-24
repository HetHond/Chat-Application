package com.hethond.chatbackend.services;

import com.hethond.chatbackend.exceptions.SmsSubmissionException;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final VonageClient vonageClient;

    @Autowired
    public SmsService(final @Value("${app.vonageKey}") String vonageKey,
                      final @Value("${app.vonageSecret}") String vonageSecret) {
        this.vonageClient = VonageClient.builder()
                .apiKey(vonageKey)
                .apiSecret(vonageSecret)
                .build();

        logger.info("SmsService initialized successfully.");
    }

    public void sendMessage(final String author,
                            final String recipient,
                            final String content) throws SmsSubmissionException {
        final TextMessage textMessage = new TextMessage(author, recipient, content, false);

        final SmsSubmissionResponse response;
        try {
            response = vonageClient.getSmsClient().submitMessage(textMessage);
        } catch (RuntimeException e) {
            logger.error("Failed to send sms message due to Vonage API error.");
            throw new SmsSubmissionException("Vonage API error: " + e.getMessage());
        }

        final SmsSubmissionResponseMessage responseMessage = response.getMessages().getFirst();
        if (responseMessage.getStatus() != MessageStatus.OK) {
            final String errorMessage = responseMessage.getErrorText();
            logger.error("Failed to send SMS from {} to {}: {}", author, recipient, errorMessage);
            throw new SmsSubmissionException(errorMessage);
        }

        logger.info("SMS sent successfully from {} to {}", author, recipient);
    }
}
