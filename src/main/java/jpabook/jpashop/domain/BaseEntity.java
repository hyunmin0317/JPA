package jpabook.jpashop.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    private String createdBy;

    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDate lastModifiedDate;
}
