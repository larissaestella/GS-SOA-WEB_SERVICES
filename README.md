# 🌐 NexusWork – Plataforma de Aprendizado Inteligente e Gamificada

## Integrantes

* **Larissa Estella Gonçalves dos Santos - RM552695**
* **Joseh Gabriel Trimboli Agra - RM553094**
* **Pedro Henrique de Assumção Lima - RM552746**

---

## Descrição do Projeto

## 🔍 Visão Geral

A **NexusWork** é uma plataforma web desenvolvida para integrar:

* **Aprendizagem corporativa**
* **Gamificação com níveis e pontos**
* **Assistente virtual baseado em IA**
* **Acompanhamento de progresso do colaborador**
* **Gestão de cursos e recomendações inteligentes**

O projeto foi criado como parte da **Global Solution – Arquitetura Orientada a Serviços (SOA) e Web Services**, utilizando arquitetura REST e boas práticas de desenvolvimento orientado a serviços (SOA).

A aplicação permite:

* Cadastro e gestão de usuários
* Registro e acompanhamento de progresso em cursos 
* Sistema real de gamificação (XP, níveis, progresso)
* Assistente inteligente via API da OpenAI 
* Banco de dados MySQL com migrações Flyway 
* Controllers REST bem estruturados 
* DTOs para segurança e limpeza de dados 
* Tratamento de erros e exceções personalizadas

---

## 🚀 Como Executar o Projeto

1. **Clone esse repositório ou baixe os arquivos** para sua máquina.
2. Configuração de Variáveis de Ambiente ⚠️ **Importante!**
 
    Vá em ``` src/main/resources/application.properties ```
    ```properties
    # Substitua pelas suas variáveis de ambiente
    spring.datasource.url=jdbc:mysql://localhost:3306/nexusworkdb
    spring.datasource.username=root
    spring.datasource.password=${SUA_SENHA}
   
    openai.api.key=${OPENAI_API_KEY}
    openai.model=${OPENAI_MODEL:gpt-4.1-mini}
    ```     
2. **Criar o banco MySQL**

   ```sql
   CREATE DATABASE nexusworkdb;
   ```

3. **Executar o projeto no IntelliJ**
    ```
    NexusworkApplication -> Run
    ```

4. **Testar no Postman**

    * `POST /api/users` → cria usuário
    * `GET /api/users/{id}` → verifica pontos e nível
    * `POST /api/courses` → adiciona curso
    * `POST /api/progress` → atualiza progresso e ganha pontos
    * `POST /api/ai/chat` → envia dúvida para IA
    * `GET /api/ai/recommendations/ID`  → recebe recomendações da IA


# ▶️ **Como testar no Postman**
## Exemplo de Fluxo

1. O usuário se cadastra.
2. Escolhe um curso e conclui (progress = 100).
3. O sistema soma pontos e atualiza o nível automaticamente.
4. Pode fazer perguntas à IA sobre o conteúdo.
5. Recebe sugestões de novos cursos ou pausas via IA.

---

### 🔹 **Criar usuário**

POST
`http://localhost:8080/api/users`

Body:

```json
{
  "name": "SEU NOME",
  "email": "SEUNOME@nexuswork.com",
  "password": "SENHA"
}
```

GET
`http://localhost:8080/api/users/ID`


---

### 🔹 **Criar curso**

POST
`http://localhost:8080/api/courses`

```json
{
  "title": "Introdução à IA Generativa",
  "description": "Aprenda os fundamentos e aplicações práticas da IA generativa.",
  "difficulty": 2
}
```

GET
`http://localhost:8080/api/courses/ID`
---

### 🔹 **Registrar progresso**

POST
`http://localhost:8080/api/progress`

```json
{
  "userId": 1,
  "courseId": 1,
  "progress": 100,
}
```

✔️ Atualiza XP
✔️ Sobe nível
✔️ Marca como concluído

➤ Consultar progresso:
GET `http://localhost:8080/api/progress`

➤ Consultar progresso de um usuário:
GET `http://localhost:8080/api/progress/user/1`

➤ Consultar progresso de um usuário + curso:
GET `http://localhost:8080/api/progress/user/1/course/1`

---

### 🔹 **Assistente de IA**

POST
`http://localhost:8080/api/ai/chat`

```json
{
  "userId": 1,
  "courseId": 1,
  "question": "Quais cursos você recomenda para melhorar minhas habilidades em IA?"
}
```
GET
`http://localhost:8080/api/ai/recommendations/ID`

---

## 🏗️ Arquitetura do Projeto

O projeto segue a arquitetura em **camadas**:

* **Controller** → Recebe e responde às requisições HTTP (camada de apresentação)
* **Service** → Contém as regras de negócio
* **Repository** → Realiza o acesso e persistência de dados no banco MySQL
* **Entity / DTO** → Estruturas de dados para persistência e transferência
* **Exception** → Tratamento e padronização de erros da aplicação

---

## 📂 Estrutura do Projeto

