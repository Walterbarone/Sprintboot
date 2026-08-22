package springbotai.controller;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import springbotai.dto.ExpenseRequest;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createExpense(
            @Valid @RequestBody ExpenseRequest request) {

        String amount = request.amount()
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();

        String registeredAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        return Map.of(
                "message", "Despesa registrada com sucesso.",
                "amount", "R$ " + amount,
                "category", request.category(),
                "description", request.description(),
                "registeredAt", registeredAt
        );
    }
}