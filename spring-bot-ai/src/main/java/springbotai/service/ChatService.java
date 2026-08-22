package springbotai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import springbotai.tool.CalculatorTools;
import springbotai.tool.DateTimeTools;
import springbotai.tool.TransactionTools;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final DateTimeTools dateTimeTools;
    private final CalculatorTools calculatorTools;
    private final TransactionTools transactionTools;

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            DateTimeTools dateTimeTools,
            CalculatorTools calculatorTools,
            TransactionTools transactionTools) {

        this.dateTimeTools = dateTimeTools;
        this.calculatorTools = calculatorTools;
        this.transactionTools = transactionTools;

        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Você é um assistente de programação e finanças chamado Spring Bot AI.

                        Responda sempre em português do Brasil.
                        Ajude estudantes iniciantes com Java, Spring Boot, APIs REST,
                        bancos de dados, inteligência artificial e orçamento pessoal.

                        Para perguntas sobre data, hora, dia ou horário atual,
                        use a ferramenta getCurrentDateTime.

                        Para somar dois números, use a ferramenta sum.
                        Nunca invente o resultado de um cálculo quando a ferramenta
                        estiver disponível.

                        REGRA PARA DESPESAS:
                        Sempre que o usuário disser que gastou, pagou, comprou,
                        fez uma compra ou quiser registrar uma despesa, chame
                        obrigatoriamente a ferramenta registerExpense.

                        Considere "mercado", "supermercado", "padaria", "farmácia",
                        "combustível", "transporte", "restaurante", "lazer" e
                        "contas" como categorias válidas quando aparecerem na mensagem.
                        Se a mensagem disser "no mercado", a categoria é "mercado".

                        Para a mensagem "gastei 45.90 reais no mercado comprando frutas",
                        chame registerExpense com:
                        amount = 45.90
                        category = mercado
                        description = comprando frutas

                        Só pergunte a categoria se a mensagem realmente não contiver
                        nenhuma informação que possa definir uma categoria.
                        """)
                .build();
    }

    public String respond(String message) {
        return chatClient.prompt()
                .user(message)
                .tools(dateTimeTools, calculatorTools, transactionTools)
                .call()
                .content();
    }
}