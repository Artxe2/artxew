package artxew.project.layers.example.ws;

import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import artxew.framework.environment.websocket.SessionGroupHolder;
import artxew.framework.environment.websocket.WsApi;
import artxew.framework.layers.cmminq.svc.CmmInqSvc;
import artxew.framework.util.ContextUtil;
import artxew.framework.util.StringUtil;
import artxew.project.layers.example.dto.ws.ExampleWebSocketDto;
import lombok.extern.slf4j.Slf4j;

@Profile("!prod")
@Slf4j
@Component
@WsApi("Example WebSocket")
@ServerEndpoint(value = "/example")
public class ExampleWs {

    private static final String EXAMPLE = "EXAMPLE";

    private CmmInqSvc cmmInqSvc = ContextUtil.getBean(CmmInqSvc.class);
    
    @OnMessage
    public void onMessage(Session session, String message) throws Exception {
        String id = session.getId();

        log.info("onMessage({}: {})", id, message);

        Set<Session> set = SessionGroupHolder.getGroups(EXAMPLE);
        ExampleWebSocketDto dto = new ExampleWebSocketDto();

        dto.setId(id);
        dto.setProtocol(2);
        dto.setData(message);

        String json;

        for (Session s : set) {
            json = StringUtil.toJson(dto);
            s.getBasicRemote().sendText(json);
        }
    }

    @OnOpen
    public void onOpen(Session session) throws Exception {
        String id = session.getId();

        log.info("onOpen({})", id);

        Set<Session> set = SessionGroupHolder.add(EXAMPLE, session);
        ExampleWebSocketDto dto = new ExampleWebSocketDto();
        String time = cmmInqSvc.currentTime();

        dto.setId(id);
        dto.setProtocol(0);
        dto.setData(time);
        
        String json = StringUtil.toJson(dto);

        session.getBasicRemote().sendText(json);
        dto.setProtocol(1);
        for (Session s : set) {
            if (!session.equals(s)) {
                json = StringUtil.toJson(dto);
                s.getBasicRemote().sendText(json);
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        String id = session.getId();

        log.info("onClose({})", id);

        Set<Session> set = SessionGroupHolder.remove(EXAMPLE, session);
        ExampleWebSocketDto dto = new ExampleWebSocketDto();
        String time = cmmInqSvc.currentTime();

        dto.setId(id);
        dto.setProtocol(9);
        dto.setData(time);

        String json;

        for (Session s : set) {
            json = StringUtil.toJson(dto);
            s.getBasicRemote().sendText(json);
        }
    }

    @OnError
    public void onError(Session session, Throwable e) throws Exception  {
        String id = session.getId();

        log.info("onError({}: {})", id, e);

        ExampleWebSocketDto dto = new ExampleWebSocketDto();

        dto.setId(id);
        dto.setProtocol(8);
        
        String json = StringUtil.toJson(dto);

        session.getBasicRemote().sendText(json);
    }
}
