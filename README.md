Banco Digital API — Projeto de Estudo

Este é um projeto de estudo onde desenvolvi uma API REST em Java com Spring Boot simulando um pequeno banco digital.

Aqui é possível:

cadastrar clientes

abrir contas bancárias

consultar informações

atualizar o status de uma conta

remover contas

listar todas as contas

O projeto me ajudou a entender melhor como funciona uma API real, organizar camadas do backend, trabalhar com banco H2, consumir serviços externos (ViaCEP) e modernizar o ambiente usando Docker.

O que eu aprendi / pratiquei

Criar endpoints REST usando Spring Boot

Validar dados recebidos no backend

Consumir uma API externa para buscar CEP

Tratar erros e retornar mensagens de forma clara

Salvar as informações em um banco H2

Organizar a API em camadas (Controller, Service, Repository, DTO, Model)

Subir o projeto no Docker e rodar a API sem depender da configuração da máquina

Tecnologias

Java 21

Spring Boot 3

Spring Web

Spring Data JPA

H2 Database (file mode)

Spring Validation

Lombok

Docker

Integração ViaCEP

Validações

CPF/CNPJ: obrigatório e único

Email válido

Data de nascimento no formato ISO

CEP obrigatório

Tipo de conta deve ser PF ou PJ

Verificação de conta temporária no serviço de abertura

Regras de negócio

Cliente deve possuir CPF/CNPJ único

CEP é validado via API ViaCEP

Endereço é preenchido automaticamente

Ao abrir conta:

saldo inicial é 0

status inicial é TEMPORARIO

um cliente não pode ter mais de uma conta TEMPORARIO

Docker no projeto

Empacota a API como uma imagem executável

Roda em qualquer ambiente sem configurar Java

Facilita deploy em nuvem (ex.: EC2)

Garante reprodutibilidade do ambiente

Ajuda em CI/CD

A API funciona com H2 mesmo dentro do Docker.

Como executar local
mvn clean install
mvn spring-boot:run

Acessos

API: http://localhost:8080/

H2 Console: http://localhost:8080/h2-console

Persistência H2

Arquivo salvo em:

./data/banco-digital-db.mv.db


application.yml:

spring:
  datasource:
    url: jdbc:h2:file:./data/banco-digital-db;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver


Observação: DB_CLOSE_DELAY=-1 mantém o banco ativo durante a execução para facilitar testes.

Endpoints
1) Criar cliente

POST /clientes

Body
{
  "nomeCompleto": "Thainá Fonseca",
  
  "cpfCnpj": "1234567809",
  
  "email": "seuemail@aqui.com",
  
  "dataNascimento": "2000-01-01",
  
  "endereco": {
    "cep": "01001000"
  }
}

201 Created
{
  "id": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c",
  
  "nomeCompleto": "Thainá Fonseca",
  
  "cpfCnpj": "12345678900",
  
  "email": "seuemail@aqui.com",
  
  "dataNascimento": "2000-01-01",
  
  "endereco": {
    "cep": "08250-530",
    "logradouro": "Itaquera",
    "bairro": "JB",
    "localidade": "São Paulo",
    "uf": "SP"
  }
}

400 Bad Request (CPF/CNPJ já cadastrado)
{
  "error": "Bad Request",
  
  "message": "CPF/CNPJ já cadastrado"
}

400 Bad Request (CEP inválido)
{
  "error": "Not Found",
  
  "message": "CEP inválido"
}

2) Abrir conta

POST /contas

201 Created
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  
  "tipoConta": "PF",
  
  "status": "TEMPORARIO",
  
  "saldo": 0.0,
  
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

404 Not Found (cliente não existe)
{
  "error": "Not Found",
  
  "message": "Cliente não encontrado"
}

400 Bad Request (já possui conta temporária)
{
  "error": "Bad Request",
  
  "message": "Cliente já possui conta temporária"
}

400 Bad Request (tipo inválido)
{
  "error": "Bad Request",
  
  "message": "Tipo de conta inválido. Use PF ou PJ"
}

3) Consultar conta

GET /contas/{idConta}

200 OK
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  
  "tipoConta": "PF",
  
  "status": "TEMPORARIO",
  
  "saldo": 0.0,
  
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  
  "dataAtualizacao": null,
  
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

404 Not Found
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}

4) Atualizar status da conta

PUT /contas/{idConta}/status?status=APROVADA

200 OK
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  
  "tipoConta": "PF",
  
  "status": "APROVADA",
  
  "saldo": 0.0,
  
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  
  "dataAtualizacao": "2025-10-28T23:20:05.987Z",
  
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

400 Bad Request (status inválido)
{
  "error": "Bad Request",
  
  "message": "Status inválido. Use APROVADA, REPROVADA ou TEMPORARIO"
}

5) Remover conta

DELETE /contas/{idConta}

204 No Content

(sem body)

404 Not Found
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}

6) Listar contas

GET /contas

200 OK
[
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

Bônus / Próximos passos

Criar testes unitários (JUnit + Mockito)

Automatizar deploy com CI/CD

Subir a API em EC2

Migrar para Postgres/RDS

Criar documentação Swagger
