package aws.sqs;

import aws.AWSClient;
import aws.s3.dto.BetDto;
import aws.s3.dto.CouponDto;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


import java.util.List;

public class AmazonSQSService {

    public void send(BetDto betDto) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(betDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final AmazonSQS sqs = AWSClient.getAmazonSQSClient();
        String queueUrl = "http://localstack:4566/000000000000/sqs";
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(json)
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);
    }
}
