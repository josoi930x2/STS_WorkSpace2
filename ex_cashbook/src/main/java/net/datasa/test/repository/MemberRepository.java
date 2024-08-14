package net.datasa.test.repository;

import net.datasa.test.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 회원 정보 Repository
 */

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

}
