package artxew.boot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Artxe2
 */
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@EnableWebMvc
@SpringBootApplication(scanBasePackages = "artxew")
public class BootApplication {

	/**
	 * @author Artxe2
	 */
	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}
}