package artxew.framework.layers.oauth.svc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.layers.oauth.dto.req.SignInReqDto;
import artxew.framework.layers.oauth.dto.res.QuerySessionResDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuthSvc extends BaseService {
    private static final String OAUTH_NAMESPACE = "framework.postgresql.oauth.";

    private final CommonDao commonDao;

    @Transactional(readOnly = true)
    public QuerySessionResDto signIn(SignInReqDto reqDto) {
        return commonDao.select(OAUTH_NAMESPACE + "signIn", reqDto);
    }
}