package springbotai.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTools {

    @Tool(description = "Soma dois valores numéricos com precisão")
    public String sum(
            @ToolParam(description = "Primeiro número")
            BigDecimal firstNumber,

            @ToolParam(description = "Segundo número")
            BigDecimal secondNumber) {

        BigDecimal result = firstNumber.add(secondNumber);

        return result.setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}