package io.github.okraskat.room.optimizer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/greetings")
class HelloController {

    @GetMapping
    Map<String, String> hello() {
        return Map.of("message", "hello");
    }

}
