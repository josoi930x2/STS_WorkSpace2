package net.datasa.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.test.domain.dto.CashbookDTO;
import net.datasa.test.security.AuthenticatedUser;
import net.datasa.test.service.CashbookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가계부 관련 콘트롤러
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("cashbook")
public class CashbookController {

    //가계부 관련 처리 서비스
    private final CashbookService cashbookService;

    /**
     * 가계부 페이지로 이동
     * @return 가계부 출력 페이지
     */
    @GetMapping("view")
    public String listAll() {
        return "cashbook";
    }

    /**
     * 가계부 내역 저장
     * @param cashbookDTO   작성한 가계부 내역
     * @param user          로그인한 사용자 정보
     */
    @ResponseBody
    @PostMapping("input")
    public void write(CashbookDTO cashbookDTO
            , @AuthenticationPrincipal AuthenticatedUser user) {

        //작성한 내용에 사용자 아이디 추가
        cashbookDTO.setMemberId(user.getUsername());
        log.debug("저장할 정보 : {}", cashbookDTO);

        //서비스로 전달하여 저장
        cashbookService.input(cashbookDTO);
    }

    /**
     * 가계부 내역 목록 조회
     * @param user  로그인한 사용자 정보
     * @return  가계부 내역 리스트
     */
    @ResponseBody
    @GetMapping("list")
    public List<CashbookDTO> list(@AuthenticationPrincipal AuthenticatedUser user) {
        //서비스로 사용자 아이디를 전달하여 해당 아이디의 수입,지출 내역을 목록으로 리턴한다.
        List<CashbookDTO> list = cashbookService.getList(user.getUsername());
        return list;
    }

    /**
     * 가계부 내역 삭제
     * @param infoNum   삭제할 번호
     * @param user      사용자 정보
     */
    @ResponseBody
    @PostMapping("delete")
    public void delete(@RequestParam("infoNum") Integer infoNum
            , @AuthenticationPrincipal AuthenticatedUser user) {
        log.debug("삭제할 번호 : {}, 로그인 아이디 : {}", infoNum, user.getUsername());

        //서비스로 가계부내역 번호와 사용자 아이디를 전달하여 삭제한다.
        cashbookService.delete(infoNum, user.getUsername()) ;
    }


    /**
     * 수입 지출 통계 페이지로 이동
     * @return 통계 페이지 HTML 파일
     */
    @GetMapping("stats")
    public String stats() {

        return "stats";
    }

    /**
     * 전달된 연도의 수입 또는 지출 합계 금액을 구하여 리턴
     * @param year          연도
     * @param type          수입 또는 지출
     * @param user          로그인한 사용자 정보
     * @return              합계 금액
     */
    @ResponseBody
    @PostMapping("getTotal")
    public Integer getTotal(
            @RequestParam("year") Integer year
            , @RequestParam("type") String type
            , @AuthenticationPrincipal AuthenticatedUser user) {

        log.debug("연도: {}, 구분: {}, 사용자아이디: {}", year, type, user.getUsername());

        //서비스로 연도, 구분, 아이디를 전달하여 합계 금액을 구한다.
        int total = cashbookService.getTotal(year, type, user.getUsername());
        return total;
    }

}
