package nl.femalert.femserver.model.helper.security;

import nl.femalert.femserver.model.helper.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTokenUtils tokenUtils;

    private static final Set<String> SECURED_PATHS = Set.of(
        "/user"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getServletPath();

            if (HttpMethod.OPTIONS.matches(request.getMethod()) || SECURED_PATHS.stream().noneMatch(path::startsWith)) {

                filterChain.doFilter(request, response);
                return;
            }
            String encodedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (encodedToken == null) {
                throw new AuthenticationException("Authentication problem.");
            }
            encodedToken = encodedToken.replace("Bearer ", "");

            JWTokenInfo tokenInfo = tokenUtils.decode(encodedToken);

            request.setAttribute(JWTokenInfo.KEY, tokenInfo);
            filterChain.doFilter(request, response);

        } catch (AuthenticationException exception) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication error");
        }
    }
}
