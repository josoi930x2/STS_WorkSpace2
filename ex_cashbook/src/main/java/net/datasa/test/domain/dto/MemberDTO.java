package net.datasa.test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 DTO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    String memberId;
    String memberPw;
}
