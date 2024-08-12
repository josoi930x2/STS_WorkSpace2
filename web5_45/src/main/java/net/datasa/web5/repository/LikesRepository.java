package net.datasa.web5.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.datasa.web5.domain.entity.BoardEntity;
import net.datasa.web5.domain.entity.LikesEntity;
import net.datasa.web5.domain.entity.MemberEntity;

public interface LikesRepository extends JpaRepository<LikesEntity, Integer> {
//	NullPointerException 방지: 명시적으로 null 처리를 할 수 있도록 하여 코드에서 발생할 수 있는 NullPointerException을 줄일 수 있습니다.
//	코드 가독성 향상: 메서드의 반환 값이 null일 수 있는지 명확하게 표시하므로, 코드를 읽고 이해하기 쉬워집니다.
//	명확한 API 설계: Optional을 반환함으로써 호출자에게 값을 체크하도록 강제할 수 있습니다.
	Optional<LikesEntity> findByMemberAndBoard(MemberEntity member, BoardEntity board);
}