```
skillquest/
│
├── src/
│   ├── main/
│   │   ├── java/br/com/skillquest/
│   │   │   ├── config/
│   │   │   │   └── WebClientConfig.java      → Configura o cliente HTTP para a API da OpenAI
│   │   │   ├── controller/
│   │   │   │   ├── AIController.java         → Controla o chat e recomendações via IA
│   │   │   │   ├── CourseController.java     → Gerencia os cursos disponíveis
│   │   │   │   ├── ProgressController.java   → Atualiza e retorna progresso e pontuação
│   │   │   │   └── UserController.java       → CRUD de usuários
│   │   │   ├── dto/
│   │   │   │   ├── AIQuestionDTO.java        → DTO para perguntas à IA
│   │   │   │   ├── CompletionRequestDTO.java → DTO para requisição à OpenAI
│   │   │   │   ├── CompletionResultDTO.java  → DTO para resposta da OpenAI
│   │   │   │   ├── CourseDTO.java            → DTO de curso
│   │   │   │   ├── ProgressDTO.java          → Transporte de progresso enviado pelo cliente
│   │   │   │   └── UserDTO.java              → DTO de usuário
│   │   │   ├── entity/
│   │   │   │   ├── Course.java               → Entidade curso
│   │   │   │   ├── User.java                 → Entidade usuário (pontos e nível)
│   │   │   │   └── UserCourseProgress.java   → Entidade de progresso (user + course)
│   │   │   │── enums/
│   │   │   │   └── CourseStatus.java         → 
│   │   │   │   └── UserRole.java             → 
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java → Trata erros globais da aplicação
│   │   │   │   └── ResourceNotFoundException.java → Erro personalizado para dados inexistentes
│   │   │   ├── repository/
│   │   │   │   ├── CourseRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── UserCourseProgressRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AIRecommendationService.java → Comunicação com a OpenAI
│   │   │   │   ├── CourseService.java
│   │   │   │   ├── GamificationService.java     → Lógica de pontos e níveis
│   │   │   │   ├── ProgressService.java
│   │   │   │   └── UserService.java
│   │   │   │── vo/
│   │   │   │   └── EmailVO.java              → 
│   │   │   │   └── LevelInfoVO.java          → 
│   │   │   └── SkillquestApplication.java     → Classe principal (entry point do Spring Boot)
│   │   └── resources/
│   │       ├── application.properties        → Configurações do MySQL e da API da OpenAI
│   │       └── db/migration/
│   │           ├── V1__create_users.sql
│   │           ├── V2__create_courses.sql
│   │           └── V3__user_course_progress.sql
│   └── test/java/br/com/skillquest/
│       └── SkillquestApplicationTests.java
│
├── pom.xml       → Dependências do projeto (Spring Boot, JPA, Flyway, WebClient, etc.)
└── README.md
```

---

## 💾 **Banco de Dados e Flyway**

Banco: **MySQL**
Migrações automáticas via **Flyway**

Tabelas:

* `users` → informações do colaborador (nome, pontos, nível)
* `courses` → lista de cursos
* `user_course_progress` → progresso individual em cada curso

As migrações estão em:

```
src/main/resources/db/migration
                        ├── V1__create_users.sql
                        ├── V2__create_courses.sql
                        └── V3__user_course_progress.sql
```

### V1 – Cria tabela de usuários

* ID
* Nome
* Email
* Senha
* Role
* Level
* Pontos

### V2 – Cria tabela de cursos

* ID
* Título
* Descrição
* Dificuldade
* Pontos

### V3 – Cria tabela de progresso do curso

* ID
* user_id (FK)
* course_id (FK)
* Progresso
* Completo (Status)
* Completo em (Data)

---

## ⚙️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3+**
* **Spring Data JPA**
* **Spring Web**
* **Flyway** (migração de banco)
* **MySQL**
* **Postman** (testes de endpoints)
* **OpenAI API (ChatGPT)** 

---

## 🔗 Consumo de API Externa (OpenAI)

O sistema consome a **API da OpenAI** para:

* Consultar IA sobre temas de cursos e responder dúvidas dos colaboradores (`/api/ai/chat`)
* Gerar recomendações de novos cursos(`/api/ai/recommendations`)

---

## 🔐 Segurança e Validações

* Validação de entrada em DTOs (uso de `@Valid`, `@NotNull`, etc.)
* Tratamento de erros com `GlobalExceptionHandler`
* Respostas padronizadas para exceções (`ResourceNotFoundException`, `IllegalArgumentException`)

---

# 🧬 **Gamificação**

Cada curso concluído gera XP automaticamente:

* 100% progresso → ganha XP
* XP acumulado → sobe de nível
* Enum `UserLevel` define cargos/níveis

Regras implementadas no `ProgressService`.

---


# 📝 **Conclusão**

O projeto NexusWork entrega uma solução robusta, moderna e escalável, unindo:

* **APIs REST organizadas**
* **Integração real de IA**
* **Gamificação funcional**
* **Arquitetura limpa**
* **Migrações e persistência confiáveis**

