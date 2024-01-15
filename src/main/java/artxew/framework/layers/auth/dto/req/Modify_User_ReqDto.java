package artxew.framework.layers.auth.dto.req;
import com.fasterxml.jackson.annotation.JsonIgnore;
import artxew.framework.util.SessionHelper;
import artxew.project.enums.StringYN;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
public class Modify_User_ReqDto {

	@JsonIgnore
	@Schema(description = "순번", example = "123456")
	private Long sno = SessionHelper.sno();

	@NotEmpty
	@Schema(example = "test1234")
	private String id;

	@NotEmpty
	@Schema(description = "비밀번호", example = "test123!@#")
	private String pwd;

	@JsonIgnore
	@Schema(description = "비밀번호솔트", example = "abcdefg")
	private String pwdSalt;

	@JsonIgnore
	@Schema(description = "비밀번호해시", example = "abcdefg")
	private String pwdHash;

	@JsonIgnore
	@Schema(example = "abcdefg")
	private String ci;

	@JsonIgnore
	@Schema(example = "abcdefg")
	private String di;

	@JsonIgnore
	@Schema(description = "이름", example = "테스트")
	private String nm;

	@JsonIgnore
	@Schema(description = "생년월일", example = "20001212")
	private String bday;

	@JsonIgnore
	@Schema(description = "전화번호", example = "01012345678")
	private String tel;

	@JsonIgnore
	@Schema(description = "성별코드", example = "M")
	private String gndrCd;

	@JsonIgnore
	@Schema(description = "외국인여부", example = "N")
	private StringYN frgnYn;

	@NotEmpty
	@Schema(description = "메일주소", example = "test@test.test")
	private String mail;

	@NotEmpty
	@Schema(description = "우편번호", example = "01234")
	private String post;

	@NotEmpty
	@Schema(description = "집주소", example = "집주소")
	private String homeAddr;

	@NotEmpty
	@Schema(description = "상세주소", example = "상세주소")
	private String dtlAddr;
}