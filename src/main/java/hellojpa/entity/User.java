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
public class User {

    @Id
    private Long id;

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

    @Transient
    private int temp;
}
