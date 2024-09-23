package org.example.springv4.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.springv4.core.util.JwtUtil;
import org.example.springv4.core.util.Resp;
import org.example.springv4.user.User;

import java.io.IOException;
import java.io.PrintWriter;

// 인가 필터 이다. 토큰이 있는지 없는지. 인증이 아니다. 인증은 로그인 할 때 하는 것
// 책임 : 인가 (인증된 사람만 들어올 수 있게 해주는 것)
// Filter 인터페이스를 구현하는데 자바17이상은 jakarta 패키지
// 어떻게 동작하는지는 FilterConfig 클래스를 만들어서 (@Configuration) 여기서 정의해준다.
public class AttackFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //매개변수에 FilterChain은 다음 필터 존재시 다음 필터로 보내는 것이다.

        //다운캐스팅을 해서 자녀가 가진 더 많은 메서드를 사용.  HttpServeltRequest는 ServletRequet의 자녀다.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ip = req.getRemoteAddr();
        //알고리즘.
        //여기에 일정 시간동안 몇 회를 썼는지 그런 것에 제한을 거는 그런 로직 등.
        HttpSession session = req.getSession();
        session.setAttribute("count", 6);






/*

        System.out.println("JwtAuthorizationFilter필터가 동작했습니다.");


        resp.setHeader("Content-Type", "text/plain");


        PrintWriter out = response.getWriter(); // 쓰기 버퍼 -> request는 톰캣이 제공해주는 읽기 버퍼, response는 톰캣이 제공해주는 쓰기 버퍼다.


        // 메시지 바디로 응답해주는 것이다. 메시지 바디기 때문에 컨텐트 타입이 필요한데 설정을 안 해주면 text/html이다.
        // Content-Type을 설정 안 해줬기 때문에 text/html로 나가고 html태그가 해석이 돼서 태그 적용이 되는 것이다.
        // 따라서 페이지에서 F12 눌러서 개발자 모드에서 네트워크 -> 접속 주소 누르고 응답 헤더를 봐도 content-type은 보이지 않지만 text/html 이다.
        // 메시지 바디의 Content-Type을 지정해주려면 쓰기버퍼 이전에 설정해줘야 한다.
        // ServletRequest와 ServletResponse는 메서드가 많지 않으므로 자녀 클래스인 HttpServletRequest 혹은 HttpServletResponse로 다운캐스팅 해서
        // setHeader메서드로 헤더에서 Conent-Type을 지정해준다. 이때 text/plain으로 바꿔서 테스트 해보면
        // text/html이 아니라 text/plain이므로 태그 해석이 안 돼서 태그까지 그대로 출력되고 헤더에서 컨텐트 타입을 명시해줬기 때문에 F12의 응답헤더에서 text/plain 확인가능
        // 이때 인코딩은 자동으로 charset=UTF-8로 나온다. 요즘은 이게 표준이다.
        // 참고로 인코딩은 타입옆에 ; 세미콜론으로 이어서 지정해준다. resp.setHeader("Content-Type", "text/plain; charset=ASCII");
        // 이렇게 해보면 UTF-8이 아니라서 한글이 깨지는 것도 확인할 수 있다.

        out.println("<h1>좋아요</h1>");
        // out.flush(); 자동 flush 되는데 버퍼는 flush 돼야 나가는 것을 알고 있어야 된다.
        //PrintWriter는 자동으로 flush 해주고 OutputStream이었나? 그건 수동으로 해줘야 했던 것 같다.
*/

    }

}
