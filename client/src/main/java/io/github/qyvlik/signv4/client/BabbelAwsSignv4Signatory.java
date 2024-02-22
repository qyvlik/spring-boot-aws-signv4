package io.github.qyvlik.signv4.client;

import com.babbel.mobile.android.commons.okhttpawssigner.OkHttpAwsV4Signer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BabbelAwsSignv4Signatory implements Interceptor {
    private final String accessKey;
    private final String secretKey;
    private final OkHttpAwsV4Signer signer;
    private final DateTimeFormatter formatter;


    /**
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param region    us-east-1
     * @param service   execute-api
     */
    public BabbelAwsSignv4Signatory(String accessKey,
                                    String secretKey,
                                    String region,
                                    String service) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.signer = new OkHttpAwsV4Signer(region, service);
        this.formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        // UTC: YYYYMMDDTHHMMSSZ
        String xAmzDate = ZonedDateTime
                .now(ZoneId.of("UTC"))
                .format(this.formatter);
        Request request = this.signer.sign(
                chain.request().newBuilder()
                        .addHeader("x-amz-date", xAmzDate)
                        .build(),
                this.accessKey,
                this.secretKey
        );
        return chain.proceed(request);
    }
}
