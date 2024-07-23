package com.example.groupqueue.controllers.start;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.groupqueue.services.GroupScheduleService;

@Controller
public class StartController {
    //  METHODS
    @GetMapping("/")
    public String start() {
//        @Autowired
//        UserRepository userRepository;
//        Iterable<User> users = userRepository.findAll();
//        for(User user : users) {
//            System.out.println(user);
//            System.out.println("================");
//        }

//        System.err.println(GroupScheduleService.getGroupSchedule(272303));

        return "/views/startPage/startPageDesktop";
    }
}
