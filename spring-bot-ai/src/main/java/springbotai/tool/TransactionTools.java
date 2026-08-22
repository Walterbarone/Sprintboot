package springbotai.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import springbotai.entity.Transaction;
import springbotai.repository.TransactionRepository;

@Component
public class TransactionTools {

    private final TransactionRepository transactionRepository;

    public TransactionTools(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(description = """
            Registra uma despesa financeira informada pelo usuário.
            Use esta ferramenta quando o usuário disser que gastou, pagou,
            comprou algo ou quiser registrar uma despesa.
            """)
    public String registerExpense(

            @ToolParam(description = "Valor da despesa em reais, por exemplo: 45.90")
            BigDecimal amount,

            @ToolParam(description = "Categoria da despesa, por exemplo: mercado, transporte ou lazer")
            String category,

            @ToolParam(description = "Descrição curta da despesa, por exemplo: compra no supermercado")
            String description) {

        BigDecimal formattedAmount = amount.setScale(2, RoundingMode.HALF_UP);

        Transaction transaction = new Transaction(
                formattedAmount,
                category.trim().toLowerCase(),
                description.trim()
        );

        Transaction savedTransaction = transactionRepository.save(transaction);

        String registeredAt = savedTransaction.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        return """
                Despesa registrada com sucesso.
                Identificador: %d
                Valor: R$ %s
                Categoria: %s
                Descrição: %s
                Data do registro: %s
                """.formatted(
                savedTransaction.getId(),
                savedTransaction.getAmount().toPlainString(),
                savedTransaction.getCategory(),
                savedTransaction.getDescription(),
                registeredAt
        );
    }
}