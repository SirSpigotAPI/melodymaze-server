package app.melodymaze.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Oauth2LoginController {

    @RequestMapping("/signin")
    public String loginPage() {
        return "login";
    }

}
