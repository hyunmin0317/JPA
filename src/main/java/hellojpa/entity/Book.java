package hellojpa.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "B")
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;

    private String isbn;
}
