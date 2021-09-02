package aws.s3;

import aws.s3.dto.BetDto;
import aws.s3.dto.CouponDto;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
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
public class AmazonS3Service {
    String bucket;
    String key;
    AmazonS3 s3;
    S3Object obj;
    Context context;
    Boolean first = true;
    Boolean fail = false;
    List<CouponDto> coupons;
    public void analyzeInput(){
        try (InputStream stream = obj.getObjectContent()) {
            String text = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            List<Integer> list = new ArrayList<Integer>();
            int tempInt = 0;
            for(int i = 0; i < text.length(); i++)
            {
                char c = text.charAt(i);
                if(!isDigit(c)){
                    if(tempInt!=0) {
                        list.add(tempInt);
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
                    Set<Integer> set = new HashSet<>(list);
                    if(set.size()==6) {
                        CouponDto couponDto = new CouponDto(set);
                        coupons.add(couponDto);
                    }
                    if(set.size() != list.size() && set.size()!=6 || list.size()!=6){
                        fail= true;
                        context.getLogger().log("\n " + "zly bet" );
                    }
                    first=true;
                }
            }
            Set<Integer> set = new HashSet<>(list);

            if(set.size() != list.size()){
                fail= true;
                context.getLogger().log("\n " + "zly bet" );
            }
            stream.transferTo(System.out);
            System.out.println();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    public void moveFile(){
        try {
            String target;
            if(fail){
                target = "error/"+key;
            }
            else{
                target = "success/"+key;
            }
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, key, bucket, target);
            s3.copyObject(copyObjectRequest);
            s3.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public AmazonS3Service(String bucket, String key, AmazonS3 s3, S3Object obj, Context context) {
        this.bucket = bucket;
        this.key = key;
        this.s3 = s3;
        this.obj = obj;
        this.context = context;
        this.coupons = new ArrayList();
    }

    public Boolean getFail() {
        return fail;
    }

    public List<CouponDto> getCoupons() {
        return coupons;
    }
}
