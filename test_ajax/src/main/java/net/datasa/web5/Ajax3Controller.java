package net.datasa.web5;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class Ajax3Controller {
	
	 private final AjaxService ajaxService;
	
	//like.html로 포워딩, 추천기능 테스트
	@GetMapping("like")
	public String like() {
		return "like";
	}
	
	//전달된 글번호의 추천 처리
	@ResponseBody
	@PostMapping("like")
	public int like(@RequestParam("num") Integer num) {
		int cnt = ajaxService.like(num);
		return cnt;
	}
	
	@GetMapping("idDuplicate")
	public String idDuplicate() {
		return "idDuplicate";
	}
	
	@ResponseBody
	@PostMapping("inDuplicate")
	public boolean inDuplicate(@RequestParam("id") String id) {
		boolean res  = ajaxService.inDuplicate(id);
		
		return res;
	}
	

}
