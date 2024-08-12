package net.datasa.web3;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 정보 관련 처리를 하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {
	
	private final PersonRepository personRepository;

	/**
	 * 객체를 생성해서 DB에 저장
	 */
	public void test() {
		PersonEntity entity = new PersonEntity();
		entity.setId("abcde");
		entity.setName("김길동");
		entity.setAge(30);
		
		personRepository.save(entity);
	}

	/**
	 * 개인정보 전달받아 DB에 저장
	 * @param dto 저장할 정보를 담은 객체
	 */
	public void save(PersonDTO dto) {
		PersonEntity entity = new PersonEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setAge(dto.getAge());
		
		personRepository.save(entity);
	}

	/**
	 * 정보 조회
	 * @param id 조회할 아이디
	 * @return 조회 결과를 담은 객체
	 */
	public PersonDTO select(String id) {

		PersonEntity entity = personRepository.findById(id).orElse(null);
		if (entity == null) return null;
		
		PersonDTO dto = new PersonDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setAge(entity.getAge());
		
		return dto;
	}

	/**
	 * 정보 삭제
	 * @param id 삭제할 아이디
	 * @return 삭제 여부 true/false
	 */
	public boolean delete(String id) {
		boolean result = personRepository.existsById(id);
		
		if (result) {
			personRepository.deleteById(id);
		}
		return result;
	}
	
	/**
	 * 모든 정보 조회
	 * @return 모든 정보를 담은 객체의 리스트
	 */
	public List<PersonDTO> selectAll() {
		List<PersonEntity> entityList = personRepository.findAll();
		List<PersonDTO> dtoList = new ArrayList<>();
		
		for (PersonEntity entity : entityList) {
			PersonDTO dto = new PersonDTO();
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setAge(entity.getAge());
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 사용자 정보를 ID 기준으로 수정
	 * @param dto 수정할 정보
	 */
	public void update(PersonDTO dto) {
		//DB의 정보를 조회
		PersonEntity entity = personRepository.findById(dto.getId())
				.orElseThrow(() -> new EntityNotFoundException("없는 ID"));
		//dto의 수정할 정보를 entity에 세팅
		entity.setName(dto.getName());
		entity.setAge(dto.getAge());
		//entity저장
		personRepository.save(entity);
	}
	
	
	
}
