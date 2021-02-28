package com.aliDns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zhou-zc
 */
@SpringBootApplication
@EnableScheduling
public class AliddnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliddnsApplication.class, args);
    }

}
