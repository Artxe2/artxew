package artxew.framework.util;
import java.io.IOException;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import artxew.framework.environment.flowlog.FlowLogHolder;

/**
 * @author Artxe2
 */
public class RestClient {

	/**
	 * @author Artxe2
	 */
	private static final RestTemplate restTemplate = new RestTemplateBuilder(rt -> {})
		.errorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
				return false;
			}
			@Override
			public void handleError(@NonNull ClientHttpResponse response) throws IOException {}
		})
		.setConnectTimeout(Duration.ofSeconds(3))
		.setReadTimeout(Duration.ofSeconds(5))
		.build();

	/**
	 * @author Artxe2
	 */
	private RestClient() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @author Artxe2
	 */
	@SuppressWarnings("null")
	public static ResponseEntity<String> get(
		@NonNull
		String url
		, @NonNull
		HttpHeaders headers
	) {
		StringBuilder sb = new StringBuilder("RestClient.get(")
			.append(url)
			.append(", {headers})");
		FlowLogHolder.touch(sb.toString());
		return restTemplate.exchange(
			url
			, HttpMethod.GET
			, new HttpEntity<Object>(headers)
			, String.class
		);
	}

	/**
	 * @author Artxe2
	 */
	public static ResponseEntity<String> method(
		@NonNull
		HttpMethod method
		, @NonNull
		String url
		, Object body
		, HttpHeaders headers
	) {
		StringBuilder sb = new StringBuilder("RestClient.method(")
			.append(method)
			.append(", ")
			.append(url)
			.append(", {body}, {headers})");
		FlowLogHolder.touch(sb.toString());
		return restTemplate.exchange(
			url
			, method
			, new HttpEntity<Object>(body, headers)
			, String.class
		);
	}
}
