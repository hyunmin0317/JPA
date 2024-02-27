package hellojpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "MBR4")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member4 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    @Embedded
    private Period workPeriod;

    @Embedded
    private Address homeAddress;
}
