Banco Digital API / Projeto de Estudo

Este é um projeto de estudo onde desenvolvi uma API REST em Java com Spring Boot simulando um pequeno banco digital.

Aqui é possível:

cadastrar clientes

abrir contas bancárias

consultar informações

atualizar o status de uma conta

remover contas

listar todas as contas

projeto me ajudou a entender melhor como funciona uma API real, organizar camadas do backend, trabalhar com banco H2,
consumir serviços externos (ViaCEP) e ainda modernizar o ambiente usando Docker.


O que eu pratiquei com este projeto

Criar endpoints REST usando Spring Boot

Validar dados recebidos no backend

Consumir uma API externa para buscar CEP

Tratar erros e retornar mensagens de forma clara

Salvar as informações em um banco H2

Organizar a API em camadas (Controller, Service, Repository, DTO, Model)

Subir o projeto no Docker e rodar a API sem depender da configuração da máquina

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

O Docker permite:

Empacotar a API como uma imagem executável

Rodar a aplicação em qualquer ambiente sem configurar Java

Automatizar deploy em ambientes de nuvem (como EC2)

Garantir reprodutibilidade do ambiente

Facilitar CI/CD

A API funciona normalmente com o banco H2 mesmo dentro do Docker.

Instruções de como iniciar o projeto
Rodar localmente
mvn clean install
mvn spring-boot:run

 Acessos

API → http://localhost:8080/

H2 Console → http://localhost:8080/h2-console

 Persistência banco H2

O banco está configurado no modo file e os dados são salvos na pasta:

/data/banco-digital-db.mv.db

Configuração application.yml:
spring:
  datasource:
    url: jdbc:h2:file:./data/banco-digital-db;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver


OBS: DB_CLOSE_DELAY = -1 mantém o banco vivo durante toda a execução.

ENDPOINTS

 1. Criar cliente

POST /clientes

Body usado:
{
  "nomeCompleto": "Thainá Fonseca",
  "cpfCnpj": "1234567809",
  "email": "seuemail@aqui.com",
  "dataNascimento": "2000-01-01",
  "endereco": {
    "cep": "01001000"
  }
}

 Response 201 (sucesso)
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

 400 — CPF já cadastrado
{
  "error": "Bad Request",
  "message": "CPF/CNPJ já cadastrado"
}

 400 — CEP inválido
{
  "error": "Not Found",
  "message": "CEP inválido"
}

 2. Abrir conta

POST /contas

 Sucesso
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "TEMPORARIO",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

 Cliente não encontrado
{
  "error": "Not Found",
  "message": "Cliente não encontrado"
}

 Conta temporária já existente
{
  "error": "Bad Request",
  "message": "Cliente já possui conta temporária"
}

 Tipo de conta inválido
{
  "error": "Bad Request",
  "message": "Tipo de conta inválido. Use PF ou PJ"
}

 3. Consultar conta

GET /contas/{idConta}

 Sucesso
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "TEMPORARIO",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "dataAtualizacao": null,
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

 Conta não encontrada
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}

 4. Atualizar status da conta

PUT /contas/{idConta}/status?status=APROVADA

 Sucesso
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "APROVADA",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "dataAtualizacao": "2025-10-28T23:20:05.987Z",
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}

 Status inválido
{
  "error": "Bad Request",
  "message": "Status inválido. Use APROVADA, REPROVADA ou TEMPORARIO"
}

 5. Remover conta

DELETE /contas/{idConta}

204 No Content

(sem body)

 Conta não encontrada
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}

 6. Listar contas

GET /contas

 Sucesso
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

 BÔNUS / Próximos passos

Criar testes unitários (JUnit + Mockito)

Automatizar deploy com CI/CD

Subir a API na EC2

Migrar para Postgres/RDS

Criar documentação Swagger
