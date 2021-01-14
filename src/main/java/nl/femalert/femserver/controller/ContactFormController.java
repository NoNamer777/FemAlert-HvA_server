package nl.femalert.femserver.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.ContactForm;
import nl.femalert.femserver.model.entity.Event;
import nl.femalert.femserver.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

import static nl.femalert.femserver.controller.common.dataFetchers.getBooleanValue;
import static nl.femalert.femserver.controller.common.dataFetchers.getStringValue;

@RestController
@RequestMapping("/contact")
public class ContactFormController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/request-contact")
    public ResponseEntity<Object> requestContact(@RequestBody ObjectNode contactData) {
        ContactForm contactForm = getContactFormData(contactData);

        try {
            emailService.sendConfirmationMessageSendMessage(contactForm);
            emailService.forwardEmailToAdministrator(contactForm);

        } catch (MessagingException exception) {
            throw new RuntimeException(exception.getMessage(), exception);
        }

        return ResponseEntity.ok().build();
    }

    public ContactForm getContactFormData(ObjectNode contactFormData) {
        String name = getStringValue(contactFormData, "name");
        String subject = getStringValue(contactFormData, "subject");
        String emailAddress = getStringValue(contactFormData, "email");
        String body = getStringValue(contactFormData, "question");

        ContactForm contactForm = new ContactForm();

        contactForm.setName(name);
        contactForm.setSubject(subject);
        contactForm.setEmailAddress(emailAddress);
        contactForm.setBody(body);

        return contactForm;
    }
}
