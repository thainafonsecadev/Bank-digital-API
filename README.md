Banco Digital API

Este é um projeto de estudo onde desenvolvi uma API REST em Java com Spring Boot simulando um pequeno banco digital.

Aqui é possível:

- Cadastrar clientes  
- Abrir contas bancárias  
- Consultar informações  
- Atualizar o status de uma conta  
- Remover contas  
- Listar todas as contas  

O projeto me ajudou a entender melhor como funciona uma API real, organizar camadas do backend, trabalhar com banco H2, consumir serviços externos (ViaCEP) e modernizar o ambiente usando Docker.

---

## O que foi praticado neste projeto

- Criação de endpoints REST usando Spring Boot  
- Validação de dados recebidos no backend  
- Consumo de API externa para busca de CEP  
- Tratamento de erros e retorno de mensagens claras  
- Persistência de dados em banco H2  
- Organização da aplicação em camadas (Controller, Service, Repository, DTO, Model)  
- Execução do projeto em container Docker  

---

## Tecnologias utilizadas

- Java 21  
- Spring Boot 3  
- Spring Web  
- Spring Data JPA  
- H2 Database (file mode)  
- Spring Validation  
- Lombok  
- Docker  
- ViaCEP API  

---

## Validações

- CPF/CNPJ: obrigatório e único  
- E-mail válido  
- Data de nascimento no formato ISO  
- CEP obrigatório  
- Tipo de conta deve ser PF ou PJ  
- Verificação de conta temporária no serviço de abertura  

---

## Regras de negócio

- Cliente deve possuir CPF/CNPJ único  
- CEP é validado via API ViaCEP  
- Endereço é preenchido automaticamente  
- Ao abrir conta:
  - saldo inicial é 0  
  - status inicial é TEMPORARIO  
  - um cliente não pode ter mais de uma conta TEMPORARIO  

---

## Sobre o Docker neste projeto

O Docker permite:

- Empacotar a API como uma imagem executável  
- Rodar a aplicação em qualquer ambiente sem precisar configurar o Java localmente  
- Automatizar o deploy em ambientes de nuvem (como AWS EC2)  
- Garantir reprodutibilidade e consistência do ambiente  
- Facilitar pipelines de CI/CD  

A API funciona normalmente com o banco H2 mesmo dentro do container Docker.

---

## Instruções de como iniciar o projeto

Rodar localmente (em sua IDE de preferência):

bash
mvn clean install
mvn spring-boot:run


---

## Acessos

API → [http://localhost:8080/](http://localhost:8080/)  
H2 console → [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## Persistência banco H2

O banco está configurado no modo file, e todos os dados são salvos na pasta:


/data/banco-digital-db.mv.db


### Configuração application.yml

yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/banco-digital-db;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver


OBS: DB_CLOSE_DELAY=-1 mantém o banco ativo durante toda a execução.

---

# Iniciando ENDPOINTS

## 1. Criar Cliente - POST /clientes

### Body utilizado:

json
{
  "nomeCompleto": "",
  "cpfCnpj": "",
  "email": "",
  "dataNascimento": "",
  "endereco": {
    "cep": ""
  }
}


### Response 201 Created SUCESSO

json
{
  "id": "5cd272df-afb5-4128-90bd-579b94467c0f",
  
  "nomeCompleto": "Manoel Fonseca",
  
  "cpfCnpj": "312.363.281-23",
  
  "email": "manoelbg@hotmail.com",
  
  "dataNascimento": "04/01/1999",
  
  "endereco": {
    "cep": "08295-530",
    
    "logradouro": "Rua Félix Capella",
    
    "bairro": "Conjunto Residencial José Bonifácio",
    
    "localidade": "São Paulo",
    
    "uf": "SP"
  }
}


### Response ERRO / 400 BAD REQUEST

json
{
  "error": "Bad Request",
  "message": "CPF/CNPJ já cadastrado"
}


### Response ERRO / 400 CEP INVÁLIDO

json
{
  "error": "Not Found",
  "message": "CEP inválido"
}


---

## 2. Abrir Conta - POST /contas

### Response 201 Created SUCESSO

json
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "TEMPORARIO",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}


### Response ERRO / 404 CLIENTE NÃO ENCONTRADO

json
{
  "error": "Not Found",
  "message": "Cliente não encontrado"
}


### Response ERRO / 400 CONTA TEMPORÁRIA EXISTENTE

json
{
  "error": "Bad Request",
  "message": "Cliente já possui conta temporária"
}


### Response ERRO / 400 TIPO DE CONTA INVÁLIDO

json
{
  "error": "Bad Request",
  "message": "Tipo de conta inválido. Use PF ou PJ"
}


---

## 3. Consultar Conta - GET /contas/{idConta}

### Response 200 OK SUCESSO

json
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "TEMPORARIO",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "dataAtualizacao": null,
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}


### Response ERRO / 404 CONTA NÃO ENCONTRADA

json
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}


---

## 4. Atualizar Status da Conta - PUT /contas/{idConta}/status?status=APROVADA

### Response 200 OK SUCESSO

json
{
  "idConta": "9a66386f-1d73-4fa9-9e07-902c67a6c1d2",
  "tipoConta": "PF",
  "status": "APROVADA",
  "saldo": 0.0,
  "dataCriacao": "2025-10-28T23:10:21.123Z",
  "dataAtualizacao": "2025-10-28T23:20:05.987Z",
  "clienteId": "b4c0d95e-5cb2-489a-8857-13ae5cb2b65c"
}


### Response ERRO / 400 STATUS INVÁLIDO

json
{
  "error": "Bad Request",
  "message": "Status inválido. Use APROVADA, REPROVADA ou TEMPORARIO"
}


---

## 5. Remover Conta - DELETE /contas/{idConta}

### Response 204 NO CONTENT (sem body)

### Response ERRO / 404 CONTA NÃO ENCONTRADA

json
{
  "error": "Not Found",
  "message": "Conta não encontrada"
}


---

## 6. Listar Contas - GET /contas

### Response 200 OK SUCESSO

json
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


---

## BÔNUS / Próximos Passos

- Criar testes unitários (JUnit + Mockito)  
- Automatizar deploy com CI/CD  
- Subir a API em uma instância EC2 na AWS  
- Migrar o banco para PostgreSQL / RDS  
- Criar documentação com Swagger  
"""
