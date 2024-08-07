package net.datasa.web5;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AjaxController {
	
	//ajax1.html로 포워딩. 연습 페이지 첫번째. 
	@GetMapping("ajax1")
	public String ajax () {
		return "ajax1";
	}
	
	//ajax요청을 처리하는 메소드
	//리턴값이 매번 달라짐
	@ResponseBody
	@GetMapping("ajaxtest1")
	public void ajaxtest1() {
		log.debug("AjaxController의 ajaxtest1() 메소드 실행됨");
		throw new RuntimeException("오류");
	}
	

}
