package aws.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class BetDto {


    private Set<CouponDto> coupons;

    public void addCoupon(CouponDto coupon) {
        this.coupons.add(coupon);
    }
}