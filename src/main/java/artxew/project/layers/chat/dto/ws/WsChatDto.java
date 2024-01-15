package artxew.project.layers.chat.dto.ws;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import artxew.framework.util.StringUtil;
import lombok.Data;

/**
 * @author Artxe2
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsChatDto {

	/**
	 * @author Artxe2
	 */
	public enum Protocol {
		OPEN(1),
		MESSAGE(2),
		READ(20),
		IMAGE(21),
		// EMOTICON(22),
		BLOCK(29),
		CLOSE(3),
		ERROR(4);
		private int value;

		@JsonValue
		public int getValue() {
			return value;
		}
		private Protocol(int value) {
			this.value = value;
		}
	}
	private String key = StringUtil.uniqueKey(20);
	private Long userNo;
	private Protocol ptc = Protocol.MESSAGE;
	private Object data;
	private Date time = new Date();
	private Boolean blck;
}