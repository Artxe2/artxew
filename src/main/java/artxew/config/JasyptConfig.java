package artxew.config;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Artxe2
 */
@Configuration
public class JasyptConfig {

	/**
	 * @author Artxe2
	 */
	@Bean
	public StringEncryptor stringEncryptor(Environment environment) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		encryptor.setConfig(config);
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setPassword(System.getProperty("environment.password"));
		config.setKeyObtentionIterations(1000);
		config.setPoolSize(2);
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		return encryptor;
	}
}