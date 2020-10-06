package nl.femalert.femserver.model;

import java.util.Arrays;

public enum UserStatus {
    REGISTERING("registering"),
    OFFLINE("offline"),
    ONLINE("online"),
    DEACTIVATED("deactivated");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public static UserStatus parse(String text) {
        return Arrays.stream(UserStatus.values())
            .filter(status -> status.value.equals(text))
            .findFirst()
            .orElse(null);
    }

    @Override
    public String toString() {
        return value;
    }
}
