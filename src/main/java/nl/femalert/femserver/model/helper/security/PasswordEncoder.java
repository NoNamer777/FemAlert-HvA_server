package nl.femalert.femserver.model.helper.security;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class PasswordEncoder {

    @SuppressWarnings("UnstableApiUsage")
    public String encode(String text) {

        return Hashing.sha256()
            .hashString(text, StandardCharsets.UTF_8)
            .toString();
    }
}
