package com.aliDns.task;

import com.aliDns.api.IpApi;
import com.aliDns.domain.AliClient;
import com.aliDns.dto.Ip;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

/**
 * 定时更新域名解析规则
 * @author zhou-zc
 */
@Component
public class ALiDdnsTask {
    private static final Logger log = LoggerFactory.getLogger(ALiDdnsTask.class);

    @Value("${aliDns.domainName}")
    private String domainName;

    @Autowired
    private AliClient aliClient;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduledTask() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonip.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpApi service = retrofit.create(IpApi.class);
        final Call<Ip> ipCall = service.getIp();
        ipCall.enqueue(new Callback<Ip>() {
            @Override
            public void onResponse(Call<Ip> call, Response<Ip> response) {
                final String ip = response.body().getIp();
                log.info("获取IP【{}】",ip);
                try {
                    final List<DescribeDomainRecordsResponse.Record> records = getRecords();
                    for (DescribeDomainRecordsResponse.Record record : records) {
                        final String rr = record.getRR();
                        final String value = record.getValue();
                        if(rr.equals("dev") && !value.equals(ip)){
                            // 更新解析
                            UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
                            request.setRR("dev");
                            request.setRecordId("20373458475616256");
                            request.setType("A");
                            request.setValue(ip);
                            UpdateDomainRecordResponse acsResponse = aliClient.getAcsClient().getAcsResponse(request);
                            log.info("更新云解析结果：{}",acsResponse.toString());
                        }
                    }
                } catch (ClientException e) {
                    log.error("更新云解析结果：{}",e.getErrMsg());
                }
            }

            @Override
            public void onFailure(Call<Ip> call, Throwable throwable) {
                log.error("获取IP失败");
            }
        });
    }
    /**
     * 获取解析列表
     * @return
     * @throws ClientException
     */
    private List<DescribeDomainRecordsResponse.Record> getRecords() throws ClientException {
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(domainName);
        DescribeDomainRecordsResponse response = aliClient.getAcsClient().getAcsResponse(request);
        final List<DescribeDomainRecordsResponse.Record> domainRecords = response.getDomainRecords();
        return domainRecords;
    }


}
