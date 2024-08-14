package net.datasa.web5.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@ResponseBody
	@PostMapping("ajaxtest2")
	public void ajaxtest2(@RequestParam("str") String s) {
		log.debug("전달된 값 : {}", s);
	}
	
	//http://localhost:7778/ajaxtest3 ->단순하게 리턴하려는 객체를 전송
	@ResponseBody
	@GetMapping("ajaxtest3")
	public String ajaxtest3() {
		String s = "문자열";
		log.debug("서버에서 보낸 값 : {}", s);
		return s;
	}
	
	@ResponseBody
	@PostMapping("ajaxtest4")
	public int ajaxtest4(@RequestParam("num1") int a,
			@RequestParam("num2") int b) {
		 int c = a + b;
		 log.debug("합계 : {}", c);
		return c;
		
	}
	
	//숫자가 아닌값이 왔을때
	@ResponseBody
	@PostMapping("ajaxtest5")
	//계산결과(int타입), 에러결과(String타입)
	public ResponseEntity<?> ajaxtest5(@RequestParam("num1") String a,
			@RequestParam("num2") String b) {
		
		try {
		int n1 = Integer.parseInt(a);
		int n2 = Integer.parseInt(b);
		int n3 = n1 / n2;
		
		//정상처리 200번
		return ResponseEntity.ok(n3);
		}
		//메서드가 끝날때 return
		//숫자가 아닌값 입력
		catch(NumberFormatException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("정수가 아닙니다.");
		}
		//0으로 나눌때	
		catch(ArithmeticException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("0으로 나눌수 없습니다.");
		}
		//그외
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("계산 실패했습니다.");
		}
	}
	
	@GetMapping("calc")
	public String calc () {
		return "calc";
	}
	

	@ResponseBody
	@PostMapping("ajaxtest6")
	public int ajaxtest6(
				@RequestParam("num1") int n1,
				@RequestParam("num2") int n2,
				@RequestParam("op") String op) {
			
			int n3 = 0;
			
			switch (op) {
	          case "+":
	        	   n3 = n1 + n2;
	            break;

	          case "-":
	        	   n3 = n1 - n2;
	            break;

	          case "*":
	        	   n3 = n1 * n2;
	            break;

	          case "/":
	        	  n3 = n1 / n2;
		        break;
			}
			return n3;
		}
	
	
	@ResponseBody
	@PostMapping("ajaxtest7")
	public ResponseEntity<?> ajaxtest7(
				@RequestParam("num1") String a,
				@RequestParam("num2") String b,
				@RequestParam("op") String op) {
			
		try {
					int n1 = Integer.parseInt(a);
					int n2 = Integer.parseInt(b);
					int n3 = 0;
					
					switch (op) {
			          case "+":
			        	   n3 = n1 + n2;
			            break;
		
			          case "-":
			        	   n3 = n1 - n2;
			            break;
		
			          case "*":
			        	   n3 = n1 * n2;
			            break;
		
			          case "/":
			        	  n3 = n1 / n2;
				        break;
					}
					return ResponseEntity.ok(n3);
			
		    }
			catch(NumberFormatException e) {
		    		e.printStackTrace();
		    		return ResponseEntity.badRequest().body("정수가 아닙니다.");
		    }
			catch(ArithmeticException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("0으로 나눌수 없습니다.");
			}
			catch(Exception e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body("계산 실패했습니다.");
			}
		
	}	

}
