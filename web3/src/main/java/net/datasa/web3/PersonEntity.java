package net.datasa.web3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * DB의 person 테이블과 매핑되는 클래스
 */
@Data
@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "age")
    private Integer age;
}
