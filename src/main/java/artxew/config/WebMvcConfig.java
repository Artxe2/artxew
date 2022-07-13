package artxew.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import artxew.framework.environment.authcheck.JwtAuthCheckInterceptor;
import artxew.framework.environment.authcheck.RedisAuthCheckInterceptor;
import artxew.framework.environment.flowlog.FlowLogInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer
        , ApplicationListener<ContextRefreshedEvent> {

    @Value("${artxew.auth-check.auth-with-jwt:false}")
    private boolean authWithJwt;

    @Value("${artxew.domain}")
    private String domain;

    private final FlowLogInterceptor flowLogInterceptor;

    private final JwtAuthCheckInterceptor jwtAuthCheckInterceptor;

    private final RedisAuthCheckInterceptor redisAuthCheckInterceptor;
    

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        return dispatcherServlet;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (authWithJwt) {
            registry.addInterceptor(jwtAuthCheckInterceptor)
                    .addPathPatterns("/**");
        } else {
            registry.addInterceptor(redisAuthCheckInterceptor)
                    .addPathPatterns("/**");
        }
        registry.addInterceptor(flowLogInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("public/**")
                .addResourceLocations("classpath:public/");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        StringBuilder sb = new StringBuilder("\n\n");

        sb.append("Swagger-UI: [");
        sb.append(domain);
        sb.append("/swagger-ui/index.html]\n");
        sb.append("WebSocket-UI: [");
        sb.append(domain);
        sb.append("/websocket-ui/index.html]\n");
        log.info(sb.toString());
    }
}