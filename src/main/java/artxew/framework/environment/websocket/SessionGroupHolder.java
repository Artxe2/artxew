package artxew.framework.environment.websocket;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

public final class SessionGroupHolder {

    private static final Map<String, Set<Session>> sessionGroupMap = new HashMap<>();

    private SessionGroupHolder() {}

    public static Set<Session> add(
        String group
        , Session session
    ) {
        Set<Session> set = sessionGroupMap
                .computeIfAbsent(group, k -> new HashSet<>());

        set.add(session);
        return set;
    }

    public static Set<Session> getGroups(String group) {
        return sessionGroupMap.get(group);
    }

    public static Deque<Set<Session>> getGroupsOfSession(Session session ) {
        Deque<Set<Session>> queue = new ArrayDeque<>();

        for (Set<Session> set : sessionGroupMap.values()) {
            if (set.contains(session)) {
                queue.add(set);
            }
        }
        return queue;
    }

    public static Set<Session> remove(
        String group
        , Session session
    ) {
        Set<Session> set = sessionGroupMap.get(group);

        set.remove(session);
        return set;
    }
}