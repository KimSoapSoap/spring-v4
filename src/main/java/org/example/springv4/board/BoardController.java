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
