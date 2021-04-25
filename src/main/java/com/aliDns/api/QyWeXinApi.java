package com.aliDns.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

public interface QyWeXinApi {
    @GET("cgi-bin/gettoken")
    Call<Map> getToken(@Query("corpid") String corpid,@Query("corpsecret") String corpsecret);
}
