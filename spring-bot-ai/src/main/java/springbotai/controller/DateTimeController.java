package springbotai.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateTimeController {

    @GetMapping("/api/datetime")
    public Map<String, String> getDateTime() {
        LocalDateTime now = LocalDateTime.now();

        return Map.of(
                "dateTime", now.toString(),
                "message", "Data e hora atual do servidor"
        );
    }
}