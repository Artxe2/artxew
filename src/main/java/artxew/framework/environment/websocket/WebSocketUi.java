package artxew.framework.environment.websocket;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.StreamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import artxew.framework.decedent.ctrl.BaseController;
import lombok.RequiredArgsConstructor;

@Controller
@Profile("!prod")
@RequestMapping("websocket-ui")
@RequiredArgsConstructor
public class WebSocketUi extends BaseController {

    private static final String SERVER_ENDPOINT = ServerEndpoint.class.getName();

    private static final String WS_API = WsApi.class.getName();

    private final ApplicationContext context;

    private byte[] html;
    
    @GetMapping("index.html")
    public void indexHtml(HttpServletResponse response) throws IOException {

        response.reset();
        response.setContentType("text/html; charset=UTF-8");
        StreamUtils.copy(
            new BufferedInputStream(
                new ByteArrayInputStream(html)
            )
            , response.getOutputStream()
        );
    }

    @PostConstruct
    private void setHtml() throws IOException {
        StringBuilder sb = new StringBuilder("<!DOCTYPE html>");

        sb.append("<html lang=\"en\">");
        appendHead(sb);
        appendBody(sb);
        sb.append("</html>");
        html = sb.toString().getBytes();
    }

    private void appendHead(StringBuilder sb) {
        sb.append("<head>");
        sb.append("  <meta charset=\"UTF-8\">");
        sb.append("  <title>WebSocket Ui</title>");
        sb.append("<style>");
        sb.append("\nhtml, body { width: 100%; height: 100%; }");
        sb.append("\nbody { background-color: #fafafa; }");
        sb.append("\nhtml, body, div { margin: 0; padding: 0; }");
        sb.append("\na { color: black; text-decoration: none; outline: none }");
        sb.append("\nh4 { margin: 0; }");
        sb.append("\ntextarea { display: block; resize: none; }");
        sb.append("\n.fold > .foldable { display: none; }");
        sb.append("\n.topbar { background-color: black; padding: 16px; }");
        sb.append("\n.title { color: white; font-weight: 700; font-family: sans-serif; font-size: 16px; }");
        sb.append("\n.wrapper { max-width: 1460px; margin: 0 auto; padding: 50px 20px 0 20px; font-family: sans-serif; }");
        sb.append("\n.epblock { background: rgba(73,204,144,.1); margin: 0 0 15px; border: 1px solid #49cc90; border-radius: 4px; box-shadow: 0 0 3px rgb(0 0 0 / 19%); color: #3b4151; }");
        sb.append("\n.epblock-summary { border-bottom: 1px solid #49cc90; display: flex; align-items: center; padding: 5px; cursor: pointer; }");
        sb.append("\n.epblock-summary-method { background: #49cc90; font-size: 14px; font-weight: 700; min-width: 80px; padding: 6px 15px; text-align: center; border-radius: 3px; text-shadow: 0 1px 0 rgb(0 0 0 / 10%); color: #fff; }");
        sb.append("\n.epblock-summary-path { font-size: 16px; display: flex; align-items: center;  word-break: break-word; padding: 0 10px; font-family: monospace; font-weight: 600; color: #3b4151; }");
        sb.append("\n.epblock-summary-description { font-size: 13px; flex: 1 1 auto; word-break: break-word; font-family: sans-serif; color: #3b4151; }");
        sb.append("\n.epblock-body { height: auto; border: none; }");
        sb.append("\n.epblock-body-header { display: flex; align-items: center; padding: 8px 20px; background: hsla(0,0%,100%,.8); box-shadow: 0 1px 2px rgb(0 0 0 / 10%) }");
        sb.append("\n.epblock-body-title { font-size: 14px; flex: 1; margin: 0; color: #3b4151; }");
        sb.append("\n.open-btn { font-size: 14px; font-weight: 700; padding: 5px 23px; transition: all .3s; border: 2px solid grey; border-radius: 4px; background: transparent; box-shadow: 0 1px 2px rgb(0 0 0 / 10%); font-family: sans-serif; color: #3b4151; text-transform: none; box-sizing: inherit; font-family: sans-serif; font-size: 14px; line-height: 1.15; margin: 0px; overflow: visible; appearance: button; cursor: pointer; outline: none; font-weight: 700; padding: 5px 23px; transition: all 0.3s ease 0s; border-style: solid; border-color: grey; border-radius: 4px; background-image: initial; background-position: initial; background-size: initial; background-repeat: initial; background-attachment: initial; background-origin: initial; background-clip: initial; 0px 1px 2px; color: rgb(59, 65, 81); }");
        sb.append("\n.connected .open-btn { border-color: #ff6060; background-color: transparent; font-family: sans-serif; color: #ff6060; }");
        sb.append("\n.epblock-body-body { flex: 1; display: flex; flex-direction: column; }");
        sb.append("\n.epblock-body-api { display: flex; padding: 20px; }");
        sb.append("\n.epblock-body-transmit { font-size: 12px; font-weight: bold; padding: 10px; border: none; border-radius: 4px; outline: none; background: #41444e; font-family: monospace; font-weight: 600; color: #fff; }");
        sb.append("\n.epblock-body-transmit:not([readonly]) { min-height: 210px; background: hsla(0,0%,100%,.8); color: #3b4151; }");
        sb.append("\n.execute-btn { box-sizing: inherit; font-family: sans-serif; font-size: 14px; line-height: 1.15; margin: 0px; overflow: visible; appearance: button; cursor: pointer; outline: none; font-weight: 700; padding: 8px 40px; transition: all 0.3s ease 0s; border-style: solid; border-color: rgb(73, 144, 226); border-radius: 4px; background: rgb(73, 144, 226); color: rgb(255, 255, 255); display: none; }");
        sb.append("\n.connected .execute-btn { display: block; margin-top: 50px; }");
        sb.append("\n.epblock-body-receive { font-size: 12px; font-weight: bold; padding: 10px; border: none; border-radius: 4px; outline: none; background: #41444e; font-family: monospace; font-weight: 600; color: #fff; flex: 1; height: 70px; }");
        sb.append("\n.connected .epblock-body-receive { height: 210px; }");

        sb.append("</style></head>");
    }

