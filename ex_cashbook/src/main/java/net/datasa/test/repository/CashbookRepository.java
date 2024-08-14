package net.datasa.test.repository;

import net.datasa.test.domain.entity.CashbookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 게시판 관련 repository
 */

@Repository
public interface CashbookRepository extends JpaRepository<CashbookEntity, Integer> {

    //한 사용자의 모든 가계부 내역을 날짜순으로 조회
    List<CashbookEntity> findByMember_MemberIdOrderByInputDate(String memberId);


    //한 사용자의 특정 연도의 수입 또는 지출의 합계를 계산
    //전달받은 값을 Query의 지정한 곳에 넣어서 쿼리를 실행한다.
    //쿼리는 Entity 클래스의 이름을 기준으로 작성한다.
    //전달받은 값은 @Param에서 지정한 이름과 같은 (예) :year ) 부분에 대입된다.
    //매개변수의 이름은 상관없다. (예) y)
    // """는 문자열을 여러 줄에 걸쳐서 입력할 수 있게 해준다.
    @Query("""
        SELECT SUM(c.amount) FROM CashbookEntity c 
        WHERE 
            c.type = :type 
            AND YEAR(c.inputDate) = :year
            AND c.member.memberId = :id
     """)
    Optional<Integer> sumAmountByTypeAndYear(@Param("year") int y, @Param("type") String t, @Param("id") String id);

}
