package app.melodymaze.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoggedOutController {

    @RequestMapping("/loggedOut")
    public String loggedOutPage() {
        return "logged-out";
    }

}
