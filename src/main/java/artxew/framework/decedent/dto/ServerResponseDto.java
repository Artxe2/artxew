package artxew.framework.decedent.dto;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponseDto<T> implements Serializable {

	@JsonProperty("DATA")
	private T data;

	@JsonProperty("MESSAGE")
	@Schema(example = "금지된 요청입니다.")
	private String message;

	@JsonProperty("ERROR")
	@Schema(example = "forbidden")
	private String error;

	/**
	 * @author Artxe2
	 */
	public ServerResponseDto() {
		// for Deserialization
	}

	/**
	 * @author Artxe2
	 */
	public ServerResponseDto(T data) {
		this.data = data;
	}

	/**
	 * @author Artxe2
	 */
	public ServerResponseDto(T data, String message) {
		this.data = data;
		this.message = message;
	}

	/**
	 * @author Artxe2
	 */
	public ServerResponseDto(T data, String message, String error) {
		this.data = data;
		this.message = message;
		this.error = error;
	}
}