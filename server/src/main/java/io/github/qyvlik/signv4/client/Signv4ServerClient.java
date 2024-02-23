package io.github.qyvlik.signv4.client;

import io.github.qyvlik.domain.Result;
import io.github.qyvlik.signv4.server.gateway.action.request.ComplexJsonReq;
import io.github.qyvlik.signv4.server.gateway.action.request.PostRequest;
import retrofit2.Call;
import retrofit2.http.*;

public interface Signv4ServerClient {
    @POST("/api/v1/post-form")
    @FormUrlEncoded
    Call<Result<String>> echoPostForm(@Field(value = "param1") String param1,
                                      @Field(value = "param2") String param2);

    // todo 无法使用 PostRequest，找不到合适的注解
    @POST("/api/v1/post-form-2")
    @FormUrlEncoded
    Call<Result<String>> echoPostForm2(@Field(value = "param1") String param1,
                                       @Field(value = "param2") String param2,
                                       @Query("param3") String param3);

    @PUT("/api/v1/put-form")
    @FormUrlEncoded
    Call<Result<String>> echoPutForm(@Field("param1") String param1,
                                     @Field("param2") String param2,
                                     @Field("param3") String param3);

    /**
     * DELETE not support application/x-www-form-urlencoded
     * <p>
     * FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).
     */
    @DELETE("/api/v1/delete-form")
    Call<Result<String>> echoDeleteForm(@Query("param1") String param1,
                                        @Query("param2") String param2);

    @POST("/api/v1/post-json")
    Call<Result<String>> echoPostJSON(@Body PostRequest request,
                                      @Query("param3") String param3);


    @HTTP(method = "DELETE", path = "/api/v1/delete-json", hasBody = true)
    Call<Result<String>> echoDeleteJSON(@Body PostRequest request,
                                        @Query("param3") String param3);

    @PUT("/api/v1/put-json")
    Call<Result<String>> echoPutJSON(@Body PostRequest request,
                                     @Query("param3") String param3);

    @POST("/api/v1/post-json-2")
    Call<Result<String>> echoPostJSON2(@Body ComplexJsonReq request,
                                       @Query("param3") String param3);


    @GET("/api/v1/time")
    Call<Result<Long>> getTime();

    /**
     * HEAD method must use Void as response type.
     */
    @HEAD("/api/v1/head")
    Call<Void> head(@Query("param1") String param1,
                    @Query("param2") String param2);
}
