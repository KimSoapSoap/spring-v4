package org.example.springv4.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springv4.core.util.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final HttpSession session;
    private final UserService userService;

    @PostMapping("/api/user/profile")
    public String profile(@RequestParam("profile") MultipartFile profile){
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.프로필업로드(profile, sessionUser);

        return "redirect:/api/user/profile-form";
    }

    @GetMapping("/api/user/profile-form")
    public String profileForm(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        String profile = userService.프로필사진가져오기(sessionUser);
        request.setAttribute("profile", profile);
        return "user/profile-form";
    }

    // http://localhost:8080/user/samecheck?username=hello
    @GetMapping("/user/samecheck")
    public ResponseEntity<?> sameCheck(@RequestParam("username") String username) {
        boolean isSameUsername = userService.유저네임중복되었니(username);
        return ResponseEntity.ok(Resp.ok(isSameUsername, isSameUsername ? "중복되었어요" : "중복되지않았어요"));
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest.LoginDTO loginDTO, Errors errors) {
        String accessToken = userService.로그인(loginDTO);
        // RequestParam으로 받는 것이 아니므로 @RequestBody를 넣어준다. @RequestBody를 넣어준다. JSON을 받아서 자바 객체로 변환


        // 원래 ok에 body를 넣어주면 되는데 header를 넣으려면 body와 함께 적어줘야 한다. 그냥 문법이다.
        // headerName을 "Authorization" 으로 하고 "Bearer " + accessToken 하면  엑세스토큰이다 라는 것을 명시
        // Bearer 하고 한 칸 뛰우는 것이 핵심. 컨벤션이다.
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .body(Resp.ok(null));
    }

    @PostMapping("/join")
    public String join(@Valid UserRequest.JoinDTO joinDTO, Errors errors) {
        userService.회원가입(joinDTO);
        return "redirect:/login-form";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }
}
