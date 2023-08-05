package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.MailRequest;
import com.i_dont_love_null.allergy_safe.properties.AppProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
    private AppProperties appProperties;

    public void sendMail(MailRequest mailRequest) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(mailRequest.getSenderName() + " <" + appProperties.getSenderEmail() + ">");
        message.setRecipients(MimeMessage.RecipientType.TO, mailRequest.getReceiverEmail());
        message.setSubject(mailRequest.getSubject());
        message.setContent(mailRequest.getContent(), "text/html; charset=utf-8");

        mailSender.send(message);
    }
}
