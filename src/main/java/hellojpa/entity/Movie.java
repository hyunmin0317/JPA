package hellojpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "M")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("M")
public class Movie extends Item {

    private String director;

    private String actor;
}
