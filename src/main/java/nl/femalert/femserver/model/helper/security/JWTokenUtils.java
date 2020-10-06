package nl.femalert.femserver.model.helper.security;

import io.jsonwebtoken.*;
import nl.femalert.femserver.model.entity.User;
import nl.femalert.femserver.model.helper.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTokenUtils {

    private static final String JWT_USERID_CLAIM = "sub";
    private static final String JWT_USERNAME_CLAIM = "username";
    private static final String JWT_ADMIN_CLAIM = "admin";

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.pass-phrase}")
    private String passPhrase;

    // Token validity is 6 hours.
    @Value("${jwt.validity-duration}")
    private int expiration;

    /** Provides an encoded JWT Token with the default Token validity duration. */
    public String encode(User user) {
        return this.encode(user, expiration);
    }

    /** Provides an encoded JWT Token with a (possibly) custom Token validity duration. */
    public String encode(User user, int expiration) {
        return Jwts.builder()
            .claim(JWT_USERID_CLAIM, user.getId())
            .claim(JWT_USERNAME_CLAIM, user.getName())
            .claim(JWT_ADMIN_CLAIM, user.isAdmin())
            .setIssuer(issuer)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(getKey(passPhrase), SignatureAlgorithm.HS512)
            .compact();
    }

    public JWTokenInfo decode(String encodedToken) throws AuthenticationException {

        try {

            Key key = getKey(passPhrase);
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(encodedToken);

            Claims claims = jws.getBody();
            JWTokenInfo tokenInfo = new JWTokenInfo();

            tokenInfo.setUserId(claims.get(JWT_USERID_CLAIM).toString());
            tokenInfo.setUsername(claims.get(JWT_USERNAME_CLAIM).toString());
            tokenInfo.setAdmin(Boolean.parseBoolean(claims.get(JWT_ADMIN_CLAIM).toString()));

            return tokenInfo;

        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException exception) {

            throw new AuthenticationException(exception.getMessage());
        }
    }

    public static Key getKey(String passPhrase) {
        return new SecretKeySpec(passPhrase.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }
}
