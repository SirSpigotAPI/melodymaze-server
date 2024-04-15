package app.melodymaze.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Oauth2LoginController {

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

}
