package junit.util;
public class TestUtil {
	public static void verify(boolean b) {
		if (!b) {
			throw new RuntimeException();
		}
	}
}