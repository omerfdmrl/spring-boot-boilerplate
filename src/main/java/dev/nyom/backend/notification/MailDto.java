package dev.nyom.backend.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailDto {
    private String to;
    private String subject;
    private String body;
    private boolean isHtml;
}
