package org.example.springv4.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.springv4.user.User;

import java.util.Date;

public class JwtUtil {
    public static String create(User user) {
        String accessToken = JWT.create()
                .withSubject("바보") // 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) //만료시간 7주일
                .withClaim("id", user.getId()) // payload에 추가. 개인정보 넣지 않고 검증을 위한 id정도만(인조키 id번호)
                .withClaim("username", user.getUsername())  // 참고로 .withClaim()으로 추가하면 payload에 내용 계속 추가.
                .sign(Algorithm.HMAC512("metacoding")); //우리가 암호화 하고 우리가 복호화 해서 확인할 것이므로 대칭키 사용
        return accessToken;            //"metacoding"이 secret이다
    }

    //검증 코드
    public static User verify(String jwt){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("metacoding")).build().verify(jwt);
        int id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();

        return User.builder()
                .id(id)
                .username(username)
                .build();
    }

}
