package net.datasa.web5.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 리플 정보 엔티티
 */
@Builder
@Getter
@Setter
//BoardEntity와 ReplyEntity의 순환참조 문제로 toString() 호출시 오류일때 해당 필드를 제외
@ToString(exclude = "board")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "likes")
public class LikesEntity {
    //리플 테이블의 기본키
    //기본키 생성 전략 : 자동 증가(auto-increment) 값으로 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer likeId; 

    //작성자 정보 (외래키)
    //다대일 관계. 리플 여러개가 회원정보 하나를 참조한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private MemberEntity member;

    //게시글 정보 (외래키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_num")
    private BoardEntity board;

}
