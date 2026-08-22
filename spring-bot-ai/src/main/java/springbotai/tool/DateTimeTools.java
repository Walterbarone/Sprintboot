package springbotai.tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class DateTimeTools {

    @Tool(description = "Obtém a data e hora atual do servidor")
    public String getCurrentDateTime() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return LocalDateTime.now().format(formatter);
    }
}