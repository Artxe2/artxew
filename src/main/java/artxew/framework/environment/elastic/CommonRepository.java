package artxew.framework.environment.elastic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import artxew.framework.environment.exception.DefinedException;
import artxew.framework.util.StringUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CommonRepository {

    private static final String ELASTIC_SEARCH_ERROR = "elastic-search-error";

    private final RestClient restClient;

    public Map<String, Object> select(String index, String id) {
        return select(index, id, Map.class);
    }

    public <T> T select(String index, String id, Class<T> valueType) {
        StringBuilder sb = new StringBuilder("/");

        sb.append(index);
        sb.append("/_doc/");
        sb.append(id);

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

    public Object insert(String index, Object data) {
        StringBuilder sb = new StringBuilder("/");

        sb.append(index);
        sb.append("/_doc");

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

    public Object insert(String index, String id, Object data) {
        StringBuilder sb = new StringBuilder("/");

        sb.append(index);
        sb.append("/_create/");
        sb.append(id);

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

    public Object merge(String index, String id, Object data) {
        StringBuilder sb = new StringBuilder("/");

        sb.append(index);
        sb.append("/_doc/");
        sb.append(id);

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

    public Object update(String index, String id, Object data) {
        StringBuilder sb = new StringBuilder("/");

        sb.append(index);
        sb.append("/_update/");
        sb.append(id);

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

    public Object delete(String index, String id) {
        StringBuilder sb = new StringBuilder("/");

        sb.append(index);
        sb.append("/_doc/");
        sb.append(id);

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

    public Object bulk(Object ...data) {
        StringBuilder sb = new StringBuilder();

        for (Object o : data) {
            sb.append(StringUtil.toJson(o));
        }

        Request request = new Request("POST", "/_bulk");
        
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

    public Object search(Object query) {
        return search(null, query);
    }

    public Object search(String index, Object query) {
        StringBuilder sb = new StringBuilder();

        if (index != null) {
            sb.append("/");
            sb.append(index);
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
