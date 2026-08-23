# Spring Bot AI

API inteligente desenvolvida com **Java**, **Spring Boot**, **Spring AI**, **Ollama**, **PostgreSQL** e recursos locais de Inteligência Artificial.

O projeto funciona como um assistente de programação e finanças pessoais. Ele responde perguntas, executa ferramentas reais, registra despesas no banco de dados e transforma arquivos de áudio em texto usando Whisper local.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Validation
- Spring Data JPA
- Spring AI
- Ollama
- Modelo Llama 3.2 3B
- PostgreSQL 16
- Docker e Docker Compose
- Faster-Whisper
- Python 3.11
- CTranslate2
- Hibernate
- Maven

## Funcionalidades

- Chat com modelo de linguagem local usando Ollama.
- Assistente configurado para responder em português do Brasil.
- Respostas sobre Java, Spring Boot, APIs REST, banco de dados, IA e finanças.
- Tool Calling com Spring AI.
- Consulta de data e hora atual.
- Soma de dois números através de ferramenta Java.
- Registro de despesas por linguagem natural.
- Persistência de despesas no PostgreSQL.
- API REST para criar e consultar despesas.
- Upload de áudio e transcrição local com Faster-Whisper.
- Funcionamento sem API Key e sem créditos de serviços de IA externos.

## Arquitetura

```text
Usuário
   │
   ├── POST /api/chat
   │      │
   │      ▼
   │   ChatController
   │      │
   │      ▼
   │   ChatService
   │      │
   │      ▼
   │   Spring AI + Ollama
   │      │
   │      ▼
   │   Tool Calling
   │      ├── DateTimeTools
   │      ├── CalculatorTools
   │      └── TransactionTools
   │               │
   │               ▼
   │        TransactionRepository
   │               │
   │               ▼
   │          PostgreSQL
   │
   ├── POST /api/expenses
   ├── GET /api/expenses
   ├── GET /api/expenses/{id}
   │
   └── POST /api/transcriptions
          │
          ▼
     TranscriptionController
          │
          ▼
     TranscriptionService
          │
          ▼
     scripts/transcribe.py
          │
          ▼
     Faster-Whisper local
```

## Estrutura do projeto

```text
spring-bot-ai/
├── scripts/
│   └── transcribe.py
├── src/
│   └── main/
│       ├── java/
│       │   └── springbotai/
│       │       ├── controller/
│       │       │   ├── ChatController.java
│       │       │   ├── DateTimeController.java
│       │       │   ├── ExpenseController.java
│       │       │   └── HealthController.java
│       │       ├── dto/
│       │       │   ├── ChatRequest.java
│       │       │   ├── ChatResponse.java
│       │       │   ├── ErrorResponse.java
│       │       │   └── ExpenseRequest.java
│       │       ├── entity/
│       │       │   └── Transaction.java
│       │       ├── exception/
│       │       │   └── GlobalExceptionHandler.java
│       │       ├── repository/
│       │       │   └── TransactionRepository.java
│       │       ├── service/
│       │       │   ├── ChatService.java
│       │       │   └── TranscriptionService.java
│       │       ├── tool/
│       │       │   ├── CalculatorTools.java
│       │       │   ├── DateTimeTools.java
│       │       │   └── TransactionTools.java
│       │       └── SpringBotAiApplication.java
│       └── resources/
│           └── application.properties
├── compose.yaml
├── pom.xml
└── README.md
```

## Pré-requisitos

Antes de executar o projeto, instale:

- Java 21
- Docker Desktop
- Ollama
- Maven Wrapper incluído no projeto
- Python 3.11
- Faster-Whisper

Também é necessário baixar o modelo do Ollama:

```powershell
ollama pull llama3.2:3b
```

## Banco de dados

O PostgreSQL é iniciado com Docker Compose:

```powershell
docker compose up -d
```

O container do projeto utiliza a porta:

```text
localhost:5433
```

A aplicação utiliza o banco:

```text
spring_bot_ai
```

A tabela `transactions` é criada automaticamente pelo Hibernate ao iniciar a aplicação.

## Executando a aplicação

Inicie o banco de dados:

```powershell
docker compose up -d
```

Execute a aplicação Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Endpoint de chat

### Enviar uma mensagem ao assistente

```text
POST /api/chat
```

Exemplo no PowerShell:

```powershell
$body = @{
    message = "Gastei 45.90 reais no mercado comprando frutas"
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/chat" `
    -Method Post `
    -ContentType "application/json; charset=utf-8" `
    -Body ([System.Text.Encoding]::UTF8.GetBytes($body))
```

Exemplo de comportamento:

```text
Usuário: Gastei 45.90 reais no mercado comprando frutas

IA:
→ identifica valor, categoria e descrição
→ chama a ferramenta registerExpense
→ salva a transação no PostgreSQL
→ retorna uma confirmação ao usuário
```

## Endpoints de despesas

### Criar uma despesa manualmente

```text
POST /api/expenses
```

Exemplo:

```powershell
$expense = @{
    amount = 80.50
    category = "transporte"
    description = "Recarga do cartão de ônibus"
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/expenses" `
    -Method Post `
    -ContentType "application/json; charset=utf-8" `
    -Body ([System.Text.Encoding]::UTF8.GetBytes($expense))
```

### Listar despesas

```text
GET /api/expenses
```

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/expenses" `
    -Method Get
```

### Buscar despesa por identificador

```text
GET /api/expenses/{id}
```

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/expenses/1" `
    -Method Get
```

## Endpoint de transcrição

### Transcrever áudio em texto

```text
POST /api/transcriptions
```

O endpoint recebe um arquivo de áudio via `multipart/form-data` e utiliza o Faster-Whisper executado localmente.

Exemplo:

```powershell
curl.exe `
    -X POST `
    -F "file=@teste.wav" `
    http://localhost:8080/api/transcriptions
```

Resposta esperada:

```json
{
  "transcription": "mostra a cotação do dólar"
}
```

## Whisper local

O projeto utiliza Faster-Whisper para transcrição offline.

Crie e ative o ambiente virtual Python:

```powershell
py -3.11 -m venv .venv-whisper
.\.venv-whisper\Scripts\Activate.ps1
```

Instale a dependência:

```powershell
python -m pip install --upgrade pip
pip install faster-whisper
```

Teste a transcrição diretamente pelo script:

```powershell
python .\scripts\transcribe.py .\teste.wav
```

## Banco de dados: consulta manual

Para acessar o PostgreSQL pelo terminal:

```powershell
docker exec -it spring-bot-ai-postgres psql -U springbot -d spring_bot_ai
```

Listar tabelas:

```sql
\dt
```

Consultar todas as transações:

```sql
SELECT id, amount, category, description, created_at
FROM transactions
ORDER BY id DESC;
```

Sair do PostgreSQL:

```sql
\q
```

## Próximas melhorias

- Implementar Text-to-Speech local.
- Criar endpoint que transcreve áudio e envia automaticamente o texto ao chat.
- Criar relatórios de despesas por categoria e período.
- Criar limite de despesas por categoria.
- Adicionar autenticação de usuários.
- Criar testes unitários e de integração.
- Criar interface web ou aplicação mobile.
- Adicionar auditoria de ações do assistente.