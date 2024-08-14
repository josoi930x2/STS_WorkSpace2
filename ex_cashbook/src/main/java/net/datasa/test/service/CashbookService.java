package net.datasa.test.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.dto.CashbookDTO;
import net.datasa.test.domain.entity.CashbookEntity;
import net.datasa.test.domain.entity.MemberEntity;
import net.datasa.test.repository.CashbookRepository;
import net.datasa.test.repository.MemberRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 가계부 관련 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CashbookService {

    private final CashbookRepository cashbookRepository;
    private final MemberRepository memberRepository;

    /**
     * 가계부 정보 저장
     *
     * @param cashbookDTO 저장할 정보
     */
    public void input(CashbookDTO cashbookDTO) {

        //로그인한 사용자의 아이디로 회원정보를 조회한다.
        MemberEntity memberEntity = memberRepository.findById(cashbookDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원아이디가 없습니다."));

        //엔티티 객체를 생성하여 전달된 DTO의 값을 저장한다.
        CashbookEntity cashbookEntity = new CashbookEntity();
        cashbookEntity.setMember(memberEntity);
        cashbookEntity.setType(cashbookDTO.getType());
        cashbookEntity.setMemo(cashbookDTO.getMemo());
        cashbookEntity.setAmount(cashbookDTO.getAmount());
        cashbookEntity.setInputDate(cashbookDTO.getInputDate());

        log.debug("저장되는 엔티티 : {}", cashbookEntity);

        //엔티티의 값을 테이블에 저장한다.
        cashbookRepository.save(cashbookEntity);
    }

    /**
     * 로그인한 사용자의 가계부 내역 조회
     * @param id 사용자 아이디
     * @return 가계부 내역 목록
     */
    public List<CashbookDTO> getList(String id) {
        //로그인한 사용자의 가계부 내역을 날짜순으로 정렬하여 조회한다.
        List<CashbookEntity> entityList = cashbookRepository.findByMember_MemberIdOrderByInputDate(id);
        //DTO를 저장할 리스트 생성
        List<CashbookDTO> dtoList = new ArrayList<>();

        //DB에서 조회한 해당 사용자의 가계부 내역을 DTO객체로 변환하여 ArrayList에 저장한다.
        for (CashbookEntity entity : entityList) {
            CashbookDTO dto = CashbookDTO.builder()
                    .infoNum(entity.getInfoNum())
                    .memberId(entity.getMember().getMemberId())
                    .type(entity.getType())
                    .amount(entity.getAmount())
                    .memo(entity.getMemo())
                    .inputDate(entity.getInputDate())
                    .build();
            dtoList.add(dto);
        }
        //DTO객체들이 저장된 리스트를 리턴한다.
        return dtoList;
    }

    /**
     * 가계부 내역 삭제
     * @param infoNum   삭제할 번호
     * @param username  로그인한 사용자 아이디
     */
    public void delete(Integer infoNum, String username) {
        //전달받은 번호의 가계부 내역을 조회한다.
        CashbookEntity entity = cashbookRepository.findById(infoNum)
                .orElseThrow(() -> new EntityNotFoundException("해당 번호의 내역이 없습니다."));

        //로그인한 사용자 본인의 데이터인지 확인하고, 아니면 예외를 발생시킨다.
        if (!entity.getMember().getMemberId().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        //DB 테이블에서 해당 정보를 삭제한다.
        cashbookRepository.deleteById(infoNum);
    }

    /**
     * 전달된 연도의 수입 또는 지출 합계 금액을 구하여 리턴
     * @param year          연도
     * @param type          수입 또는 지출
     * @param id            로그인한 사용자 아이디
     * @return              합계 금액
     */
    public int getTotal(Integer year, String type, String id) {
        //테이블의 정보를 지정한 조건으로 필터링하여 합계금액을 구한다.
        //해당되는 데이터가 없는 경우(null)는 0으로 처리한다.
        Integer total = cashbookRepository.sumAmountByTypeAndYear(year, type, id)
                .orElse(0);
        return total;
    }

}
