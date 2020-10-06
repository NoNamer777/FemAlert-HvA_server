package nl.femalert.femserver.model.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityIdConflictException extends RuntimeException {

    public EntityIdConflictException(String message) {
        super(message);
    }
}
