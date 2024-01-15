package artxew.framework.environment.exception;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.yaml.snakeyaml.Yaml;
import artxew.framework.decedent.dto.ServerResponseDto;

/**
 * @author Artxe2
 */
@SuppressWarnings("unchecked")
public final class DefinedException extends RuntimeException {
	private static Map<String, HashMap<String, Object>> exceptions;
	private final ServerResponseDto<Object> body;
	private final String name;

	/**
	 * @author Artxe2
	 */
	static {
		try (
			InputStream inputStream = new BufferedInputStream(
				new ClassPathResource("exception/defined-exception.yml").getInputStream()
			);
		) {
			exceptions = new Yaml().loadAs(inputStream, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @author Artxe2
	 */
	public DefinedException(String key) {
		super(key);
		body = new ServerResponseDto<>(null);
		name = key;
	}

	/**
	 * @author Artxe2
	 */
	public DefinedException(String key, Throwable e) {
		super(key, e);
		body = new ServerResponseDto<>(null);
		name = key;
	}

	/**
	 * @author Artxe2
	 */
	public DefinedException(ServerResponseDto<Object> data, String key, Throwable e) {
		super(key, e);
		if (data == null) {
			body = new ServerResponseDto<>(null);
		} else {
			body = data;
		}
		name = key;
	}

	/**
	 * @author Artxe2
	 */
	public String name() {
		return name;
	}

	/**
	 * @author Artxe2
	 */
	public static DefinedException badRequest(BindingResult br) {
		Map<String, Object> errors = parseError(br);
		ServerResponseDto<Object> resDto= new ServerResponseDto<>(errors);
		return new DefinedException(resDto, "bad-request", null);
	}

	/**
	 * @author Artxe2
	 */
	public static DefinedException internalError(Throwable e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		ServerResponseDto<Object> resDto = new ServerResponseDto<>(errors.toString());
		return new DefinedException(resDto, "internal-error", e);
	}

	/**
	 * @author Artxe2
	 */
	public ResponseEntity<ServerResponseDto<Object>> parseResponse() {
		HashMap<String, Object> e = exceptions.get(name);
		if (e == null) {
			e = exceptions.get("internal-error");
		}
		body.setMessage((String) e.get("message"));
		body.setError(name);
		return ResponseEntity.status((int) e.get("status")).body(body);
	}

	/**
	 * @author Artxe2
	 */
	private static Map<String, Object> parseError(BindingResult br) {
		Map<String, Object> errors = new HashMap<>();
		Map<String, Object> t;
		List<Map<String, Object>> list;
		for (var fe : br.getFieldErrors()) {
			if (fe.getField().contains(".")) {
				String[] arr = fe.getField().split("\\.");
				String key;
				int length = arr.length - 1;
				int index = 0;
				int idx;
				t = errors;
				while (index < length) {
					key = arr[index++];
					if (key.charAt(key.length() - 1) == ']') {
						list = (List<Map<String, Object>>) t.computeIfAbsent(
							key.substring(0, key.indexOf('['))
							, k -> new ArrayList<>()
						);
						idx = Integer.parseInt(key.substring(key.indexOf('[') + 1, key.length() - 1));
						while (list.size() <= idx) {
							list.add(new HashMap<>());
						}
						t = list.get(idx);
						t.put("INDEX", idx);
					} else {
						t = (Map<String, Object>) t.computeIfAbsent(key, k -> new HashMap<>());
					}
				}
				t.put(arr[index], fe.getCode());
			} else {
				errors.put(fe.getField(), fe.getCode());
			}
		}
		return errors;
	}
}