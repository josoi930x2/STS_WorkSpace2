package net.datasa.web5.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.web5.dto.CommentDTO;
import net.datasa.web5.entity.CommentEntity;
import net.datasa.web5.repository.CommentRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	

	public void input(CommentDTO dto) {
		// TODO Auto-generated method stub
		CommentEntity entity = new CommentEntity();
		
		entity.setName(dto.getName());
		entity.setComment(dto.getComment());
	
		commentRepository.save(entity);
	}


	public List<CommentDTO> getList() {
		// TODO Auto-generated method stub
		List<CommentEntity> entityList = commentRepository.findAll();
		List<CommentDTO> dtoList = new ArrayList<>();
		
		for(CommentEntity entity : entityList) { 
			CommentDTO dto = new CommentDTO();
			dto.setNum(entity.getNum());
			dto.setName(entity.getName());
			dto.setComment(entity.getComment());
			
			dtoList.add(dto);
		}
		return dtoList;

	}


	public void delete(Integer num) {
		// TODO Auto-generated method stub
		
		CommentEntity commentEntity = commentRepository.findById(num)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 없습니다."));

		commentRepository.delete(commentEntity);
		
	}


	public void update(CommentDTO dto) {
	    CommentEntity commentEntity = commentRepository.findById(dto.getNum())
	            .orElseThrow(() -> new EntityNotFoundException("댓글이 없습니다."));

	    commentEntity.setName(dto.getName());
	    commentEntity.setComment(dto.getComment());

	    commentRepository.save(commentEntity);
	}

}
