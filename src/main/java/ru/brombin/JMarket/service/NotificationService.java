package ru.brombin.JMarket.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.brombin.JMarket.entity.Item;
import ru.brombin.JMarket.entity.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@AllArgsConstructor
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private final JavaMailSender mailSender;

    public void notifySeller(User seller, Item item) {
        String subject = "Low Quantity Notification";
        String message = String.format("Dear %s, your item '%s' is running low on quantity. Only %d units left.",
                seller.getUsername(), item.getName(), item.getQuantity());

        sendEmail(seller.getEmail(), subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);
            LOGGER.info("Email sent to: {}", to);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email to: {}", to, e);
        }
    }
}