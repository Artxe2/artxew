package artxew.framework.util;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@Configuration
@RequiredArgsConstructor
public class UtilsFieldsProvider implements ApplicationContextAware {
	private final Environment environment;

	/**
	 * @author Artxe2
	 */
	@Override
	public void setApplicationContext(
		@NonNull ApplicationContext context
	) throws BeansException {
		SpringContext.context = context;
	}

	/**
	 * @author Artxe2
	 */
	@PostConstruct
	private void injectFields() {
		ChattingHelper.chatFilePath = environment.getProperty("artxew.file.chat");
		MailSender.address = environment.getProperty("artxew.mail.address");
		MailSender.host = environment.getProperty("artxew.mail.host");
		MailSender.password = environment.getProperty("artxew.mail.password");
		MailSender.personal = environment.getProperty("artxew.mail.personal");
		MailSender.port = environment.getProperty("artxew.mail.port");
		MailSender.username = environment.getProperty("artxew.mail.username");
		SpringContext.activeProfile = environment.getProperty("spring.profiles.active");
		StreamResponseWriter.uploadFilePath = environment.getProperty("artxew.file.upload");
	}
}