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
public class JwtAuthorizationFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //매개변수에 FilterChain은 다음 필터 존재시 다음 필터로 보내는 것이다.

        //다운캐스팅을 해서 자녀가 가진 더 많은 메서드를 사용.  HttpServeltRequest는 ServletRequet의 자녀다.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String accessToken = req.getHeader("Authorization");

        //isEmpty() 는 value.length ==0
        //isBlacnk() 는 isEmpty()이고 white space(공백)인 것
        if(accessToken == null || accessToken.isBlank()) {

            //토큰이 없을 때 검증을 해주는데 아래에 catch 부분을 그대로 가져 왔다.
            //에러메시지는 예외처리가 아니라서 e가 없지만 무슨 에러인지 아니까 에러메시지를 넣어준다.
            // "토큰이 없어요." 에러메시지를 넣어주고 끝에 return; 으로 코드를 끝낸다.
            resp.setHeader("Content-Type", "application/json; charset=utf-8");
            PrintWriter out = resp.getWriter();
            Resp fail = Resp.fail(401, "토큰이 없어요." );
            String responseBody = new ObjectMapper().writeValueAsString(fail);
            out.println(responseBody);
            out.flush();
            return; // 리턴을 붙여야 검증이 안 될 때 멈춤. 아니면 아래로 내려간다.
        }


        // 서명이 위조되거나 만료댔을 때 터짐. 필터이므로 여기서는 try/catch로 예외시 직접 응답
        // try - catch로 응답하는 우리가 이 위치에서 throw할 수 없기 때문에
        // 직접 처리하는 이유도 있지만 에러가 났을 때 항상 동일한 형태로 응답해주기 위함도 있다.
        try {
            //토큰 검증
            User sessionUser = JwtUtil.verify(accessToken);

            //검증하고 나서 문제가 없으면 session에 넣기 위해 request에서 session을 불러 온다.
            HttpSession session = req.getSession();

            //requet를 통해 불러온 session에 sessionUser를 넣어준다. 이렇게 전달하는 것이다.
            session.setAttribute("sessionUser", sessionUser);

            //필터를 다 썼으니 그 다음 필터로 이동. 다음 필터가 없으면 DS(Dispatcher Servlet)로 이동.
            chain.doFilter(req, resp);

        }catch(Exception e) {
            //setContentType을 제공하니까 이 메서드를 써도 되고 setHeader 사용해도 된다.
            //resp.setContentType("application/json; charset=utf-8");
            resp.setHeader("Content-Type", "application/json; charset=utf-8");
            PrintWriter out = resp.getWriter();
            //인증 안 됐으면 401, 인가가 안 됐으면 403. 여기선 토큰 자체가 검증이 안 돼서 그냥 401
            Resp fail = Resp.fail(401,e.getMessage() );

            String responseBody = new ObjectMapper().writeValueAsString(fail);

            //이대로 fail을 전달하면 자바 객체가 전달되므로 Json으로 파싱해서 전달해야 한다.
            //이때 ObjectMapper를 사용한다. -> writeValueAsString()
            //out.println(fail);
            out.println(responseBody);
            out.flush();
        }

        //만약 토큰 검증이 실패하면 로그인 화면으로 보내주면 된다.
        // 응답 메시지가 중요한 것이 프론트가 응답 메시지를 읽고 토큰 만료, 검증 실패 등이 나온다면 로그인화면으로 보내줄 수 있기 때문
        // 그래서 일정한 메시지가 중요한 것이다.






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
