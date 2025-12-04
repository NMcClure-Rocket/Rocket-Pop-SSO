package com.example.rocketpop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")  
public class DemoAPIEndpoints {

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/number/{number}")
    public String number(@PathVariable("number") int number) {
        return "Number is " + number;
    }

    @PostMapping("/printUser")
    public String printUser(@RequestBody Test test) {
        return "Name is " + test.name + " and age is " + test.age;
    }

    @GetMapping("/letter/{letter}")
    public String letter(@PathVariable("letter") String letter) {
        return "Letter is " + letter;
    }

    @PostMapping("/loginDemo")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        System.out.println(username);
        return new HashMap<String, String>() {{
            put("given_name","admin");
        }};
    }
}
