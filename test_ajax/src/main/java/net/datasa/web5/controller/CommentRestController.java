package net.datasa.web5.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.dto.CommentDTO;
import net.datasa.web5.dto.PersonDTO;
import net.datasa.web5.service.CommentService;

/**댓글관련 Ajax요청에 대한 처리
 * 
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("comment")
public class CommentRestController {
	
	private final CommentService commentService;
	
	@ResponseBody
	@PostMapping("input")
	public void input(CommentDTO dto) {
		//서비스로 전달하여 저장
		log.debug("전달된 객체 : {}", dto);
		
		commentService.input(dto);
	}
	
	@ResponseBody
	@GetMapping("list")
	public List<CommentDTO> list () {
		
		List<CommentDTO> list = commentService.getList();
	
		return list;
	}
	
	@PostMapping("delete")
	public void delete(@RequestParam("num") Integer num) {
	    commentService.delete(num);
	} 
	
	@PostMapping("update")
	public void update(CommentDTO dto) {
	    commentService.update(dto);
	}
	
	
}
