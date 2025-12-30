package com.operator;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CRMServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CRMServiceApplication.class, args);

        log.info("CRM Service started successfully!");
    }
}
