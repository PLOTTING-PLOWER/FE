package com.example.plotting_fe.global;

import com.example.plotting_fe.global.dto.response.GeocodeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NCPApiService {
    @GET("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
    Call<GeocodeResponse> getGeocode(
            @Header("X-NCP-APIGW-API-KEY-ID") String apiKeyId,
            @Header("X-NCP-APIGW-API-KEY") String apiKey,
            @Query("query") String response);
}
