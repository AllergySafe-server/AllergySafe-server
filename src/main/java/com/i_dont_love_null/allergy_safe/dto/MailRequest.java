package com.i_dont_love_null.allergy_safe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Getter
@Setter
@NoArgsConstructor
@Service
public class MailRequest {
    private String receiverEmail;
    private String subject;
    private String content;
    private String senderName;
}
