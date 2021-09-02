package hendler;

import aws.AWSClient;
import aws.s3.AmazonS3Service;
import aws.s3.dto.BetDto;
import aws.s3.dto.CouponDto;
import aws.sqs.AmazonSQSService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.util.HashSet;
import java.util.Set;

public class S3EventHandler implements RequestHandler<S3Event, String> {

    AmazonSQSService amazonSQSService = new AmazonSQSService();
    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        String bucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String key = s3Event.getRecords().get(0).getS3().getObject().getKey();

        final AmazonS3 s3 = AWSClient.getAmazonS3Client();

        S3Object obj = s3.getObject(new GetObjectRequest(bucket, key));
        AmazonS3Service amazonS3Service = new AmazonS3Service(bucket, key, s3, obj, context);
        amazonS3Service.analyzeInput();
        amazonS3Service.moveFile();
        if(!amazonS3Service.getFail()) {
            Set<CouponDto> set = new HashSet<>(amazonS3Service.getCoupons());
            BetDto betDto = new BetDto(set);
            amazonSQSService.send(betDto);
        }
        return obj.getObjectMetadata().getContentType();
    }


}