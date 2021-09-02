package mapper;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bet {
    private Long id;
    private OffsetDateTime creationDate;
    private Set<Coupon> coupons;
}