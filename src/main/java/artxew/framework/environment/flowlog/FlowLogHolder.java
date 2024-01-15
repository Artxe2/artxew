package artxew.framework.environment.flowlog;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Artxe2
 */
public final class FlowLogHolder {

	/**
	 * @author Artxe2
	 */
	static class SignatureLog {
		private StringBuilder logger = new StringBuilder();
		private Deque<String> stack = new ArrayDeque<>();
		private String push(String signature) {
			if (stack.isEmpty()) {
				logger.setLength(0);
			} else {
				logger.append("\n");
				for (int i = 0; i < stack.size(); i++) {
					logger.append("\t");
				}
			}
			stack.addLast(signature);
			return logger.append(logger.isEmpty() ? "\n《→ " : "→ ")
				.append(signature)
				.toString();
		}
		private String pop() {
			String signature = stack.pollLast();
			logger.append("\n");
			for (int i = 0; i < stack.size(); i++) {
				logger.append("\t");
			}
			return logger.append("← ")
				.append(signature)
				.toString();
		}
		private String touch(String signature) {
			if (stack.isEmpty()) {
				return "↓ " + signature;
			}
			logger.append("\n");
			for (int i = 0; i < stack.size(); i++) {
				logger.append("\t");
			}
			return logger.append("↓ ")
				.append(signature)
				.toString();
		}
	}
	private static final ConcurrentHashMap<Thread, SignatureLog> loggerMap = new ConcurrentHashMap<>();

	/**
	 * @author Artxe2
	 */
	private FlowLogHolder() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	public static String push(String signature) {
		Thread key = Thread.currentThread();
		SignatureLog sl = loggerMap.computeIfAbsent(key, k -> new SignatureLog());
		return sl.push(signature);
	}

	/**
	 * @author Artxe2
	 */
	public static String pop() {
		Thread key = Thread.currentThread();
		SignatureLog sl = loggerMap.get(key);
		return sl.pop();
	}

	/**
	 * @author Artxe2
	 */
	public static String touch(String signature) {
		Thread key = Thread.currentThread();
		SignatureLog sl = loggerMap.getOrDefault(key, new SignatureLog());
		return sl.touch(signature);
	}
}