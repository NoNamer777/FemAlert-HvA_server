package nl.femalert.femserver.service;

import nl.femalert.femserver.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("no-reply@dnd-mapp.nl");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }

    public void sendVerificationMessage(User user, String verificationLink) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        Context context = new Context();

        context.setVariable("emailAddress", user.getEmailAddress());
        context.setVariable("verificationLink", verificationLink);

        String html = templateEngine.process("registration-email-template", context);

        helper.setTo(user.getEmailAddress());
        helper.setFrom("no-reply@fem-alert.nl");
        helper.setSubject("Validate your Registration");
        helper.setText(html, true);

        emailSender.send(message);
    }

}
