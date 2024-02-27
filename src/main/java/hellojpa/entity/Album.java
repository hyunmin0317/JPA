package hellojpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "A")
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
}
