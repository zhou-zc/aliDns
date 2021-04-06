package com.aliDns.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliClient {
    @Value("${aliDns.accessKeyId}")
    private String accessKeyId;
    @Value("${aliDns.accessKeySecret}")
    private String accessKeySecret;




}
