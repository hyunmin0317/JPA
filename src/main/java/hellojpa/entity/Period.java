package hellojpa.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
public class Period {

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
