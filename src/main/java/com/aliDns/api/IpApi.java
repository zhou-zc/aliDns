package com.aliDns.api;

import com.aliDns.dto.Ip;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IpApi {
    /**
     * 获取当前服务器的IP
     * @return
     */
    @GET("/")
    Call<Ip> getIp();
}
