package artxew.framework.layers.auth.dto.res;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignIn_User_ResDto implements Serializable {

	@NotNull
	@Schema(description = "순번", example = "123456")
	private Long sno;

	@NotEmpty
	@Schema(example = "test1234")
	private String id;

	@NotEmpty
	@Schema(description = "이름", example = "테스트")
	private String nm;

	@JsonIgnore
	private String pwd;

	private Integer err;

	@Schema(description = "user roles", example = "[\"01\", \"02\", \"03\"]")
	private Role[] roles;
	public void setRolesStr(String str) {
		if (str == null || str.isEmpty()) {
			roles = null;
		} else {
			String[] tokens = str.split(",");
			Role[] roles = new Role[tokens.length];
			int index = 0;
			for (String role : tokens) {
				roles[index++] = Role.fromCode(role);
			}
			this.roles = roles;
		}
	}

	@JsonIgnore
	private Boolean blck;

	@NotEmpty
	@Schema(description = "전화번호", example = "01012345678")
	private String tel;
}