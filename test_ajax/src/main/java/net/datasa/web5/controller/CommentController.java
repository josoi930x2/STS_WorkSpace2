package net.datasa.web5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("comment")
public class CommentController {
	
	//댓글달기 테스트 페이지로 이동
	@GetMapping("main")
	public String main() {
		return "comment";
	}

}
