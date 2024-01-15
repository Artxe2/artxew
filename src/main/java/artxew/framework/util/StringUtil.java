package artxew.framework.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import artxew.framework.environment.exception.DefinedException;

/**
 * @author Artxe2
 */
public final class StringUtil {
	private static MessageDigest digest;
	private static SecretKeyFactory key;
	private static final SecureRandom random = new SecureRandom();
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static final String SALT = System.getProperty("environment.password");

	/**
	 * @author Artxe2
	 */
	static {
		try {
			digest = MessageDigest.getInstance("SHA3-256");
			key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
		objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
		objectMapper.getFactory()
			.setStreamReadConstraints(
				StreamReadConstraints.builder()
					.maxStringLength(Integer.MAX_VALUE)
					.build()
			);
	}

	/**
	 * @author Artxe2
	 */
	private StringUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	public static String toHash(String data) {
		byte[] hash = digest.digest(
			new StringBuilder(SALT).append(data)
				.toString()
				.getBytes(StandardCharsets.UTF_8)
		);
		char[] chars = new char[hash.length * 2];
		int index = 0;
		for (var b : hash) {
			int i = b + 128;
			if (i < 16) {
				chars[index++] = '0';
				chars[index++] = toChar(i);
			} else {
				chars[index++] = toChar(i / 16);
				chars[index++] = toChar(i % 16);
			}
		}
		return new String(chars);
	}

	/**
	 * @author Artxe2
	 */
	public static String masking(String input, int start, int end) {
		if (input == null) return null;
		char[] chars = input.toCharArray();
		end = Math.min(end, chars.length);
		while (start < end) {
			chars[start++] = '*';
		}
		return new String(chars);
	}

	/**
	 * @author Artxe2
	 */
	private static char toChar(int i) {
		if (i < 10) {
			return (char) (i + 48);
		}
		return (char) (i + 55);
	}

	/**
	 * @author Artxe2
	 */
	public static String toJson(Object value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			String message = e.getMessage();
			Throwable cause = e.getCause();
			throw new JsonIOException(message, cause);
		}
	}

	/**
	 * @author Artxe2
	 */
	public static String toPrettyJson(Object src) {
		return gson.toJson(src);
	}

	/**
	 * @author Artxe2
	 */
	public static <T> T fromJson(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (JsonProcessingException e) {
			String message = e.getMessage();
			Throwable cause = e.getCause();
			throw new JsonIOException(message, cause);
		}
	}

	/**
	 * @author Artxe2
	 */
	public static String evl(String a, String b) {
		if (a == null || a.isEmpty()) {
			return b;
		}
		return a;
	}

	/**
	 * @author Artxe2
	 */
	public static String lpad(String str, int length, char c) {
		int index = str.length();
		char[] padding = new char[length];
		while (index > 0) {
			padding[--length] = str.charAt(--index);
		}
		while (length > 0) {
			padding[--length] = c;
		}
		return new String(padding);
	}

	/**
	 * @author Artxe2
	 */
	public static String rpad(String str, int length, char c) {
		int index = str.length();
		char[] padding = new char[length];
		while (length > index) {
			padding[--length] = c;
		}
		while (index > 0) {
			padding[--index] = str.charAt(index);
		}
		return new String(padding);
	}

	/**
	 * @author Artxe2
	 */
	public static String uniqueKey(int length) {
		if (length < 13) {
			throw new DefinedException("internal-error");
		}
		StringBuilder sb = new StringBuilder(String.valueOf(new Date().getTime()));
		int index = 13;
		while (index++ < length) {
			int r = random.nextInt(36);
			if (r < 10) {
				sb.append(r);
			} else {
				sb.append((char) (r + 87));
			}
		}
		return sb.toString();
	}

	/**
	 * @author Artxe2
	 */
	public static String hashingPassword(String password) {
		byte[] salt = new byte[24];
		random.nextBytes(salt);
		String saltStr = Base64.getEncoder().encodeToString(salt);
		String hash = pbkdf2(password, saltStr);
		return new StringBuilder(saltStr)
			.append(':')
			.append(hash)
			.toString();
	}

	/**
	 * @author Artxe2
	 */
	public static String pbkdf2(String input, String salt) {
		return pbkdf2(input, Base64.getDecoder().decode(salt));
	}

	/**
	 * @author Artxe2
	 */
	private static String pbkdf2(String input, byte[] salt) {
		char[] password = input.toCharArray();
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, 12000, 192);
			return Base64.getEncoder()
				.encodeToString(
					key.generateSecret(spec).getEncoded()
				);
		} catch (InvalidKeySpecException e) {
			throw new DefinedException("internal-error");
		}
	}
}