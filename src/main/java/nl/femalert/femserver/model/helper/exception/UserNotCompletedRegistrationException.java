package nl.femalert.femserver.model.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotCompletedRegistrationException extends RuntimeException {

    public UserNotCompletedRegistrationException(String message) {
        super(message);
    }
}
