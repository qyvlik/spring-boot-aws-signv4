package io.github.qyvlik.signv4.client;

import io.github.qyvlik.signv4.client.signer.AwsV4Signer;
import io.github.qyvlik.signv4.domain.model.Signing;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AwsSignv4Signatory implements Interceptor {
    private final String accessKey;
    private final String secretKey;

    private final String region;
    private final String service;

    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");


    /**
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param region    us-east-1
     * @param service   execute-api
     */
    public AwsSignv4Signatory(String accessKey,
                              String secretKey,
                              String region,
                              String service) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.service = service;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final long now = System.currentTimeMillis();

        ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.of("UTC"));
        String requestDateTime = DATETIME_FORMATTER.format(dateTime);

        Request request = chain.request()
                .newBuilder().addHeader("x-amz-date", requestDateTime)
                .build();

        Signing signing = AwsV4Signer.signing(request,
                this.accessKey,
                this.secretKey,
                requestDateTime,
                region,
                service
        );

        System.out.println("client signing="+signing);

        return chain.proceed(request.newBuilder()
                .addHeader("Authorization", signing.authorization())
                .build());
    }
}
