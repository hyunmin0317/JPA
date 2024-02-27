package hellojpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "MBR4")
@Getter
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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String city;

    private String street;

    private String zipcode;
}
