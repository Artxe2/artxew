package artxew.framework.environment.exception;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.yaml.snakeyaml.Yaml;

import artxew.framework.decedent.dto.ServerResponseDto;

public final class DefinedException extends RuntimeException {
    private static Map<String, HashMap<String, Object>> exceptions;

    private final ServerResponseDto<Object> body;
    private final String name;

    static {
        try (
            InputStream inputStream = new BufferedInputStream(
                new FileInputStream(
                    "src/main/resources/exception/defined-exception.yml"
                )
            );
        ) {
            exceptions = new Yaml().loadAs(inputStream, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefinedException(String key) {
        super(key);
        body = new ServerResponseDto<>(null);
        name = key;
    }

    public DefinedException(String key, Throwable e) {
        super(key, e);
        body = new ServerResponseDto<>(null);
        name = key;
    }

    public DefinedException(ServerResponseDto<Object> data, String key, Throwable e) {
        super(key, e);
        if (data == null) {
            body = new ServerResponseDto<>(null);
        } else {
            body = data;
        }
        name = key;
    }

    public String name() {
        return name;
    }

    public static DefinedException badRequest(BindingResult br) {
        Map<String, Object> errors = parseError(br);
        ServerResponseDto<Object> resDto= new ServerResponseDto<>(errors);

        return new DefinedException(resDto, "bad-request", null);
    }

    public static DefinedException unknownError(
        ServerResponseDto<Object> data
        , Throwable e
    ) {
        return new DefinedException(data, "unknown-error", e);
    }

    public ResponseEntity<ServerResponseDto<Object>> parseResponse() {
        HashMap<String, Object> e = exceptions.get(name);
        if (e == null) {
            return unknownError(body, null).parseResponse();
        }
        body.setMessage((String) e.get("message"));
        body.setError(name);
        return ResponseEntity.status((int) e.get("code")).body(body);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseError(BindingResult br) {
        Map<String, Object> errors = new HashMap<>();
        Map<String, Object> t;
        List<Map<String, Object>> list;

        for (FieldError fe : br.getFieldErrors()) {
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
                            key.substring(0, key.indexOf('[')), k -> new ArrayList<>()
                        );
                        idx = Integer.parseInt(key.substring(
                            key.indexOf('[') + 1
                            , key.length() - 1
                        ));
                        while (list.size() <= idx) {
                            list.add(new HashMap<>());
                        }
                        t = list.get(idx);
                        t.put("INDEX", idx);
                    } else {
                        t = (Map<String, Object>) t.computeIfAbsent(
                            key, k -> new HashMap<>()
                        );
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