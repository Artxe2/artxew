package artxew.framework.environment.valid;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @author Artxe2
 */
@Constraint(validatedBy = SafeHtmlValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SafeHtml {

	String message() default "{artxew.framework.environment.valid.SafeHtml.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}