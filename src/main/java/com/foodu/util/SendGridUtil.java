package com.foodu.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Member;

@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class SendGridUtil {

    private final SendGrid sendGrid;

    @Value("${spring.sendgrid.from}")
    private String fromEmail;

    @Value("${spring.sendgrid.template.accepted}")
    private String acceptedTemplateId;

    @Value("${spring.sendgrid.template.rejected}")
    private String rejectedTemplateId;

    public void sendDynamicTemplateEmail(String email, String truckId, String eventId, String templateId) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(email);
        Mail mail = new Mail();

        mail.setFrom(from);
        mail.setTemplateId(templateId); // 동적으로 수락/거절 템플릿 선택 가능

        Personalization personalization = new Personalization();
        personalization.addDynamicTemplateData("truck", truckId);
        personalization.addDynamicTemplateData("event", eventId);
        personalization.addTo(to);
        mail.addPersonalization(personalization);

        send(mail);
    }

    private void send(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);
        log.info("SendGrid Response: {}", response.getStatusCode());
        log.info("SendGrid Response: {}", response.getBody());
        log.info("SendGrid Response: {}", response.getHeaders());
    }

}
