package hendler;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Character.isDigit;

public class S3EventHandler implements RequestHandler<S3Event, String> {
    private Boolean first = true;
    SendReceiveMessages sendReceiveMessages = new SendReceiveMessages();
    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        context.getLogger().log("java " + s3Event);
        // Pull the event records and get the object content type
        String bucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String key = s3Event.getRecords().get(0).getS3().getObject().getKey();
        context.getLogger().log("asd2 " + bucket );
        S3Object obj = prepareS3().getObject(new GetObjectRequest(bucket, key));
        sendReceiveMessages.lol();
        try (InputStream stream = obj.getObjectContent()) {
            String text = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            // ArrayList<List<Integer> > listOfLists = new ArrayList<List<Integer> >();
            List<Integer> list = new ArrayList<Integer>();
            int tempInt = 0;
            for(int i = 0; i < text.length(); i++)
            {
                char c = text.charAt(i);

                if(!isDigit(c)){
                    if(tempInt!=0) {
                        list.add(tempInt);
                        context.getLogger().log(Integer.toString(tempInt) + " ");
                        tempInt = 0;
                    }
                }
                if(isDigit(c)){
                    if(first){
                        list.clear();
                        first = false;
                    }
                    if(tempInt != 0){
                        tempInt += tempInt*10 + Integer.parseInt(String.valueOf(c)) - tempInt;
                    }
                    else {
                        tempInt = Integer.parseInt(String.valueOf(c));

                    }

                }

                if(!isDigit(c) && !first && c!=','){

                    //  listOfLists.add(list);
                    Set<Integer> set = new HashSet<>(list);

                    if(set.size() != list.size()){
                        context.getLogger().log("\n " + "zly bet" );

                    }
                    first=true;
                }
            }
            Set<Integer> set = new HashSet<>(list);

            if(set.size() != list.size()){
                context.getLogger().log("\n " + "zly bet" );

            }
          stream.transferTo(System.out);
            System.out.println();
            context.getLogger().log("asd3 " +stream.transferTo(System.out) );

        } catch (IOException ioe) {
            //throw ioe;
            ioe.printStackTrace();
        }

        return obj.getObjectMetadata().getContentType();
        //return "elp";
    }

    public final String AWS_REGION = "eu-central-1";
    public final String S3_ENDPOINT = "http://localstack:4566";

    private AmazonS3 prepareS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials("AAAA", "BBBBB");

        AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(S3_ENDPOINT, AWS_REGION);

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.withEndpointConfiguration(config);
        builder.withPathStyleAccessEnabled(true);
        builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        return builder.build();
    }
}