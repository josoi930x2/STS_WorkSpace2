package net.datasa.test.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 Entity
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cashbook_member")
public class MemberEntity {
    //회원 아이디
    @Id
    @Column(name = "member_id", length = 30)
    String memberId;

    //비밀번호
    @Column(name = "member_pw", nullable = false, length = 100)
    String memberPw;
}
