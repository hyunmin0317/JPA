package hellojpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 50)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
//    private Long id;

    // unique 보다 uniqueConstraints 사용 (제약조건명 지정 가능)
    @Column(name = "name", nullable = false, unique = true, length = 10)
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)    // 기본 ORDINAL 사용 X
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // 최신 하이버네이트 지원
    private LocalDate testLocalDate;            // @Temporal(TemporalType.DATE)
    private LocalDateTime testLocalDateTime;    // @Temporal(TemporalType.TIME)

    // CLOB: String, char[], java.sql.CLOB - 필드 타입이 문자
    // BLOB: byte[], java.sql.BLOB - 나머지
    @Lob
    private String description;

    // 필드 매핑 X -> 주로 메모리상에서만 임시로 보관 (데이터베이스에 저장 X, 조회 X)
    @Transient
    private int temp;
}
