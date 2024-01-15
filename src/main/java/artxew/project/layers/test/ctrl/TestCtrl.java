package artxew.project.layers.test.ctrl;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.util.MailSender;
import artxew.project.layers.test.svc.TestSvc;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@Profile({ "dev", "local", "stg" })
@RequestMapping("api/test")
@RequiredArgsConstructor
@RestController
@Tag(name = "기능 테스트")
public class TestCtrl extends BaseController {

	private final TestSvc testSvc;

	/**
	 * @author Artxe2
	 */
	@GetMapping
	public ResponseEntity<ServerResponseDto<
	List<Map<String, Object>>
	>> query() {
		return processResult(testSvc.query());
	}

	/**
	 * @author Artxe2
	 */
	@PostMapping(path = "send-mail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ServerResponseDto<
	String
	>> sendMail(
		@RequestPart("to")
		String to
		, @RequestPart(name = "c", required = false)
		String cc
		, @RequestPart(name = "bcc", required = false)
		String bcc
		, @RequestPart(name = "files", required = false)
		MultipartFile[] files
	) {
		String time = testSvc.currentTime();
		MailSender.send(
			"테스트 이메일"
			, "<h1 style=\"color:green\">테스트 " + time + "</h1>"
			, to, cc, bcc
			, files
		);
		return processResult(time);
	}
}