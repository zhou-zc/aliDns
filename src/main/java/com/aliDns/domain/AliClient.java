package com.aliDns.domain;

import com.aliDns.task.ALiDdnsTask;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AliClient {
    private static final Logger log = LoggerFactory.getLogger(ALiDdnsTask.class);
    private IAcsClient client = null;
    public AliClient(@Value("${aliDns.accessKeyId}") String accessKeyId, @Value("${aliDns.accessKeySecret}") String accessKeySecret){
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Alidns", "alidns.aliyuncs.com");
            client = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error("添加Endpoint错误");
        }
    }

}
