package jpabook.jpashop.domain;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class Album extends Item {

    private String artist;

    private String etc;
}
