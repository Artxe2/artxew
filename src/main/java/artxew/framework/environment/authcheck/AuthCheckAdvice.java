package artxew.framework.environment.authcheck;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import artxew.framework.environment.exception.DefinedException;
import artxew.framework.util.SessionMap;

@Aspect
@Component
public final class AuthCheckAdvice {

    @Before("@annotation(authCheck)")
    public void authCheck(
        JoinPoint joinPoint
        , AuthCheck authCheck
    ) {
        if (SessionMap.auth() == null) {
            throw new DefinedException("unauthorized");
        }
        String[] roles = SessionMap.auth().split(",");
        String[] permitted = authCheck.value();

        for (String r : roles) {
            for (String p : permitted) {
                if (r.equals(p)) {
                    return;
                }
            }
        }
        throw new DefinedException("permission-denied");
    }
}