package artxew.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private static final String REFRESH_TOKEN_NAME = "refresh-token";

    private static final String ACCESS_TOKEN_NAME = "access-token";

    @Value("${artxew.auth-check.auth-with-jwt:false}")
    private boolean authWithJwt;

    @Bean
    @Profile("prod")
    public UiConfiguration uiConfig() {
        String[] methods = new String[] { "get" };

        return UiConfigurationBuilder.builder()
            .supportedSubmitMethods(methods)
            .build();
    }

    @Bean
    public Docket swaggerFrameworkApi() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("Framework API").build();
        Set<String> consumes = getConsumeContentTypes();
        Set<String> produces = getProduceContentTypes();
        Predicate<RequestHandler> apis = RequestHandlerSelectors.basePackage(
            "artxew.framework.layers"
        );
        Predicate<String> paths = PathSelectors.any();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName("Framework")
                .apiInfo(apiInfo)
                .consumes(consumes)
                .produces(produces)
                .select()
                .apis(apis)
                .paths(paths)
                .build();

        if (authWithJwt) {
            SecurityContext context = securityContext();
            List<SecurityContext> contexts = Arrays.asList(context);
            List<SecurityScheme> schemes = apiKeys();

            docket.securityContexts(contexts)
                    .securitySchemes(schemes);
        }
        return docket;
    }

    @Bean
    public Docket swaggerProjectApi() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("Project API").build();
        Set<String> consumes = getConsumeContentTypes();
        Set<String> produces = getProduceContentTypes();
        Predicate<RequestHandler> apis = RequestHandlerSelectors.basePackage(
            "artxew.project.layers"
        );
        Predicate<String> paths = PathSelectors.any();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName("Project")
                .apiInfo(apiInfo)
                .consumes(consumes)
                .produces(produces)
                .select()
                .apis(apis)
                .paths(paths)
                .build();

        if (authWithJwt) {
            SecurityContext context = securityContext();
            List<SecurityContext> contexts = Arrays.asList(context);
            List<SecurityScheme> schemes = apiKeys();

            docket.securityContexts(contexts)
                    .securitySchemes(schemes);
        }
        return docket;
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();

        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();

        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    private SecurityContext securityContext() { 
        return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
    }

    private List<SecurityReference> defaultAuth() { 
        List<SecurityReference> list = new ArrayList<>();
        AuthorizationScope[] authorizationScopes = { new AuthorizationScope(
            "global"
            , "accessEverything"
        ) }; 

        list.add(new SecurityReference(ACCESS_TOKEN_NAME, authorizationScopes));
        list.add(new SecurityReference(REFRESH_TOKEN_NAME, authorizationScopes));
        return list;
    }

    private List<SecurityScheme> apiKeys() {
        List<SecurityScheme> list = new ArrayList<>();

        list.add(new ApiKey(ACCESS_TOKEN_NAME, ACCESS_TOKEN_NAME, "header"));
        list.add(new ApiKey(REFRESH_TOKEN_NAME, REFRESH_TOKEN_NAME, "header"));
        return list;
    }
}