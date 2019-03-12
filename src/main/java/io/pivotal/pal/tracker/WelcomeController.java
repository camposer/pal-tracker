package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {


    private final String hello;

    public WelcomeController(@Value("${welcome.message}") String hello, @Value("${welcome.ignored:NOT SET}") String s) {
        this.hello = hello;
    }

    @GetMapping("/")
    public String sayHello() {
        return hello;
    }
}
