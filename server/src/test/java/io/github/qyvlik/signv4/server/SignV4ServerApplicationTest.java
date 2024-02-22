package io.github.qyvlik.signv4.server;

import io.github.qyvlik.signv4.client.AwsSignv4Signatory;
import io.github.qyvlik.signv4.client.BabbelAwsSignv4Signatory;
import io.github.qyvlik.signv4.client.Signv4ServerClient;
import io.github.qyvlik.signv4.server.gateway.action.request.ComplexJsonReq;
import io.github.qyvlik.signv4.server.gateway.action.request.PostRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignV4ServerApplicationTest {

    @LocalServerPort
    private int port;

    private Signv4ServerClient create(boolean usingBabble) {
        String accessKey = "test1";
        String secretKey = "test1";
        String region = "us-east-1";
        String service = "execute-api";
        Interceptor auth = usingBabble ? new BabbelAwsSignv4Signatory(accessKey, secretKey, region, service)
                : new AwsSignv4Signatory(accessKey, secretKey, region, service);

        return new Retrofit.Builder()
                .baseUrl("http://localhost:" + port)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .callTimeout(Duration.ofMinutes(10))
                        .addInterceptor(auth)
                        .build()
                )
                .build()
                .create(Signv4ServerClient.class);
    }

    private String randomString() {
        return  UUID.randomUUID() + "中文 +*%~&!\"#'(){}";
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoPostForm(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        var call = client.echoPostForm(param1, param2);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s", param1, param2), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoPostForm2(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        String param3 = randomString();

        var call = client.echoPostForm2(param1, param2, param3);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s, param3: %s", param1, param2, param3), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoPutForm(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        String param3 = randomString();

        var call = client.echoPutForm(param1, param2, param3);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s, param3: %s", param1, param2, param3), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoDeleteForm(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();

        var call = client.echoDeleteForm(param1, param2);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s", param1, param2), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoPostJSON(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        String param3 = randomString();
        PostRequest request = new PostRequest();
        request.setParam1(param1);
        request.setParam2(param2);

        var call = client.echoPostJSON(request, param3);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s, param3: %s", param1, param2, param3), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoDeleteJSON(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        String param3 = randomString();
        PostRequest request = new PostRequest();
        request.setParam1(param1);
        request.setParam2(param2);

        var call = client.echoDeleteJSON(request, param3);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s, param3: %s", param1, param2, param3), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoPutJSON(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        String param3 = randomString();
        PostRequest request = new PostRequest();
        request.setParam1(param1);
        request.setParam2(param2);

        var call = client.echoPutJSON(request, param3);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s, param3: %s", param1, param2, param3), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void echoPostJSON2(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        String param3 = randomString();

        ComplexJsonReq req = new ComplexJsonReq();
        req.setParam1(param1);
        req.setList(List.of(param2));

        var call = client.echoPostJSON2(req, param3);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
        assertEquals(String.format("param1: %s, param2: %s, param3: %s", param1, param2, param3), result.data());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void getTime(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        var call = client.getTime();
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
        var result = resp.body();
        assertNotNull(result);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void head(boolean usingBabble) throws IOException {
        Signv4ServerClient client = create(usingBabble);
        String param1 = randomString();
        String param2 = randomString();
        var call = client.head(param1, param2);
        var resp = call.execute();
        assertNotNull(resp);
        assertEquals(200, resp.code());
    }

}