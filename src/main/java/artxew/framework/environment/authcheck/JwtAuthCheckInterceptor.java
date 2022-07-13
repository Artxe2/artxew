package artxew.framework.environment.authcheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;

@Component
public final class JwtAuthCheckInterceptor implements HandlerInterceptor {

    @Value("${artxew.auth-check.accept-for-guest-uris}")
    private String[] acceptForGuestUris;

    @Override
    public boolean preHandle(
        HttpServletRequest request
        , HttpServletResponse response
        , Object handler
    ) throws Exception {
        String uri = request.getRequestURI();

        for (String s : acceptForGuestUris) {
            if (uri.startsWith(s)) {
                return true;
            }
        }

        Claims jws = JwtHolder.hold(request, response);

        return jws != null;
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request
        , HttpServletResponse response
        , Object handler
        , Exception ex
    ) throws Exception {
        JwtHolder.release();
    }
}