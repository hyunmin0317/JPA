package hellojpa.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Address {

    private String city;

    private String street;

    private String zipcode;
}
