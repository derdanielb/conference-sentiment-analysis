package net.csa.conference;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTest {


    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

}
