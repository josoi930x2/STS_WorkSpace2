package net.datasa.web5;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
public class Ajax2Controller {
	
	
	@GetMapping("ajax2")
	public String ajax2 () {
		return "ajax2";
	}
	
	@ResponseBody
	@PostMapping("input")
	//포워딩하는게 아니기떄문에 model 사용x
	public void input (PersonDTO dto) {
		log.debug("전달된 객체 : {}", dto);
		
	}
	
	@ResponseBody
	@GetMapping("getObject")
	public PersonDTO getObject () {
		
		PersonDTO dto = new PersonDTO("김철수", 22, "010-1111-2222");
		
		return dto;
	}
	
	@ResponseBody
	@GetMapping("getList")
	public List<PersonDTO> getList () {
		
		List<PersonDTO> list = new ArrayList<>();
		list.add(new PersonDTO("김철수", 11, "010-1111-2222"));
		list.add(new PersonDTO("홍길동", 22, "010-1111-2222"));
		list.add(new PersonDTO("박철수", 33, "010-1111-2222"));
		
		return list;
	}
	
	@ResponseBody
	@PostMapping("sendArray")
	public void sendArray (String[] ar) {
		
		 for(String s : ar) {
			 log.debug("배열요소 : {}", s);
		 }
	}
	
	//여러개의 형식을 가지고 있는 객체를 전달받을 때
	@ResponseBody
	@PostMapping("sendObjectArray")
	public void sendObjectArray (@RequestParam("ar") String ar) throws JsonMappingException, JsonProcessingException {
		
		log.debug("전달된 JSON 문자열 : {}", ar);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ArrayList<PersonDTO> list =
		objectMapper.readValue(ar, new TypeReference<ArrayList<PersonDTO>>() {});
		
	}
	
	

}
