package aws.sqs;

import aws.AWSClient;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.damagereport.model.BetApi;

public class AmazonSQSService {

    public void send(BetApi betApi) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(betApi);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        final AmazonSQS sqs = AWSClient.getAmazonSQSClient();
        String queueUrl = "http://localstack:4566/000000000000/sqs";
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(json);
        sqs.sendMessage(send_msg_request);
    }
}
