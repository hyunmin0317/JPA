package hellojpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MBR")
public class Member2 {

    @Id
    private Long id;

    @Column(unique = true, length = 10)
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
