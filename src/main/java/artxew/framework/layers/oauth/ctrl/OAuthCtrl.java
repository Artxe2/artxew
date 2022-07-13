package artxew.framework.layers.oauth.ctrl;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.authcheck.JwtHolder;
import artxew.framework.layers.oauth.dto.req.SignInReqDto;
import artxew.framework.layers.oauth.dto.res.QuerySessionResDto;
import artxew.framework.layers.oauth.svc.OAuthSvc;
import artxew.framework.util.SessionMap;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Api(tags = { "OAuth" })
@RequestMapping("api/oauth")
@RequiredArgsConstructor
@RestController
public class OAuthCtrl extends BaseController {

    @Value("${artxew.auth-check.auth-with-jwt:false}")
    private boolean authWithJwt;

    private final OAuthSvc oAuthSvc;

    @PostMapping("sign-in")
    public ServerResponseDto<QuerySessionResDto> signIn(
        @Valid @RequestBody SignInReqDto reqDto
    ) {
        QuerySessionResDto resDto = oAuthSvc.signIn(reqDto);

        SessionMap.signIn(resDto);
        return processResult(resDto);
    }

    @GetMapping("session")
    public ServerResponseDto<QuerySessionResDto> querySession() {
        QuerySessionResDto resDto = new QuerySessionResDto();

        resDto.setUsrId(SessionMap.usrId());
        resDto.setAuth(SessionMap.auth());
        return processResult(resDto);
    }

    @GetMapping("access-token")
    public ServerResponseDto<String> queryAccessToken() {
        String accessToken = JwtHolder.accessToken();
        
        return processResult(accessToken);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("session")
    public void removeSession() {
        SessionMap.signOut();
    }
}