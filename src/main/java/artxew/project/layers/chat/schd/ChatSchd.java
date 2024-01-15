package artxew.project.layers.chat.schd;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import artxew.framework.util.SessionHelper;
import artxew.project.layers.chat.dto.res.QueryNewList_Chat_ResDto;
import artxew.project.layers.chat.svc.ChatSvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Artxe2
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatSchd {
	private final ChatSvc chatSvc;
	private static final ConcurrentHashMap<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

	/**
	 * @author Artxe2
	 */
	@Scheduled(cron = "0/10 * * * * *")
	private void pushNewList_Chat() {
		List<Long> reqDto = new ArrayList<>();
		for (var entry : emitters.entrySet()) {
			reqDto.add(entry.getKey());
		}
		if (reqDto.isEmpty()) {
			return;
		}
		List<QueryNewList_Chat_ResDto> list = chatSvc.queryNewList_Chat(reqDto);
		for (var res : list) {
			List<SseEmitter> emitterList = emitters.get(res.getUserNo());
			if (emitterList != null) {
				int index = emitterList.size();
				while (index-- > 0) {
					try {
						emitterList.get(index).send(
							SseEmitter.event()
								.id("/sse/chat")
								.data(res)
						);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * @author Artxe2
	 */
	public SseEmitter subscribeNew_Chat() throws IOException {
		Long no = SessionHelper.sno();
		List<SseEmitter> emitterList = emitters.computeIfAbsent(no, key -> new ArrayList<>());
		SseEmitter emitter = new SseEmitter();
		emitterList.add(emitter);
		emitter.onCompletion(() -> {
			emitterList.remove(emitter);
			if (emitterList.isEmpty()) {
				emitters.remove(no);
			}
		});
		emitter.onTimeout(() -> {
			emitterList.remove(emitter);
			if (emitterList.isEmpty()) {
				emitters.remove(no);
			}
		});
		emitter.onError(callback -> {
			emitterList.remove(emitter);
			if (emitterList.isEmpty()) {
				emitters.remove(no);
			}
		});
		emitter.send(SseEmitter.event().id("/sse/chat").data("{}"));
		return emitter;
	}
}
