package artxew.project.layers.chat.ctrl;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.authcheck.AuthCheck;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import artxew.framework.layers.auth.svc.AuthSvc;
import artxew.framework.util.ChattingHelper;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.StringUtil;
import artxew.project.layers.chat.dto.req.Query_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Create_ChatBlck_ReqDto;
import artxew.project.layers.chat.dto.req.QueryList_ChatBlck_ReqDto;
import artxew.project.layers.chat.dto.req.QueryList_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Create_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Delete_ChatBlck_ReqDto;
import artxew.project.layers.chat.dto.res.Query_Chat_ResDto;
import artxew.project.layers.chat.dto.res.QueryList_ChatBlck_ResDto;
import artxew.project.layers.chat.dto.res.QueryList_Chat_ResDto;
import artxew.project.layers.chat.dto.res.Create_Chat_ResDto;
import artxew.project.layers.chat.dto.ws.WsChatDto;
import artxew.project.layers.chat.dto.ws.WsChatDto.Protocol;
import artxew.project.layers.chat.schd.ChatSchd;
import artxew.project.layers.chat.svc.ChatSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Artxe2
 */
@RequiredArgsConstructor
@RestController
@Tag(name = "채팅 관리")
public class ChatCtrl extends BaseController {
	private final AuthSvc authSvc;
	private final ChatSchd chatSchd;
	private final ChatSvc chatSvc;

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "채팅 내용 차단"
	)
	@PatchMapping("api/chat/{chatNo}/blck/{key}")
	public ResponseEntity<ServerResponseDto<
	WsChatDto
	>> block_Chat(
		@PathVariable("chatNo")
		String chatNo
		, @PathVariable("key")
		String key
	) throws FileNotFoundException, IOException, InterruptedException {
		ChattingHelper.toggleBlock_Chat(chatNo, key, true);
		WsChatDto resDto = ChattingHelper.toggleBlock_Chat(ChattingHelper.ADMIN_CHAT_FILE_NAME, key, true);
		resDto.setPtc(Protocol.BLOCK);
		ChattingHelper.broadcast(chatNo, StringUtil.toJson(resDto));
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(description = "authSvc.checkBlocked_User(reqDto.getUserNo())", summary = "채팅방 생성")
	@PostMapping("api/chat")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ServerResponseDto<
	Create_Chat_ResDto
	>> create_Chat(
		@RequestBody @Valid
		Create_Chat_ReqDto reqDto
	) {
		authSvc.checkBlocked_User(reqDto.getUserNo());
		chatSvc.create_Chat(reqDto);
		Create_Chat_ResDto resDto = new Create_Chat_ResDto();
		resDto.setSno(reqDto.getSno());
		return processResult(resDto, 201);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "채팅 차단")
	@PostMapping("api/chat-blck")
	@ResponseStatus(HttpStatus.CREATED)
	public void create_ChatBlck(
		@RequestBody @Valid
		Create_ChatBlck_ReqDto reqDto
	) {
		chatSvc.create_ChatBlck(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "채팅 차단 해제")
	@DeleteMapping("api/chat-blck")
	@ResponseStatus(HttpStatus.CREATED)
	public void delete_ChatBlck(
		@RequestBody @Valid
		Delete_ChatBlck_ReqDto reqDto
	) {
		chatSvc.delete_ChatBlck(reqDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(description = "authSvc.checkBlocked_User(userNo)", summary = "채팅 방 목록 조회")
	@GetMapping("api/user/{userNo}/chat")
	public ResponseEntity<ServerResponseDto<
	QueryList_Chat_ResDto
	>> queryList_Chat(
		@PathVariable("userNo")
		long userNo
		, @Parameter(name = "queries") @Valid
		QueryList_Chat_ReqDto reqDto
	) {
		authSvc.checkBlocked_User(userNo);
		if (userNo != reqDto.getUserNo()) {
			exception("permission-denied");
		}
		QueryList_Chat_ResDto resDto = chatSvc.queryList_Chat(reqDto);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@Operation(summary = "채팅 차단 목록 조회")
	@GetMapping("api/chat-blck")
	public ResponseEntity<ServerResponseDto<
	QueryList_ChatBlck_ResDto
	>> queryList_ChatBlck(
		@Parameter(name = "queries") @Valid
		QueryList_ChatBlck_ReqDto reqDto
	) {
		QueryList_ChatBlck_ResDto resDto = null;
		if (SessionHelper.roles() == null) {
			reqDto.setReqUserNo(SessionHelper.sno());
		}
		resDto = chatSvc.queryList_ChatBlck(reqDto);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@GetMapping(path = "api/chat/{chatNo}/log", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "채팅 로그 조회"
	)
	public ResponseEntity<ServerResponseDto<
	WsChatDto[]
	>> queryLog_Chat(
		@PathVariable("chatNo")
		String chatNo
	) throws Exception {
		String contents = ChattingHelper.readChattingLog(chatNo);
		WsChatDto[] logs = StringUtil.fromJson(contents, WsChatDto[].class);
		if (ChattingHelper.ADMIN_CHAT_FILE_NAME.equals(chatNo) && logs.length > 2000) {
			logs = ChattingHelper.trimChattingLog(ChattingHelper.ADMIN_CHAT_FILE_NAME, 1000);
		}
		return processResult(logs);
	}

	/**
	 * @author Artxe2
	 */
	@GetMapping("api/chat/{chatNo}")
	@Operation(summary = "채팅 방 정보 조회")
	public ResponseEntity<ServerResponseDto<
	Query_Chat_ResDto
	>> query_Chat(
		@PathVariable("chatNo")
		long chatNo
	) {
		Query_Chat_ReqDto reqDto = new Query_Chat_ReqDto();
		reqDto.setChatNo(chatNo);
		Query_Chat_ResDto resDto = chatSvc.query_Chat(reqDto);
		return processResult(resDto);
	}

	/**
	 * @author Artxe2
	 */
	@GetMapping(path = "sse/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@Operation(
		description = "<a href=\"/sse/chat\" target=\"_blank\">/sse/chat</a>"
		, summary = "새로운 채팅 갯수 구독 SSE"
	)
	public SseEmitter subscribeNew_Chat() throws IOException {
		return chatSchd.subscribeNew_Chat();
	}

	/**
	 * @author Artxe2
	 */
	@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })
	@Operation(
		description = "@AuthCheck({ Role.SUPER, Role.ADMIN, Role.OPER })"
		, summary = "채팅 내용 차단 해제"
	)
	@PatchMapping("api/chat/{chatNo}/ublk/{uuid}")
	public ResponseEntity<ServerResponseDto<
	WsChatDto
	>> unblock_Chat(
		@PathVariable("chatNo")
		String chatNo
		, @PathVariable("uuid")
		String uuid
	) throws FileNotFoundException, IOException, InterruptedException {
		ChattingHelper.toggleBlock_Chat(chatNo, uuid, false);
		WsChatDto resDto = ChattingHelper.toggleBlock_Chat(ChattingHelper.ADMIN_CHAT_FILE_NAME, uuid, false);
		resDto.setPtc(Protocol.BLOCK);
		ChattingHelper.broadcast(chatNo, StringUtil.toJson(resDto));
		return processResult(resDto);
	}
}