package net.datasa.web5.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.entity.BoardEntity;
import net.datasa.web5.repository.BoardRepository;
import net.datasa.web5.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AjaxService {
	
	
	
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	
	/**
	 * 게시글의 추천수 증가
	 * @param num	추천할 글 번호
	 * @return 		증가된 추천수
	 */
	public int like(int num) {
		//전달된 글 번호로 게시글 정보 조회
		BoardEntity entity = boardRepository.findById(num)
                .orElseThrow(() -> new EntityNotFoundException("없는 번호"));
		
		//리턴받은 엔티티객체의 추천수를 수정
		int n = entity.getCnt() +1;
		entity.setCnt(n);
		
		//수정된 추천수를 리턴
		return n;
	}
	/**
	 * 아이디 존재여부 확인
	 * @param id 조회할 아이디
	 * @return	아이디가 존재하면 true, 없으면 false
	 */
	public boolean inDuplicate(String id) {
		// TODO Auto-generated method stub
		
		return memberRepository.existsById(id);
	}
	
}
