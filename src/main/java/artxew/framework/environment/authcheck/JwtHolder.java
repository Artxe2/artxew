package artxew.framework.environment.authcheck;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeanUtils;

import artxew.framework.environment.exception.DefinedException;
import artxew.framework.layers.oauth.dto.res.QuerySessionResDto;
import artxew.framework.util.SessionMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JwtHolder {

    private static final String REFRESH_TOKEN_NAME = "refresh-token";

    private static final String ACCESS_TOKEN_NAME = "access-token";

    private static final byte[] SECRET_KEY = System.getProperty(
        "environment.password"
    ).getBytes();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<Thread, Claims> jwtMap = new HashMap<>();

    private JwtHolder() {
        throw new IllegalStateException("Utility class");
    }

    public static void signIn(QuerySessionResDto info) {
        String refreshToken = issuanceRefreshToken(info);
        String accessToken = issuanceAccessToken(refreshToken);

        SessionMap.response().setHeader(
            REFRESH_TOKEN_NAME
            , refreshToken
        );
        SessionMap.response().setHeader(
            ACCESS_TOKEN_NAME
            , accessToken
        );
    }

    public static Claims hold(
        HttpServletRequest request
        , HttpServletResponse response
    ) {
        String accessToken;
        Claims jwt;

        try {
            accessToken = request.getHeader(ACCESS_TOKEN_NAME);
            if (accessToken != null) {
                jwt = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(accessToken)
                        .getBody();
            } else {
                String refreshToken = request.getHeader(REFRESH_TOKEN_NAME);

                accessToken = issuanceAccessToken(refreshToken);
                response.setHeader(
                    ACCESS_TOKEN_NAME
                    , accessToken
                );
                jwt = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(accessToken)
                        .getBody();
            }
        } catch (ExpiredJwtException e) {
            throw new DefinedException("session-expired", e);
        } catch (DefinedException e) {
            throw e;
        } catch (Exception e) {
            throw new DefinedException("unauthorized", e);
        }
        jwtMap.put(Thread.currentThread(), jwt);
        return jwt;
    }

    public static String accessToken() {
        String accessToken = SessionMap.response().getHeader(ACCESS_TOKEN_NAME);

        if (accessToken == null) {
            accessToken = SessionMap.request().getHeader(ACCESS_TOKEN_NAME);
        }
        return accessToken;
    }

    public static Object get(String key) {
        return jwtMap.get(Thread.currentThread()).get(key);
    }

    public static void release() {
        jwtMap.remove(Thread.currentThread());
    }

    private static String issuanceRefreshToken(QuerySessionResDto info) {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        @SuppressWarnings("unchecked")
        Map<String, Object> claims = objectMapper.convertValue(
            info
            , Map.class
        );

        Date ext = new Date();

        ext.setTime(ext.getTime() + 1000 * 60 * 60 * 24 * 120);

        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static String issuanceAccessToken(String refreshToken) {
        Claims jws = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(refreshToken)
                .getBody();
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        @SuppressWarnings("unchecked")
        Map<String, Object> claims = objectMapper.convertValue(
            jws
            , Map.class
        );

        BeanUtils.copyProperties(claims, jws);

        Date ext = new Date();

        ext.setTime(ext.getTime() + 1000 * 60 * 60);
        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}