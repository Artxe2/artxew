package artxew.framework.environment.authcheck;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Artxe2
 * @Role <ul>
	<li>SYSTEM("00")</li>
	<li>SUPER("01")</li>
	<li>ADMIN("02")</li>
	<li>OPER("03")</li>
</ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AuthCheck {

	/**
	 * @author Artxe2
	*/
	public enum Role {
		SYSTEM("00")
		, SUPER("01")
		, ADMIN("02")
		, OPER("03");
		private String code;

		@JsonValue
		public String getCode() {
			return code;
		}
		private Role(String code) {
			this.code = code;
		}
		public static Role fromCode(String code) {
			for (var role : Role.values()) {
				if (role.code.equals(code)) {
					return role;
				}
			}
			return null;
		}
		@Override
		public String toString() {
			return code;
		}
	}
	Role[] value();
}