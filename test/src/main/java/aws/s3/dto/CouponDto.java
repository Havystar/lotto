package aws.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class CouponDto {


    private Set<Integer> numbers;

}