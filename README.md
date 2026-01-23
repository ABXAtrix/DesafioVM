# Cloud Manager API — Backend

API RESTful responsável pelo gerenciamento de **Máquinas Virtuais (VMs)**, permitindo operações de cadastro, atualização, controle de estado e auditoria de ações executadas no sistema.

Este projeto foi desenvolvido como parte de um **desafio técnico (nível júnior)**, seguindo boas práticas de arquitetura, segurança e documentação.

---

## Visão Geral

A **Cloud Manager API** atua como o núcleo de processamento do sistema, fornecendo endpoints para:

- Gerenciamento completo de máquinas virtuais (CRUD)
- Controle de estados das VMs (START, STOP, SUSPEND)
- Dashboards interativos e dinâmicos 
- Autenticação e autorização via JWT
- Registro de auditoria de todas as operações
- Documentação automática via Swagger

---

## Tecnologias Utilizadas


- | Tecnologia | Descrição |
- | **Java 21** | Linguagem principal do projeto 
- | **Spring Boot 3** | Framework para construção da API 
- | **Spring Security** | Camada de segurança 
- | **JWT (HS256)** | Autenticação stateless 
- | **PostgreSQL** | Banco de dados relacional 
- | **Maven** | Gerenciador de dependências 
- | **Swagger / OpenAPI 3** | Documentação da API 

---

## Segurança e Autenticação (JWT)

A API utiliza **JWT com algoritmo HS256** para autenticação.

### Atenção
> A propriedade `jwt.secret` **deve possuir no mínimo 32 caracteres**.  
> Caso contrário, o contexto de segurança não será inicializado e a aplicação falhará ao subir.

## Comando para gerar a JWT Secret

### Especificações Técnicas:
- Tipo: Base64 padrão (RFC 4648)
- Codificação: Base64 resultando em 44 caracteres
- Tamanho: 32 bytes originais (256 bits)

### Usando Node.js
```
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

### Usando PowerShell (Windows)
```
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## Instalação e Execução

### Pré-requisitos

- Java 21
- PostgreSQL
- Maven (ou Maven Wrapper)
---

### Configuração do Banco de Dados

Crie uma base de dados no PostgreSQL e configure o arquivo (use como base o application.properties.example):

```properties
# src/main/resources/application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# JWT
jwt.secret=sua_chave_mestra_com_32_caracteres_ou_mais
```
---

## Compilar e Executar

Utilize o **Maven Wrapper** incluso no projeto:

```
./mvnw clean install
./mvnw spring-boot:run
```

### A aplicação estará disponível em:
```
http://localhost:sua_porta
server.port=porta_desejada
```
---

## Endpoints Implementados

### Autenticação

- | Método | Endpoint | Descrição |
- | POST | `/api/auth/registrar` | Registra um novo usuário |
- | POST | `/api/auth/obterToken` | Autentica e retorna o JWT |
- | POST | `/api/auth/logout` | Realiza o logout |
- | GET  | `/api/auth/me` | Obtem perfil do usuário |

---

### Máquinas Virtuais

- | Método | Endpoint | Descrição |
- | GET | `/api/vms/all` | Lista todas as VMs do sistema |
- | POST | `/api/vms` | Cadastra uma nova VM |
- | PUT | `/api/vms/{id}` | Atualiza uma VM |
- | DELETE | `/api/vms/{id}` | Remove uma VM |
- | POST | `/api/vms/paginas` | Lista VMs paginadas |
- | POST | `/api/vms/filtro` | Filtra VMs no sistema |
- | GET | `/api/vms` | Lista todas as VMs do usuário |
- | GET | `/api/vms/{id}` | Procura uma VM pelo ID |

---

### Usuários

- | Método | Endpoint | Descrição |
- | PUT | `/api/usuarios/me` | Atualiza meu próprio perfil |
- | POST | `/api/usuarios/paginas` | Listar com paginação e filtro |
- | POST | `/api/usuarios/filtro` | Filtrar usuários por critérios |
- | GET | `/api/usuarios` | Listar todos os usuários |
- | GET | `/api/usuarios/{id}` | Buscar usuário pelo ID |
- | DELETE | `/api/usuarios/{id}` | Buscar usuário pelo ID |


---

### Tarefas

- | GET | `/api/tarefas` | Listar historico de atividades|

###

---
## Requisitos Implementados


- CRUD completo de Máquinas Virtuais

- Controle de estados: START, STOP, SUSPEND

- Monitoramento de tarefas das VMs com cadastro de horário

- Dashboard interativo e dinâmico 

- Validações de negócio (CPU, memória e disco > 0)

- Limite de 5 VMs por usuário

- Autenticação stateless com JWT

- Validação de formato de E-mail

- Separação de responsabilidades (Controller, Service, Repository)


---

# Documentação da API

A documentação interativa da API pode ser acessada via Swagger:
```
http://localhost:url_do_projeto/swagger-ui.html
```
Nela é possível visualizar todos os endpoints, schemas de requisição e resposta.

---

# Escopo do Projeto
Este repositório contempla exclusivamente o backend da aplicação.