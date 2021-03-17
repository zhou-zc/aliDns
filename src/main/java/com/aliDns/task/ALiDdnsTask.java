package com.aliDns.task;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhou-zc
 */
@Component
public class ALiDdnsTask {
    private static final Logger log = LoggerFactory.getLogger(ALiDdnsTask.class);
    private static final Map<String,String> cache = new HashMap<>(4);
    private static IAcsClient client = null;
    private String IP = "";
    static {
        try {
            String regionId = "cn-hangzhou";
            String accessKeyId = "";
            String accessKeySecret = "";
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Alidns", "alidns.aliyuncs.com");
            client = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error("添加Endpoint错误");
        }
    }
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduledTask() {
        String ip = this.getIp();
        if(!IP.equals(ip)){
            try {
                final List<DescribeDomainRecordsResponse.Record> records = getRecords();
                for (DescribeDomainRecordsResponse.Record record : records) {
                    final String rr = record.getRR();
                    final String value = record.getValue();
                    if(rr.equals("dev") && !ip.equals(value)){
                        // 更新解析
                        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
                        request.setRR("dev");
                        request.setRecordId("20373458475616256");
                        request.setType("A");
                        request.setValue(ip);
                        UpdateDomainRecordResponse response = client.getAcsResponse(request);
                        log.info("更新云解析结果：{}",response.toString());
                    }
                }
            } catch (ClientException e) {
                log.error("更新云解析结果：{}",e.getErrMsg());
            }
            IP = ip;
        }

    }

    /**
     * 获取ip地址
     * @return
     */
    private String getIp(){
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://jsonip.com/", String.class);
        Gson gson = new Gson();
        final Map<String,String> dataMap = gson.fromJson(response, Map.class);
        String ip = dataMap.get("ip");
        log.info("获取的IP:【{}】",ip);
        return ip;
    }

    /**
     * 获取解析列表
     * @return
     * @throws ClientException
     */
    private List<DescribeDomainRecordsResponse.Record> getRecords() throws ClientException {
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName("xxxx.com.cn");
        DescribeDomainRecordsResponse response = client.getAcsResponse(request);
        final List<DescribeDomainRecordsResponse.Record> domainRecords = response.getDomainRecords();
        return domainRecords;
    }


}
