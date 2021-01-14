package nl.femalert.femserver.controller.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.UserStatus;
import nl.femalert.femserver.model.entity.User;
import nl.femalert.femserver.model.helper.exception.EntityIdConflictException;
import nl.femalert.femserver.model.helper.exception.EntityNotFoundException;
import nl.femalert.femserver.repository.generic.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.femalert.femserver.controller.common.dataFetchers.getBooleanValue;
import static nl.femalert.femserver.controller.common.dataFetchers.getStringValue;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private EntityRepository<User> userRepo;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        User foundUser = userRepo.findById(id);

        if (foundUser == null) {
            throw new EntityNotFoundException(String.format("User with ID: '%s' is not found.", id));
        }
        return foundUser;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody ObjectNode userData, @PathVariable String id) {
        User newUserData = getUserData(userData);
        User oldUser = getUserById(id);

        if (!newUserData.getId().equals(oldUser.getId())) {
            throw new EntityIdConflictException(String.format(
                "Provided User ID: '%s' is not the same as the ID of the User ID in the request body: '%s'",
                newUserData.getId(),
                oldUser.getId()
            ));
        }
        newUserData.setPassword(oldUser.getPassword());
        newUserData.setStatus(oldUser.getStatus());

        User updatedUser = userRepo.save(newUserData);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        User foundUser = getUserById(id);

        userRepo.deleteById(foundUser.getId());
    }

    public static User getUserData(ObjectNode userData) {
        String id = getStringValue(userData, "id");
        String emailAddress = getStringValue(userData, "emailAddress");
        String name = getStringValue(userData, "name");
        String statusTxt = getStringValue(userData, "status");
        Boolean admin = getBooleanValue(userData, "admin");

        User user = new User(id);
        UserStatus status = UserStatus.parse(statusTxt);

        user.setEmailAddress(emailAddress);
        user.setName(name);
        user.setStatus(status);
        user.setAdmin(admin);

        return user;
    }
}
