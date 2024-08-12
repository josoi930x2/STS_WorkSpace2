package net.datasa.web3;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PersonController {

//	@Autowired
//	PersonService personService;
	
	//명시적인 생성자를 통한 초기화.
	//@Autowired보다 권장. 테스트 용이. 주입되지 않은 경우 컴파일 타임에 오류.
	private final PersonService personService;
	
	/**
	 * 저장 테스트 (서비스에서 객체 생성해서 바로 저장)
	 * @return 메인화면으로 이동
	 */
	@GetMapping("test")
	public String test() {
		personService.test();
		return "redirect:/";
	}
	
	/**
	 * 사용자 입력 폼으로 이동
	 * @return 입력 HTML 파일 경로
	 */
	@GetMapping("save")
	public String save() {
		return "inputForm";
	}
	
	/**
	 * 입력폼의 데이터 받아서 서비스로 전달하여 저장
	 * @param dto 사용자가 입력한 값이 저장된 객체
	 * @return
	 */
	@PostMapping("save")
	public String save(@ModelAttribute PersonDTO dto) {
		log.debug("전달된 값: {} ", dto);
		
		personService.save(dto);
		
		return "redirect:/";
	}
	
	/**
	 * 검색 폼으로 이동
	 * @return 입력 HTML 파일 경로
	 */
	@GetMapping("select")
	public String select() {
		return "selectForm";
	}
	
	/**
	 * 검색폼에서 입력한 아이디를 전달받아 회원정보 조회
	 * @param id 조회할 아이디
	 * @param model
	 * @return 정보 출력할 HTML 파일 경로
	 */
	@PostMapping("select")
	public String select( @RequestParam("id") String id , Model model) {
		
		PersonDTO dto = personService.select(id);
		
		model.addAttribute("id", id);
		model.addAttribute("person", dto);
		
		return "select";
	}
	
	/**
	 * 삭제 폼으로 이동
	 * @return 입력 HTML 파일 경로
	 */
	@GetMapping("delete")
	public String delete() {
		return "deleteForm";
	}
	
	/**
	 * 삭제하고 결과 출력 페이지로이동
	 * @param id 	삭제할 아이디
	 * @param model	콘트롤러에서 뷰로 데이터를 이동할 객체
	 * @return		출력할 HTML파일 경로
	 */
	@PostMapping("delete")
	public String delete( @RequestParam("id") String id , Model model) {
		
		boolean result = personService.delete(id);
		
		//삭제 여부를 나타내는 result와 삭제 시도한 id를 모델에 저장
		//HTML 페이지로 포워딩해서 안내문구 출력
		// 1. XX는 없는 아이디입니다.
		// 2. XX를 삭제했습니다.
        model.addAttribute("id", id);
        model.addAttribute("result", result);
		
		return "delete";
	}
	
	/**
	 * 모든 회원정보 보기
	 * @return
	 */
	@GetMapping("selectAll")
	public String selectAll(Model model) {
        List<PersonDTO> personlist = personService.selectAll();
        model.addAttribute("personlist", personlist);
		return "selectAll";
	}
	
	/**
	 * 아이디를 전달받아 회원정보 조회
	 * @param id 조회할 아이디
	 * @param model
	 * @return 개인정보 출력할 HTML 파일 경로
	 */
	@GetMapping("view")
	public String view( @RequestParam("id") String id , Model model) {
		
		PersonDTO dto = personService.select(id);
		
		model.addAttribute("id", id);
		model.addAttribute("person", dto);
		
		return "select";
	}
	
	/**
	 * http://localhost:888/info/abc 형식으로 요청받아 회원정보 조회
	 * @param id 조회할 아이디
	 * @param model
	 * @return
	 */
	@GetMapping("info" + "/{id}")
	public String info(
			@PathVariable("id")  String id,
			Model model) {
			
		PersonDTO dto = personService.select(id);
		
		model.addAttribute("id", id);
		model.addAttribute("person", dto);
		
		return "select";
	}
	
	/**
	 * 아이디를 전달받아 회원정보 삭제
	 * @param id 삭제할 아이디
	 * @return 회원목록 보기 페이지로 이동
	 */
    @GetMapping("deleteUser")
    public String deleteUser(@RequestParam("id") String id) {
        personService.delete(id);
        return "redirect:selectAll";
    }
    
    /**
     * 수정 페이지로 이동
     * @param id 수정할 아이디
     * @param model
     * @return 수정폼 HTML 파일 경로
     */
    @GetMapping("update")
    public String update(@RequestParam("id") String id, Model model) {
    	PersonDTO dto = personService.select(id);
    	model.addAttribute("person", dto);
    	return "updateForm";
    }
        
    /**
     * 사용자가 폼에 입력한 값을 DB에 저장
     * @param dto 사용자가 수정한 내용을 저장한 객체
     * @return 개인정보 보기 페이지로 이동
     */
    @PostMapping("update")
    public String update(@ModelAttribute PersonDTO dto) {
    	log.debug("전달된 값:{}", dto);
    	personService.update(dto);
    	
    	return "redirect:view?id=" + dto.getId();
    }
    
}





