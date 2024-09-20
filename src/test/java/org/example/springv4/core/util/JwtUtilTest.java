package org.example.springv4.core.util;

import org.example.springv4.user.User;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

    @Test
    public void create_test() throws Exception {
    //given
    User user = User.builder().id(1).username("ssar").build();


    //when
    String accessToken = JwtUtil.create(user);


    //then
    System.out.println(accessToken);

    }

    //위에서 create_test()에서 나온 token을 사용
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLrsJTrs7QiLCJpZCI6MSwiZXhwIjoxNzI3NDAyMTI1LCJ1c2VybmFtZSI6InNzYXIifQ.9ehBuwDAT93hG4_DisoMstKkeWE12IPPu1j7nzdLPGkuFcMjg5QxUQ72d3nFeM6f9-BW61owfR2TQ_U6VaX1Zg
    @Test
    public void verify_test() throws Exception {
    //given
    String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLrsJTrs7QiLCJpZCI6MSwiZXhwIjoxNzI3NDAyMTI1LCJ1c2VybmFtZSI6InNzYXIifQ.9ehBuwDAT93hG4_DisoMstKkeWE12IPPu1j7nzdLPGkuFcMjg5QxUQ72d3nFeM6f9-BW61owfR2TQ_U6VaX1Zg";

    //when
        User user = JwtUtil.verify(accessToken);

    //then
        System.out.println(user.getId());
        System.out.println(user.getUsername());


    }



}
