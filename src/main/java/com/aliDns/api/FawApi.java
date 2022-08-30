package com.aliDns.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.Map;

public interface FawApi {
    @Headers({
            "User-Agent: okhttp/3.11.0",
            "platform: 1",
            "version: 4.1.0",
            "aid: 1284530915985551362",
            "Authorization: VEhWVnFEZ2laTkdsVlVaMllscnQrVU9rVStyTkl2eENLa3JOQWUvaCtmRFB4RDF0NzBhc1N3c3BTWXJVWDc1YVZ6R00xSkltcVJPbwpreUdtbmdEUHl3PT0-___1661766460504___763105___763627___M001___1",
            "Content-Type: application/json"
    })
    @POST("fawcshop/usercenter/verification/getQrCode")
    Call<Map<String,String>> getQrCode(@Body Map<String,String> data);
}
