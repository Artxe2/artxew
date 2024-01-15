package artxew.config.websocket;
import artxew.framework.util.SpringContext;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpointConfig.Configurator;

/**
 * @author Artxe2
 */
public class WsConfig extends Configurator {

	/**
	 * @author Artxe2
	 */
	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		HttpSession session = (HttpSession) request.getHttpSession();
		if (session != null) {
			config.getUserProperties().put("session", session.getId());
		}
	}

	/**
	 * @author Artxe2
	 */
	@Override
	@SuppressWarnings("null")
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return SpringContext.getBean(clazz);
    }
}