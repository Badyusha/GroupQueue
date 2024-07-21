package com.example.groupqueue.controllers.start;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

        return "/views/startPage/startPageDesktop";
    }
}