    private void appendBody(StringBuilder sb) throws IOException {
        sb.append("<body>");

        Resource[] resources = context.getResources(
            "classpath*:artxew/**/*.class"
        );
        
        appendTopbar(sb);
        sb.append("<div class=\"wrapper\">");
        for (Resource resource : resources) {
            MetadataReader reader = new SimpleMetadataReaderFactory()
                    .getMetadataReader(resource);

            if (reader.getAnnotationMetadata().hasAnnotation(SERVER_ENDPOINT)) {
                appendEndPointBlock(sb, reader);
            }
        }
        sb.append("</div>");
        appendScript(sb);
        sb.append("</body>");
    }

    private void appendTopbar(StringBuilder sb) {
        sb.append("<div class=\"topbar\">");
        sb.append("  <span class=\"title\">WebSocket Ui</span>");
        sb.append("</div>");
    }

    private void appendEndPointBlock(StringBuilder sb, MetadataReader reader) {
        String uri = (String) reader.getAnnotationMetadata().getAnnotationAttributes(SERVER_ENDPOINT).get("value");
        String tag = getTag(reader);

        sb.append("<div class=\"epblock fold\" data-uri=\"" + uri +"\">");
        sb.append("  <div class=\"epblock-summary\" onclick=this.parentNode.toggleClass('fold')>");
        sb.append("    <span class=\"epblock-summary-method\">WS</span>");
        sb.append("    <span class=\"epblock-summary-path\">");
        sb.append("      <a href=\"#" + uri +"\">");
        sb.append("        <span>&ZeroWidthSpace;" + uri + "</span>");
        sb.append("      </a>");
        sb.append("    </span>");
        sb.append("    <div class=\"epblock-summary-description\">" + tag + "</div>");
        sb.append("  </div>");
        sb.append("  <div class=\"epblock-body foldable\">");
        sb.append("    <div class=\"epblock-body-header\">");
        sb.append("      <div class=\"epblock-body-title\">");
        sb.append("        <h4>Transmit Message</h4>");
        sb.append("      </div>");
        sb.append("      <button class=\"open-btn\" onclick=\"connection(this.parentNode.parentNode.parentNode)\">Open</button>");
        sb.append("    </div>");
        sb.append("    <div class=\"epblock-body-api\">");
        sb.append("      <div class=\"epblock-body-body\"  data-uri=\"" + uri +"\">");
        sb.append("        <textarea class=\"epblock-body-transmit\" value='ASDF' readonly></textarea>");
        sb.append("        <button class=\"execute-btn\" onclick=\"sendMessage(this.parentNode)\">Execute</button>");
        sb.append("      </div>");
        sb.append("    </div>");
        sb.append("    <div class=\"epblock-body-header\">");
        sb.append("      <div class=\"epblock-body-title\">");
        sb.append("        <h4>Receive Message</h4>");
        sb.append("      </div>");
        sb.append("    </div>");
        sb.append("    <div class=\"epblock-body-api\">");
        sb.append("      <textarea class=\"epblock-body-receive\" read-only></textarea>");
        sb.append("    </div>");
        sb.append("  </div>");
        sb.append("</div>");
    }

