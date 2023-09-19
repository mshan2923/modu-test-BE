package com.example.modutest.util;

import com.example.modutest.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {
    //#1 JWT데이터 생성
    // Header KEY 값
    public static final String AUTHO_Refresh_HEADER = "Autho_Refresh";
    public static final String AUTHO_Access_HEADER = "Autho_Access";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long AccessTOKEN_TIME = 10 * 1000L;//30 * 60 * 1000L; // 30분
    private final long RefreshToken_Time = (60 * 60 * 1000L) * 24 * 3;//3일

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //#2 JWT 생성
    // 토큰 생성
    public String createAccessToken(String username, UserRoleEnum role) {
        Date date = new Date();


        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + AccessTOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    public String createRefreshToken(String username, UserRoleEnum role) {
        Date date = new Date();


        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + RefreshToken_Time)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    public String createAccessToken(String username, Object role) {
        Date date = new Date();

        logger.info("--->" + new Date(date.getTime() + AccessTOKEN_TIME));
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + AccessTOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }


    //#3 JWT 저장
    // JWT Cookie 에 저장
    public void addJwtToRefreshCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHO_Refresh_HEADER, token); // Name-Value
            cookie.setPath("/");

            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    public void addJwtToAccessCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHO_Access_HEADER, token); // Name-Value
            cookie.setPath("/");

            logger.info("Create Access : " + cookie.getName() + " / " + cookie.getValue());


            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }
    //위에 생성과 추가 합침
    public void addJwtToCookies(String username, UserRoleEnum role, HttpServletResponse res)
    {
        addJwtToRefreshCookie(createAccessToken(username, role), res);
        addJwtToAccessCookie(createRefreshToken(username, role), res);
    }


    //#4 Cookie에 들어있던 JWT토큰을 Substrin, Bareer를 뗴어내야함
    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            System.out.println(tokenValue.substring(7));
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    //#5 JWT 검증
// 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다. : " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
    //Refresh Token이 유효하면 AccessToken 재발급
    public boolean valideteRefresh(String RefreshPureToken , HttpServletResponse res )
    {
        try
        {
            Date date = new Date();
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(RefreshPureToken);

            if (!claims.getBody().getExpiration().before(date))
            {
                Jws<Claims> accessClaims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(RefreshPureToken);

                addJwtToAccessCookie(createAccessToken(accessClaims.getBody().getSubject(), accessClaims.getBody().get(AUTHORIZATION_KEY)), res);
                logger.info(accessClaims.getBody().getSubject() + " / " + accessClaims.getBody().get(AUTHORIZATION_KEY));
            }
            return true;
        }catch (Exception e)
        {
            logger.error("JWT Token Error : " + e.getMessage());
            return false;
        }
    }

    //#6 JWT에서 사용자 정보 가져오기
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }



    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHO_Refresh_HEADER);

        if (!StringUtils.hasText(bearerToken))
            return null;

        if (bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}