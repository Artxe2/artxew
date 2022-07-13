package artxew.framework.environment.flowlog;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class FlowLogHolder {

    static class SignatureLog {

        private String log;
        
        private Deque<String> stack = new ArrayDeque<>();

        private String push(String signature) {
            if (stack.isEmpty()) {
                log = ">> " + signature;
            } else {
                StringBuilder sb = new StringBuilder(log);

                sb.append("\n");
                for (int i = 0; i < stack.size(); i++) {
                    sb.append("\t");
                }
                sb.append("> ");
                sb.append(signature);
                log = sb.toString();
            }
            stack.addLast(signature);
            return log;
        }

        private String pop() {
            StringBuilder sb = new StringBuilder(log);
            String signature = stack.pollLast();

            sb.append("\n");
            for (int i = 0; i < stack.size(); i++) {
                sb.append("\t");
            }
            sb.append(stack.isEmpty() ? "<< " : "< ");
            sb.append(signature);
            log = sb.toString();
            return log;
        }

        private int size() {
            return stack.size();
        }

        private void clear() {
            log = null;
        }
    }

    private static final Map<Thread, SignatureLog> logMap = new HashMap<>();

    private FlowLogHolder() {}

    public static String push(String signature) {
        Thread key = Thread.currentThread();
        SignatureLog sl = logMap.computeIfAbsent(key, k -> new SignatureLog());

        return sl.push(signature);
    }

    public static String pop() {
        Thread key = Thread.currentThread();
        SignatureLog sl = logMap.get(key);
        String log = sl.pop();

        if (sl.size() == 0) {
            sl.clear();
        }
        return log;
    }
}