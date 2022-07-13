package artxew.framework.util;

import org.springframework.context.ApplicationContext;

public class ContextUtil {

    protected static ApplicationContext context;

    private ContextUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getBean(Class<T> beanClass){
        return context.getBean(beanClass);
    }

    public static String getProperty(String key){
        return context.getEnvironment().getProperty(key);
    }
}
