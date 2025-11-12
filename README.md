
Bank Digital API — Projeto de Estudo

Este é um projeto de estudo onde desenvolvi uma API REST em Java com Spring Boot simulando um pequeno banco digital.

Aqui foi desenvolvido para os seguintes serviços :

cadastrar clientes

abrir contas bancárias

consultar informações

atualizar o status de uma conta

remover contas

listar todas as contas

O projeto me ajudou a entender melhor como funciona uma API real, organizar camadas do backend, trabalhar com banco H2, 
consumir serviços externos (ViaCEP) e ainda modernizar o ambiente usando Docker.


O que eu aprendi / pratiquei ao logo do desenvolvimento com este projeto

Criar endpoints REST usando Spring Boot

Validar dados recebidos no backend

Consumir uma API externa para buscar CEP

Tratar erros e retornar mensagens de forma clara

Salvar as informações em um banco H2

Organizar a API em camadas (Controller, Service, Repository, DTO, Model)

Subir o projeto no Docker e rodar a API sem depender da configuração da máquina


Aqui foi usada as seguintes tecnologias 

Java 21

Spring Boot 3

Spring Web

Spring Data JPA

H2 Database (file mode)

Spring Validation

Lombok

Docker

Integração ViaCEP

Com as seguintes validações 

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

Rodar localmente (sua IDE de preferência):

comandos -> mvn clean install -  mvn spring-boot:run
   
Acessos

API → http://localhost:8080/

H2 console → http://localhost:8080/h2-console


Persistência banco H2

O banco está configurado no modo file, e todos os dados são salvos na pasta:

/data/banco-digital-db.mv.db


Configuração application.yml

spring:
  datasource:
    url: jdbc:h2:file:./data/banco-digital-db;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver

OBS : DB_CLOSE_DELAY=-1 mantém o banco ativo durante toda a execução.


------

Iniciando ENDPOINTS


1. Criar Cliente 
POST /clientes

{
  "nomeCompleto": "",
  "cpfCnpj": "",
  "email": "",
  "dataNascimento": "",
  "endereco": {
    "cep": ""
  }
}

Response 201 Created SUCESSO

{
  "id": "5cd272df-afb5-4128-90bd-579b94467c0f",
  "nomeCompleto": "Manoel Fonseca",
  "cpfCnpj": "312.363.281-09",
  "email": "manoelbg@hotmail.com",
  "dataNascimento": "04/01/1999",
  "endereco": {
    "cep": "08295-539",
    "logradouro": "Rua FxX",
    "bairro": "jb",
    "localidade": "São Paulo",
    "uf": "SP"
  }
}







