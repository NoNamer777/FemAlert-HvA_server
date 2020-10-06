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
        String id = userData.get("id") == null ? null : userData.get("id").asText();
        String emailAddress = userData.get("emailAddress") == null ? null : userData.get("emailAddress").asText();
        String name = userData.get("name") == null ? null : userData.get("name").asText();
        String statusTxt = userData.get("status") == null ? null : userData.get("status").asText();
        Boolean admin = userData.get("admin") == null ? null : userData.get("admin").asBoolean();

        User user = new User(id);
        UserStatus status = UserStatus.parse(statusTxt);

        if (emailAddress != null) user.setEmailAddress(emailAddress);
        if (name != null) user.setName(name);
        if (admin != null) user.setAdmin(admin);
        if (status != null) user.setStatus(status);

        if (user.getId() == null) user.setAdmin(false);

        return user;
    }
}
