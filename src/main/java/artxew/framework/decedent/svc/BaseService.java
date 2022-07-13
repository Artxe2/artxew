package artxew.framework.decedent.svc;

import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.exception.DefinedException;

public class BaseService {

    public void exception(Object data, String key) {
        throw new DefinedException(new ServerResponseDto<>(data), key, null);
    }

    public void exception(String key) {
        throw new DefinedException(null, key, null);
    }

    public void exception(Object data, String key, Throwable e) {
        throw new DefinedException(new ServerResponseDto<>(data), key, e);
    }

    public void exception(String key, Throwable e) {
        throw new DefinedException(null, key, e);
    }
}