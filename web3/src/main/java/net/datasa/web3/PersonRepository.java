package net.datasa.web3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * person 테이블 관련 쿼리 실행 메소드들을 정의
 */
@Repository
public interface PersonRepository 
	extends JpaRepository<PersonEntity, String> {

}
