package junit.jasypt;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import junit.util.TestUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JasyptTest {

	@Test
	public void encryptTest() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor.setPassword("artxew-enc-key");
		for (var text : new String[] { "artxew" }) {
			encryptAndLogging(encryptor, text);
		}
		TestUtil.verify(true);
	}
	private void encryptAndLogging(StandardPBEStringEncryptor encryptor, String text) {
		log.info("\"{}\" -> ENC({})", text, encryptor.encrypt(text));
	}

	@Test
	public void decryptTest() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor.setPassword("artxew-enc-key");
		for (var text : new String[] { "24mGhCzihhLucu2nF3bJbf1694xXUrbb)" }) {
			decryptAndLogging(encryptor, text);
		}
		TestUtil.verify(true);
	}
	private void decryptAndLogging(StandardPBEStringEncryptor encryptor, String text) {
		log.info("ENC({}) -> \"{}\"", text, encryptor.decrypt(text));
	}
}