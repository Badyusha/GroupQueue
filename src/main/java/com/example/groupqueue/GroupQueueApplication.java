package com.example.groupqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class GroupQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupQueueApplication.class, args);
    }
}
