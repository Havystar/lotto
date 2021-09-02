package mapper;

import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    private Long id;
    private OffsetDateTime creationDate;
    private Bet bet;
    private Integer[] numbers;
}
