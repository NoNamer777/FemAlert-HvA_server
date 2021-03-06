package nl.femalert.femserver.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.UserStatus;
import nl.femalert.femserver.model.entity.User;
import nl.femalert.femserver.model.helper.exception.AuthenticationException;
import nl.femalert.femserver.model.helper.exception.EntityAlreadyExistsException;
import nl.femalert.femserver.model.helper.security.JWTokenUtils;
import nl.femalert.femserver.model.helper.security.PasswordEncoder;
import nl.femalert.femserver.repository.entity.UserRepository;
import nl.femalert.femserver.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.net.URI;
import java.util.List;

import static nl.femalert.femserver.controller.common.dataFetchers.getStringValue;
import static nl.femalert.femserver.controller.entity.UserController.getUserData;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTokenUtils tokenUtils;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<User> saveUser(@RequestBody ObjectNode userData) {
        String password = getStringValue(userData, "password");
        User newUser = getUserData(userData);

        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setStatus(UserStatus.REGISTERING);

        List<User> foundUsers = userRepo.findByQuery("find_user_by_email_address", newUser.getEmailAddress());

        if (foundUsers.size() > 0) {
            throw new EntityAlreadyExistsException(
                String.format("Email address '%s' is already registered.", newUser.getEmailAddress())
            );
        }
        User savedUser = userRepo.save(newUser);
        String validationLink = "http://localhost:8080/validate-registration?token=" + tokenUtils.encode(savedUser, 1800);

        URI locationSavedEntity = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();

        try { emailService.sendVerificationMessage(savedUser, validationLink); } catch (MessagingException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return ResponseEntity.created(locationSavedEntity).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody ObjectNode loginData) throws AuthenticationException {
        String emailAddress = getStringValue(loginData, "emailAddress");
        String password = getStringValue(loginData, "password");

        password = passwordEncoder.encode(password);

        List<User> foundUsers = userRepo.findByQuery("find_user_by_email_address", emailAddress);

        if (foundUsers.size() == 0 || !foundUsers.get(0).validatePassword(password)) {
            throw new AuthenticationException("The provided credentials are invalid.");
        }
        String tokenString = tokenUtils.encode(foundUsers.get(0));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenString));
        headers.add("Registration-Completed", Boolean.toString(!(foundUsers.get(0).getStatus() == UserStatus.REGISTERING)));

        return ResponseEntity
            .accepted()
            .headers(headers)
            .body(foundUsers.get(0));
    }
}
