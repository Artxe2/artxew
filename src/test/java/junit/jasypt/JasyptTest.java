package junit.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import junit.util.TestUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JasyptTest {
    
    @Test
    public void trueTest() {
        StandardPBEStringEncryptor standardPBEStringEncryptor
                = new StandardPBEStringEncryptor();

        standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
        standardPBEStringEncryptor.setPassword("artxew-enc-key");

        String text = "redis";
        String encoded = standardPBEStringEncryptor.encrypt(text);

        log.info("\"{}\" -> \"{}\"", text, encoded);
        TestUtil.verify(true);
    }
}
