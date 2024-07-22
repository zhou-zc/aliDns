package com.aliDns.domain;

import com.github.promeg.pinyinhelper.Pinyin;
import org.springframework.stereotype.Service;

/**
 * @author zhou-zc
 */
@Service
public class DomainService {

    /**
     * 添加域名
     */
    public void addDomain(){
        String 中文域名 = Pinyin.toPinyin("中文域名", "");
        System.out.println("中文域名 = " + 中文域名);
    }
}
