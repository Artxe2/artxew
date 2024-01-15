package artxew.config;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import artxew.framework.environment.authcheck.AuthCheckInterceptor;
import artxew.framework.environment.flowlog.FlowLogInterceptor;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	@NonNull 
	private final FlowLogInterceptor flowLogInterceptor;

	@NonNull 
	private final AuthCheckInterceptor authCheckInterceptor;

	@Value("${artxew.file.image}")
	private String imageFilePath;

	/**
	 * @author Artxe2
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	/**
	 * @author Artxe2
	 */
	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("*")
			.allowedHeaders("*")
			.allowCredentials(false)
			.maxAge(7200);
	}

	/**
	 * @author Artxe2
	 */
	@Override
	public void addInterceptors(@NonNull InterceptorRegistry registry) {
		registry.addInterceptor(authCheckInterceptor)
			.addPathPatterns("/**");
		registry.addInterceptor(flowLogInterceptor)
			.addPathPatterns("/**");
	}

	/**
	 * @author Artxe2
	 */
	@Override
	public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
		registry.addResourceHandler("public/**")
			.addResourceLocations("classpath:public/")
			.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
		registry.addResourceHandler("image/**")
			.addResourceLocations("file:///" + imageFilePath + "/")
			.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
	}
}