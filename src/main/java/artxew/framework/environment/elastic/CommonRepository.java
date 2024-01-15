package artxew.framework.environment.elastic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import artxew.framework.environment.exception.DefinedException;
import artxew.framework.environment.flowlog.FlowLogHolder;
import artxew.framework.util.StringUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author Artxe2
 */
@ConditionalOnProperty("artxew.use.elastic")
@Component
@RequiredArgsConstructor
public class CommonRepository {

	private static final String DOC = "/_doc/";
	private static final String _DATA = ", {data})";
	private static final String ELASTIC_SEARCH_ERROR = "elastic-search-error";
	private final RestClient restClient;

	/**
	 * @author Artxe2
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> select(String index, String id) {
		return select(index, id, Map.class);
	}

	/**
	 * @author Artxe2
	 */
	public <T> T select(String index, String id, Class<T> valueType) {
		StringBuilder sb = new StringBuilder("CommonRepository.select(")
				.append(index)
				.append(", ")
				.append(id)
				.append(')');
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		sb.append("/")
				.append(index)
				.append(DOC)
				.append(id);
		Request request = new Request("GET", sb.toString());
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), valueType);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public Object insert(String index, Object data) {
		StringBuilder sb = new StringBuilder("CommonRepository.insert(")
				.append(index)
				.append(_DATA);
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		sb.append("/")
				.append(index)
				.append(DOC);
		Request request = new Request("POST", sb.toString());
		request.setJsonEntity(StringUtil.toJson(data));
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public Object insert(String index, String id, Object data) {
		StringBuilder sb = new StringBuilder("CommonRepository.insert(")
				.append(index)
				.append(", ")
				.append(id)
				.append(_DATA);
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		sb.append("/")
				.append(index)
				.append("/_create/")
				.append(id);
		Request request = new Request("POST", sb.toString());
		request.setJsonEntity(StringUtil.toJson(data));
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public Object merge(String index, String id, Object data) {
		StringBuilder sb = new StringBuilder("CommonRepository.merge(")
				.append(index)
				.append(", ")
				.append(id)
				.append(_DATA);
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		sb.append("/")
				.append(index)
				.append(DOC)
				.append(id);
		Request request = new Request("PUT", sb.toString());
		request.setJsonEntity(StringUtil.toJson(data));
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public Object update(String index, String id, Object data) {
		StringBuilder sb = new StringBuilder("CommonRepository.update(")
				.append(index)
				.append(", ")
				.append(id)
				.append(", {data})");
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		sb.append("/")
				.append(index)
				.append("/_update/")
				.append(id);
		Request request = new Request("POST", sb.toString());
		sb.setLength(0);
		sb.append("{\"doc\":")
				.append(StringUtil.toJson(data))
				.append("}");
		request.setJsonEntity(sb.toString());
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public Object delete(String index, String id) {
		StringBuilder sb = new StringBuilder("CommonRepository.delete(")
				.append(index)
				.append(", ")
				.append(id)
				.append(')');
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		sb.append("/")
				.append(index)
				.append(DOC)
				.append(id);
		Request request = new Request("DELETE", sb.toString());
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}

	/**
	 * @author Artxe2
	 */
	public Object search(Object query) {
		return search(null, query);
	}

	/**
	 * @author Artxe2
	 */
	public Object search(String index, Object query) {
		StringBuilder sb = new StringBuilder("CommonRepository.search(")
				.append(index)
				.append(", ")
				.append(query)
				.append(')');
		FlowLogHolder.touch(sb.toString());
		sb.setLength(0);
		if (index != null) {
			sb.append("/")
					.append(index);
		}
		sb.append("/_search");
		Request request = new Request("GET", sb.toString());
		if (query != null) {
			if (query instanceof String) {
				request.setJsonEntity((String) query);
			} else {
				request.setJsonEntity(StringUtil.toJson(query));
			}
		}
		try (
			InputStream is = restClient.performRequest(request)
					.getEntity()
					.getContent();
		) {
			sb.setLength(0);
			int i;
			while ((i = is.read()) >= 0) {
				sb.append((char) i);
			}
			return StringUtil.fromJson(sb.toString(), Map.class);
		} catch (IOException e) {
			throw new DefinedException(ELASTIC_SEARCH_ERROR, e);
		}
	}
}
