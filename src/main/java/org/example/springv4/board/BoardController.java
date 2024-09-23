package org.example.springv4.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springv4.core.util.Resp;
import org.example.springv4.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardService boardService;

    //list 리팩토링 할 때도 pagedTO를 ResponseEntity에 데이터를 넣어서 전달해주면 된다.
    //나머지 Rest로 바꾸는 리팩토링을 해본다.
    //응답은 DTO로 해준다.

    //화면으로 이동하는 것은 필요없으므로 리팩토링 할 때 지우고
    //setAttribute로 데이터를 저장해서 전달해준 것은 데이터를 전달하도록 바꿔주면 된다.

    //이제 Rest API 서버이므로 삭제하는 것은 주소에 끝에 /delete 떼고 DeleteMapping으로 전환.
    //update는 주소 끝에 /update 떼고 PutMapping으로 바꾸면 된다.

    //그리고 insert와 update는 잘 넣었는지, 잘 수정했는지 확인해야 하므로
    //응답 데이터로 반드시 들어온 데이터를 DTO로 전달 해준다.(pk값 반드시 필요하다)

    //Rest-Api에서는 log-out이라는 것이 없다.
    //인증을 JWT로 검증을 하기 때문에 이는 프론트쪽에서 클라이언트에서 JWT를 삭제하는 것이므로.

    //그리고 Rest-API 서버기 때문에 뷰에서 발생하는 예외처리 ExceptionXXX들은 다 필요가 없다.
    //core에 error 패키지에 ex에 ExceptionXXX는 다 지우고 ExceptionApiXXX만 남기면 된다.
    //그럼 GlobalExceptionHandler도 Api만 남기고 지우고 500에러는
    //GlobalExceptionalHandler 혹은 GlobalApiExceptional 둘 중 하나에만 배치해뒀는데(둘 다 하면 충돌 때문)
    //GlobalApiExceptional로 옮기고 GlobalExceptionalHandler는 삭제하든가 하자


    // localhost:8080?title=제목
    @GetMapping("/")
    public String list(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            HttpServletRequest request) {

        BoardResponse.PageDTO pageDTO = boardService.게시글목록보기(title, page);
        request.setAttribute("model", pageDTO);
        return "board/list";
    }


    @PostMapping("/api/board/{id}/delete")
    public String removeBoard(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글삭제하기(id, sessionUser);
        return "redirect:/";
    }


    @GetMapping("/api/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }


    //Rest API 전환 후 주소에서 끝에 /save 는 버린다.
    @PostMapping("/api/board")
    public ResponseEntity<?> save(@Valid @RequestBody BoardRequest.SaveDTO saveDTO, Errors errors) {

        //필터에서 사용한 session정보가 request 덕분에 그대로 들어 있다.
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DTO boardPs = boardService.게시글쓰기(saveDTO, sessionUser);

        return ResponseEntity.ok(Resp.ok(boardPs));
    }

    @GetMapping("/api/board/{id}/update-form")
    public String updateForm(@PathVariable("id") int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DTO model = boardService.게시글수정화면(id, sessionUser);
        request.setAttribute("model", model);
        return "board/update-form";
    }

    @PostMapping("/api/board/{id}/update")
    public String update(@PathVariable("id") int id, @Valid BoardRequest.UpdateDTO updateDTO, Errors errors) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.게시글수정(id, updateDTO, sessionUser);
        return "redirect:/board/" + id;
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable("id") Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        BoardResponse.DetailDTO model = boardService.게시글상세보기(sessionUser, id);
        request.setAttribute("model", model);

        return "board/detail";
    }
}
