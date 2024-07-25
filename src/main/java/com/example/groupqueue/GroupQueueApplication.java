package com.example.groupqueue;

import com.example.groupqueue.services.GroupScheduleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class GroupQueueApplication {
    public static void main(String[] args) {
////        Crypt data
//        try {
//            String hash = Hash.hashData("root");
//            System.err.println(hash);
//        } catch (NoSuchAlgorithmException err) {
//            err.printStackTrace();
//        }

        System.err.println(GroupScheduleService.getGroupSchedule(272303));

        SpringApplication.run(GroupQueueApplication.class, args);
    }
}
