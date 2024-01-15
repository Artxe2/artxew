package artxew.config;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * @author Artxe2
 */
@Configuration
public class SwaggerConfig {

	@Value("${artxew.domain}")
	private String domain;

	@Value("${java.version}")
    private String javaVersion;

	@Value("${artxew.version}")
	private String version;

	/**
	 * @author Artxe2
	 */
	@Bean
	public OpenAPI openAPI() {
		Server server = new Server();
		server.setUrl(domain);
		return new OpenAPI()
			.info(
				new Info()
					.version(version)
					.title("artxew")
					.description(
						new StringBuilder("SpringBoot v")
							.append(SpringBootVersion.getVersion())
							.append(" & Java v")
							.append(javaVersion)
							.toString()
					)
			)
			.servers(List.of(server));
	}

	/**
	 * @author Artxe2
	 */
	@Bean
	public GroupedOpenApi exampleApi() {
		return GroupedOpenApi.builder()
			.group("Example")
			.packagesToScan("artxew.example.layers")
			.build();
	}

	/**
	 * @author Artxe2
	 */
	@Bean
	public GroupedOpenApi frameworkApi() {
		return GroupedOpenApi.builder()
			.group("Framework")
			.packagesToScan("artxew.framework.layers")
			.build();
	}

	/**
	 * @author Artxe2
	 */
	@Bean
	public GroupedOpenApi projectApi() {
		return GroupedOpenApi.builder()
			.group("Project")
			.packagesToScan("artxew.project.layers")
			.build();
	}
}