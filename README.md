# 📝 BlogApp - Spring Boot Web Blog Application

**BlogApp** é uma aplicação web de blog desenvolvida com **Spring Boot** que permite criar, visualizar, editar e excluir posts e comentários. A aplicação inclui autenticação de usuários com **Spring Security**, design responsivo com **Bootstrap** e persistência com **MySQL**.

---

## 📌 Funcionalidades

### 📃 Posts
- ✅ Listar todos os posts
- 🆕 Criar novos posts
- 🔍 Visualizar detalhes de um post
- ✏️ Editar posts existentes
- ❌ Excluir posts

### 💬 Comentários
- 💬 Adicionar comentários aos posts
- ✏️ Editar comentários
- 🗑️ Excluir comentários

### 🔐 Segurança
- 🔒 Autenticação de usuários com Spring Security
- 👤 Dois usuários pré-configurados: `admin` e `user`
- 🔐 Rotas públicas e rotas protegidas por perfil

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **Thymeleaf**
- **MySQL**
- **Bootstrap 5**
- **Maven**

---

## 🗂️ Estrutura do Projeto (MVC)

```
com.web.BlogApp
├── BlogAppApplication.java       # Classe principal
├── configuration/                # Configurações do sistema e segurança
├── controller/                   # Controladores REST/HTTP
├── dtos/                         # Objetos de Transferência de Dados (DTOs)
├── model/                        # Entidades JPA (Posts e Comentários)
├── repository/                   # Interfaces JPA para o banco de dados
├── service/                      # Camada de serviços (lógica de negócios)
└── utils/                        # Classes utilitárias
```

---

## 💾 Banco de Dados

Banco: `blogappdbweb`

### Tabela `TB_POST`

| Campo   | Tipo   | Descrição               |
|---------|--------|-------------------------|
| `id`    | UUID   | Identificador único     |
| `autor` | String | Nome do autor           |
| `data`  | Date   | Data de criação         |
| `titulo`| String | Título da postagem      |
| `texto` | String | Conteúdo da postagem    |

---

## 🚀 Como Executar

### ✅ Pré-requisitos

- Java 17
- Maven
- MySQL Server

### 🧬 Passos para rodar o projeto

#### 1. Clone o Repositório

```bash
git clone https://github.com/yourusername/web-blog-application.git
cd web-blog-application
```

#### 2. Crie o Banco de Dados

No seu MySQL:

```sql
CREATE DATABASE blogappdbweb;
```

#### 3. Configure o `application.properties`

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost/blogappdbweb?createIfNotExists=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=America/Sao_Paulo
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### 4. Compile e Rode

```bash
mvn clean install
mvn spring-boot:run
```

Acesse a aplicação em: [http://localhost:8080](http://localhost:8080)

---

## 👤 Usuários de Teste

| Usuário | Senha     | Perfil  |
|---------|-----------|---------|
| admin   | admin123  | ADMIN   |
| user    | user123   | USER    |

---

## 🧪 Dados de Teste

O sistema carrega automaticamente dados de exemplo ao iniciar a aplicação, facilitando testes e visualização da interface.

---

## 🧾 Licença

Distribuído sob a licença **MIT**. Veja o arquivo `LICENSE` para mais informações.