    private String getTag(MetadataReader reader) {
        Map<String, Object> map = reader.getAnnotationMetadata().getAnnotationAttributes(WS_API);

        if (map != null) {
            return (String) map.get("value");
        }
        return reader.getClassMetadata().getClassName();
    }

    private void appendScript(StringBuilder sb) {
        sb.append("<script>");

        sb.append("\nHTMLElement.prototype.toggleClass = function(c) {");
        sb.append("\n  if (new RegExp('(^|\\\\s)' + c + '(\\\\s|$)').test(this.className)) {");
        sb.append("\n      this.className = this.className.replace(new RegExp('(^|\\\\s)' + c), '').trim();");
        sb.append("\n  } else if (this.className) {");
        sb.append("\n    this.className += ' ' + c");
        sb.append("\n  } else {");
        sb.append("\n    this.className = c");
        sb.append("\n  }");
        sb.append("\n}\n");

        sb.append("\nfunction connection(el) {");
        sb.append("\n  const uri = el.getAttribute('data-uri');");
        sb.append("\n  if (new RegExp('(^|\\\\s)connected(\\\\s|$)').test(el.className)) {");
        sb.append("\n    window.socket[uri].close();");
        sb.append("\n    window.socket[uri] = null;");
        sb.append("\n    el.querySelector('.open-btn').innerText = 'Open';");
        sb.append("\n    el.className = el.className.replace(new RegExp('(^|\\\\s)connected'), '').trim();");
        sb.append("\n    el.querySelector('.epblock-body-transmit').readOnly = true;");
        sb.append("\n    el.querySelector('.epblock-body-receive').value = '';");
        sb.append("\n  } else {");
        sb.append("\n    if (!window.socket) {");
        sb.append("\n      window.socket = {};");
        sb.append("\n    }");
        sb.append("\n    window.socket[uri] = new WebSocket('ws://' + window.location.host + uri);");
        sb.append("\n    window.socket[uri].addEventListener('message', function(e) {");
        sb.append("\n      const receive = el.querySelector('.epblock-body-receive');");
        sb.append("\n      receive.value = receive.value + e.data + '\\n'");
        sb.append("\n      receive.scrollTop = receive.scrollHeight");
        sb.append("\n    })");
        sb.append("\n    el.querySelector('.open-btn').innerText = 'Close';");
        sb.append("\n    el.className += ' connected'");
        sb.append("\n    el.querySelector('.epblock-body-transmit').readOnly = false;");
        sb.append("\n  }");
        sb.append("\n}\n");

        sb.append("\nfunction sendMessage(el) {");
        sb.append("\n  const uri = el.getAttribute('data-uri');");
        sb.append("\n  window.socket[uri].send(el.querySelector('.epblock-body-transmit').value);");
        sb.append("\n}\n");

        sb.append("</script>");
    }
}