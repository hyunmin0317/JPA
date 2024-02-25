package hellojpa.entity;

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
public class Member {

    @Id
    private Long id;

    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
