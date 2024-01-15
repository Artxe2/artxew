package artxew.framework.util;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

/**
 * @author Artxe2
 */
public class SpringContext {
	protected static ApplicationContext context;
	protected static String activeProfile;

	/**
	 * @author Artxe2
	 */
	private SpringContext() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	public static <T> T getBean(@NonNull Class<T> beanClass){
		return context.getBean(beanClass);
	}

	/**
	 * @author Artxe2
	 */
	public static String getProperty(@NonNull String key){
		return context.getEnvironment().getProperty(key);
	}

	/**
	 * @author Artxe2
	 */
	<T> T getProperty(@NonNull String key, @NonNull Class<T> targetType) {
		return context.getEnvironment().getProperty(key, targetType);
	}

	/**
	 * @author Artxe2
	 */
	public static boolean checkProfile(String profile){
		return activeProfile.equals(profile);
	}
}
