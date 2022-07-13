package artxew.framework.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UtilsFieldsProvider implements ApplicationContextAware {

    private final Environment environment;

    @Override
    public void setApplicationContext(
        ApplicationContext context
    ) throws BeansException {
        ContextUtil.context = context;
    }

    @PostConstruct
    private void injectFields() {
        MailSender.username = environment.getProperty("artxew.mail.username");
        MailSender.password = environment.getProperty("artxew.mail.password");
        MailSender.address = environment.getProperty("artxew.mail.address");
        SessionMap.authWithJwt = Boolean.valueOf(
            environment.getProperty("artxew.auth-check.auth-with-jwt")
        );
    }
}