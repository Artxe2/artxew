package artxew.framework.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

public final class StringUtil {

    private static MessageDigest digest;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final String SALT = System.getProperty("environment.password");

    static {
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String toHash(String data) {
        return toHash(data, 1);
    }

    public static String toHash(String data, int repeat) {
        StringBuilder sb = new StringBuilder(SALT);
        
        sb.append(data);

        byte[] hash = sb.toString().getBytes(StandardCharsets.UTF_8);

        do {
            hash = digest.digest(hash);
        } while (--repeat > 0);

        char[] chars = new char[hash.length * 2];
        int index = 0;

        for (byte b : hash) {
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

    private static char toChar(int i) {
        if (i < 10) {
            return (char) (i + 48);
        }
        return (char) (i + 55);
    }

    public static String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            String message = e.getMessage();
            Throwable cause = e.getCause();

            throw new JsonIOException(message, cause);
        }
    }

    public static String toPrettyPJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromJson(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            String message = e.getMessage();
            Throwable cause = e.getCause();
            
            throw new JsonIOException(message, cause);
        }
    }

    public static String evl(String a, String b) {
        if (a == null || a.isEmpty()) {
            return b;
        }
        return a;
    }
}