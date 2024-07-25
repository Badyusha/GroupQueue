package com.example.groupqueue.controllers.start;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {
//    @Autowired
//    ClientRepository clientRepository;

    //  METHODS
    @GetMapping("/")
    public String start() {
//        Iterable<Client> clients = clientRepository.findAll();
//        for(Client client : clients) {
//            System.out.println(client.getEmail());
//            System.out.println("================");
//        }

//        System.err.println(GroupScheduleService.getGroupSchedule(272303));

        return "/views/startPage/startPageDesktop";
    }
}
