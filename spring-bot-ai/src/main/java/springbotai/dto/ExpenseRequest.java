package springbotai.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExpenseRequest(

        @NotNull(message = "O valor da despesa é obrigatório.")
        @DecimalMin(
                value = "0.01",
                message = "O valor da despesa deve ser maior que zero."
        )
        BigDecimal amount,

        @NotBlank(message = "A categoria é obrigatória.")
        String category,

        @NotBlank(message = "A descrição é obrigatória.")
        String description
) {
}