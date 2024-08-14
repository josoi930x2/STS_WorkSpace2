package net.datasa.test.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * 가계부 정보 엔티티
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cashbook_info")
public class CashbookEntity {
    //일련번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_num")
    private Integer infoNum;

    //작성자 정보 (외래키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private MemberEntity member;

    //구분
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    //내역
    @Column(name = "memo", length = 1000)
    private String memo;

    //금액
    @Column(name = "amount", columnDefinition = "int default 0")
    private Integer amount = 0;

    //날짜
    @Column(name = "input_date", nullable = false, columnDefinition = "date")
    private LocalDate inputDate;
}
