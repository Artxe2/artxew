package artxew.framework.environment.websocket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jakarta.websocket.Session;

/**
 * @author Artxe2
 */
public final class WsSessionGroupHolder {
	private static final Map<String, Set<Session>> sessionGroupMap = new HashMap<>();

	/**
	 * @author Artxe2
	 */
	private WsSessionGroupHolder() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	public static Set<Session> add(
		String group
		, Session session
	) {
		Set<Session> set = sessionGroupMap.computeIfAbsent(group, k -> new HashSet<>());
		set.add(session);
		return set;
	}

	/**
	 * @author Artxe2
	 */
	public static Set<Session> getGroups(String group) {
		return sessionGroupMap.get(group);
	}

	/**
	 * @author Artxe2
	 */
	public static Deque<Set<Session>> getGroupsOfSession(Session session) {
		Deque<Set<Session>> queue = new ArrayDeque<>();
		for (var set : sessionGroupMap.values()) {
			if (set.contains(session)) {
				queue.add(set);
			}
		}
		return queue;
	}

	/**
	 * @author Artxe2
	 */
	public static Set<Session> remove(
		String group
		, Session session
	) {
		Set<Session> set = sessionGroupMap.get(group);
		if (set != null) {
			set.remove(session);
		}
		return set;
	}
}