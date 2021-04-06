package com.aliDns.api;

import com.aliDns.dto.Ip;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IpApi {
    @GET("/")
    Call<Ip> getIp();
}
