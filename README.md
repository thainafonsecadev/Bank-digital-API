Bank Digital API

Este é um projeto de estudo onde desenvolvi uma API REST em Java com Spring Boot simulando um pequeno banco digital.

Aqui é possível:

cadastrar clientes

abrir contas bancárias

consultar informações

atualizar o status de uma conta

remover contas

listar todas as contas

-------

O projeto me ajudou a entender melhor como funciona uma API real, organizar camadas do backend, trabalhar com banco H2, consumir serviços externos (ViaCEP)
e ainda modernizar o ambiente usando Docker.

O que eu aprendi / pratiquei com este projeto

Criar endpoints REST usando Spring Boot

Validar dados recebidos no backend

Consumir uma API externa para buscar CEP

Tratar erros e retornar mensagens de forma clara

Salvar as informações em um banco H2

Organizar a API em camadas (Controller, Service, Repository, DTO, Model)

Subir o projeto no Docker e rodar a API sem depender da configuração da máquina

----

Tecnologias Usadas

Java 21

Spring Boot 3

Spring Web

Spring Data JPA

H2 Database (file mode)

Spring Validation

Lombok

Docker

Integração ViaCEP

---

Validações

CPF/CNPJ: obrigatório e único

Email válido

Data de nascimento no formato ISO

CEP obrigatório

Tipo de conta deve ser PF ou PJ

Verificação de conta temporária no serviço de abertura

Regras de Negócio

Cliente deve possuir CPF/CNPJ único

CEP é validado via API ViaCEP

Endereço é preenchido automaticamente

Ao abrir conta:

saldo inicial é 0

status inicial é TEMPORARIO

um cliente não pode ter mais de uma conta TEMPORARIO

Sobre o Docker neste projeto

---

O Docker permite:

Empacotar a API como uma imagem executável

Rodar a aplicação em qualquer ambiente sem configurar Java

Automatizar deploy em ambientes de nuvem (como EC2)

Garantir reprodutibilidade do ambiente

Facilitar CI/CD

A API funciona normalmente com o banco H2 mesmo dentro do Docker.

---

Instruções de como iniciar o projeto

Rodar localmente (sua IDE de preferência):

mvn clean install
mvn spring-boot:run


Acessos:
API → http://localhost:8080/

H2 console → http://localhost:8080/h2-console

Persistência banco H2:
O banco está configurado no modo file, e todos os dados são salvos na pasta:

/data/banco-digital-db.mv.db


Configuração application.yml:

spring:
  datasource:
    url: jdbc:h2:file:./data/banco-digital-db;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver


OBS: DB_CLOSE_DELAY=-1 mantém o banco ativo durante toda a execução.

---

ENDPOINTS
1. Criar Cliente

POST /clientes

Body utilizado:
```
{
  "nomeCompleto": "Manoel",
  "cpfCnpj": "312.363.281-09",
  "email": "manoellb@hotmail.com",
  "dataNascimento": 04/01/1999"",
  "endereco": {
    "cep": "08295-539"
  }
}

```
Response 201 Created SUCESSO
```
{
  "id": "5cd272df-afb5-4128-90bd-579b94467c0f",
  "nomeCompleto": "Manoel Fonseca",
  "cpfCnpj": "312.363.281-09",
  "email": "manoelbg@hotmail.com",
  "dataNascimento": "04/01/1999",
  "endereco": {
    "cep": "08295-539",
    "logradouro": "Rua FX",
    "bairro": "Conjunto H",
    "localidade": "São Paulo",
    "uf": "SP"
  }
}

```
Response ERRO 400 - CPF/CNPJ já cadastrado
```
{
  "error": "Bad Request",
  "message": "CPF/CNPJ já cadastrado"
}

```
Response ERRO 400 - CEP inválido
```
{
  "error": "Not Found",
  "message": "CEP inválido"
}
```
2. Abrir Conta

POST /contas

Response 201 Created SUCESSO
```
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "TEMPORARIO",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

```
Response ERRO 404 - Cliente não encontrado
```
{
  "error": "Not Found",
  "message": "Cliente não encontrado"
}
```

Response ERRO 400 - Cliente já possui conta temporária
```
{
  "error": "Bad Request",
  "message": "Cliente já possui conta temporária"
}
```

Response ERRO 400 - Tipo de conta inválido
```
{
  "error": "Bad Request",
  "message": "Tipo de conta inválido. Use PF ou PJ"
}
```
3. Consultar Conta

GET /contas/{idConta}

Response 200 OK
```
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "TEMPORARIO",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "dataAtualizacao": null,
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}
```

Response ERRO 404 - Conta não encontrada
```
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}
```
4. Atualizar Status da Conta

PUT /contas/{idConta}/status?status=APROVADA

Response 200 OK
```
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "APROVADA",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "dataAtualizacao": "2025-10-28T23:20:05.987Z",
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}
```

Response ERRO 400 - Status inválido
```
{
  "error": "Bad Request",
  "message": "Status inválido. Use APROVADA, REPROVADA ou TEMPORARIO"
}
```
5. Remover Conta

DELETE /contas/{idConta}

Response 204 No Content
(sem body)

Response ERRO 404 - Conta não encontrada
```
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}
```
6. Listar Contas

GET /contas

Response 200 OK

[```
  {
    "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
    "tipoConta": "PF",
    "status": "TEMPORARIO",
    "saldo": 0.0,
    "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
  },
  {
    "idConta": "c0b255e9-1fd2-470e-bd93-0d62adcef2d1",
    "tipoConta": "PJ",
    "status": "APROVADA",
    "saldo": 0.0,
    "clienteId": "a993728f-a12b-4fae-a587-3c1df7435d19"
  }
]
```
```

Projeto esta desenvolvido em um formato que posso futuramente explorar mais alguns serviços voltados para o Back end

Criar testes unitários (JUnit + Mockito)

Automatizar deploy com CI/CD

Subir a API em uma instância EC2 na AWS

Migrar o banco para PostgreSQL / RDS
