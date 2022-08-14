package com.eaisign.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MailRequest {
    private String toEmail;
    private String subject;
    private String body;
    private MultipartFile[] files;
}
