package pl.fissst.lbd.lotto.component;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import pl.fissst.lbd.lotto.model.BetModelApi;
import pl.fissst.lbd.lotto.service.BetService;
import pl.fissst.lbd.lotto.dto.BetDto;
import pl.fissst.lbd.lotto.mapper.BetMapper;


@Component
@RequiredArgsConstructor
public class SqsMessage {

    private final BetMapper betMapper;
    private final BetService betService;

    @SqsListener(value = "sqs")
    public void test(BetModelApi betModelApi) {
        System.out.println("sdasdasdie");
        System.out.println(betModelApi.getCoupons());
        BetDto betDto = betMapper.mapModelApiToDto(betModelApi);
        BetDto resultDto = betService.create(betDto);
    }

}
