package dev.domainx.backend.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailRequest {
    private String to;
    private String subject;
    private String body;
    private boolean isHtml;
}
