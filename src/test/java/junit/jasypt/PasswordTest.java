package junit.jasypt;
import org.junit.jupiter.api.Test;
import artxew.framework.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordTest {

	@Test
	public void encryptTest() {
		String password = "artxew";
		String[] result = StringUtil.hashingPassword(password).split(":");
		String salt = result[0];
		String hash = result[1];
		log.info("{} -> [{}, {}]", password, salt, hash);
	}
}