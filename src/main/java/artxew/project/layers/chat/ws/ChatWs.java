package artxew.project.layers.chat.ws;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import com.google.gson.JsonIOException;
import artxew.config.websocket.WsConfig;
import artxew.framework.environment.authcheck.AuthCheck.Role;
import artxew.framework.environment.exception.DefinedException;
import artxew.framework.environment.websocket.WsSessionGroupHolder;
import artxew.framework.layers.auth.svc.AuthSvc;
import artxew.framework.environment.websocket.WsApi;
import artxew.framework.util.ChattingHelper;
import artxew.framework.util.SessionHelper;
import artxew.framework.util.StringUtil;
import artxew.project.layers.chat.dto.req.MarkRead_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.MarkWrite_Chat_ReqDto;
import artxew.project.layers.chat.dto.req.Query_Chat_ReqDto;
import artxew.project.layers.chat.dto.res.Query_Chat_ResDto;
import artxew.project.layers.chat.dto.ws.WsChatDto;
import artxew.project.layers.chat.dto.ws.WsChatDto.Protocol;
import artxew.project.layers.chat.svc.ChatSvc;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Artxe2
 */
@Component
@RequiredArgsConstructor
@ServerEndpoint(value = "/ws/chat/{chatNo}", configurator = WsConfig.class)
@Slf4j
@WsApi("Chatting WebSocket")
public class ChatWs {
	private final AuthSvc authSvc;
	private final ChatSvc chatSvc;
	private final SessionRepository<?> sessionRepository;
	private String sessionId;

	/**
	 * @author Artxe2
	 */
	@OnOpen
	public void onOpen(@PathParam("chatNo") String chatNo, Session session, EndpointConfig config) throws Exception {
		sessionId = (String) config.getUserProperties().get("session");
		if (sessionId == null) {
			throw new DefinedException("unauthorized");
		}
		org.springframework.session.Session httpSession = sessionRepository.findById(sessionId);
		if (httpSession == null) {
			throw new DefinedException("session-expired");
		}
		Long userNo = httpSession.getAttribute(SessionHelper.USER_NO);
		if (userNo == null) {
			throw new DefinedException("unauthorized");
		}
		if (ChattingHelper.ADMIN_CHAT_FILE_NAME.equals(chatNo)) {
			Role[] roles = httpSession.getAttribute(SessionHelper.USER_ROLES);
			boolean permission = false;
			if (roles != null) {
				for (var r : roles) {
					if (r == Role.OPER) {
						permission = true;
						break;
					}
				}
			}
			if (!permission) {
				throw new DefinedException("permission-denied");
			}
		} else {
			Query_Chat_ReqDto reqDto = new Query_Chat_ReqDto();
			reqDto.setUserNo(userNo);
			reqDto.setChatNo(Long.valueOf(chatNo));
			Query_Chat_ResDto resDto = chatSvc.query_Chat(reqDto);
			if (resDto == null) {
				throw new DefinedException("not-found");
			}
			WsChatDto dto = new WsChatDto();
			dto.setUserNo(userNo);
			dto.setPtc(Protocol.READ);
			String json = StringUtil.toJson(dto);
			ChattingHelper.broadcast(chatNo, json);
			StringBuilder message = new StringBuilder("{\"userNo\":")
				.append(userNo)
				.append(",\"ptc\":")
				.append(Protocol.OPEN.getValue())
				.append(",\"data\":")
				.append(ChattingHelper.readChattingLog(chatNo))
				.append("}");
			session.getBasicRemote().sendText(message.toString());
		}
		WsSessionGroupHolder.add(chatNo, session);
	}

	/**
	 * @author Artxe2
	 */
	@OnMessage
	public void onMessage(@PathParam("chatNo") String chatNo, Session session, String message) throws Exception {
		org.springframework.session.Session httpSession = sessionRepository.findById(sessionId);
		if (httpSession == null) throw new DefinedException("session-expired");
		Long userNo = httpSession.getAttribute(SessionHelper.USER_NO);
		if (userNo == null) throw new DefinedException("unauthorized");
		authSvc.checkBlocked_User(userNo);
		WsChatDto dto = StringUtil.fromJson(message, WsChatDto.class);
		dto.setUserNo(userNo);
		String json = StringUtil.toJson(dto);
		switch (dto.getPtc()) {
			case MESSAGE -> {
				String lastChat = (String) dto.getData();
				if (lastChat.length() > 20) {
					lastChat = lastChat.substring(0, 17) + "...";
				}
				MarkWrite_Chat_ReqDto reqDto = new MarkWrite_Chat_ReqDto();
				reqDto.setChatNo(Long.valueOf(chatNo));
				reqDto.setLastChat(lastChat);
				reqDto.setUserNo(userNo);
				chatSvc.markWrite_Chat(reqDto);
				ChattingHelper.writeChatLog(chatNo, json);
			}
			case IMAGE -> {
				MarkWrite_Chat_ReqDto reqDto = new MarkWrite_Chat_ReqDto();
				reqDto.setChatNo(Long.valueOf(chatNo));
				reqDto.setLastChat("[이미지]");
				reqDto.setUserNo(userNo);
				chatSvc.markWrite_Chat(reqDto);
			}
			case READ -> {
				MarkRead_Chat_ReqDto reqDto = new MarkRead_Chat_ReqDto();
				reqDto.setChatNo(Long.valueOf(chatNo));
				reqDto.setUserNo(userNo);
				chatSvc.markRead_Chat(reqDto);
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + dto.getPtc());
		}
		ChattingHelper.broadcast(chatNo, json);
	}

	/**
	 * @author Artxe2
	 */
	@OnClose
	public void onClose(@PathParam("chatNo") String chatNo, Session session) throws Exception {
		WsSessionGroupHolder.remove(chatNo, session);
	}

	/**
	 * @author Artxe2
	 */
	@OnError
	public void onError(@PathParam("chatNo") String chatNo, Session session, Throwable e) throws Exception {
		printError(e);
		WsChatDto dto = new WsChatDto();
		dto.setPtc(Protocol.ERROR);
		if (e instanceof DefinedException) {
			dto.setData(e.getMessage());
		} else if (e instanceof JsonIOException) {
			dto.setData("bad-request");
		}
		String json = StringUtil.toJson(dto);
		session.getBasicRemote().sendText(json);
	}

	/**
	 * @author Artxe2
	 */
	private void printError(Throwable e) {
		e.setStackTrace(Arrays.copyOf(e.getStackTrace(), 5));
		Throwable t = e.getCause();
		while (t != null) {
			t.setStackTrace(Arrays.copyOf(t.getStackTrace(), 5));
			t = t.getCause();
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log.error(sw.toString());
	}
}
