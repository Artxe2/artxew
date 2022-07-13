package artxew.framework.environment.flowlog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class FlowLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request
        , HttpServletResponse response
        , Object handler
    ) throws Exception {
        String concat = concat(request.getMethod(), request.getRequestURI());

        log.info("\n\n{}\n", FlowLogHolder.push(concat));
        return true;
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request
        , HttpServletResponse response
        , Object handler
        , Exception ex
    ) throws Exception {
        log.info("\n\n{}\n", FlowLogHolder.pop());
    }

    private String concat(String method, String uri) {
        int mLength = method.length();
        int uLength = uri.length();
        int index = 0;
        char[] chars = new char[mLength + uLength + 1];

        for (int i = 0; i < mLength; i++) {
            chars[index++] = method.charAt(i);
        }
        chars[index++] = ':';
        for (int i = 0; i < uLength; i++) {
            chars[index++] = uri.charAt(i);
        }
        return new String(chars);
    }
}