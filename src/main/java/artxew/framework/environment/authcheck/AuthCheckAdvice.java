package artxew.framework.environment.authcheck;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.util.SessionHelper;
import jakarta.servlet.http.HttpSession;

/**
 * @author Artxe2
 */
@Aspect
@Component
public final class AuthCheckAdvice {

	/**
	 * @author Artxe2
	 */
	@Before("@annotation(authCheck)")
	public void authCheck(
		JoinPoint joinPoint
		, AuthCheck authCheck
	) {
		HttpSession session = SessionHelper.session(false);
		if (session == null) {
			throw new DefinedException("unauthorized");
		}
		Role[] roles = SessionHelper.roles();
		if (roles == null) {
			throw new DefinedException("permission-denied");
		}
		Role[] permitted = authCheck.value();
		for (var r : roles) {
			for (var p : permitted) {
				if (r == p) {
					return;
				}
			}
		}
		throw new DefinedException("permission-denied");
	}
}