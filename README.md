# Sistema de votos de cooperativa

Projeto para voto de pautas em assembleia de cooperativas. É uma aplicação rest simples que consiste em alguns endpoints
básico para realizar tal tarefa.

## Importante

Usei a aplicação para refinar meus conhecimentos de arquitetura, afinal testes como esse são demorados então sempre os
aproveito como objeto de estudo. Logo acabei causando adicionando alguma complexidade.

## Execução local

Para rodar atender aos requisitos e executar `docker compose up` na raiz do projeto, aguardar iniciar e acessar [http://localhost:8080/swagger-ui/index.html#/pauta-controller/resultados](http://localhost:8080/swagger-ui/index.html#/pauta-controller/resultados).

### Requisitos

1. Criar token no site [invertext.com](invertext.com) para usar seu validador de CPF e armazenar ele localmente em uma
variável de ambiente chamada `INVERTEXT_TOKEN`:

  ![image](https://github.com/danielarrais/voting-system/assets/28496479/21087259-bb22-4367-99ca-2d129ffa3aea)

2. Ter o docker instalado

### Variáveis de ambiente

A aplicação tem várias váriáveis de ambiente para configurar recursos internos e externos:

#### RabbitMQ
* **STACKHERO_RABBITMQ_HOST**: Host do rabbit
* **STACKHERO_RABBITMQ_PASSWORD**: Senha do RabbitMQ
* **RABBITMQ_PORT**: Porta do RabbitMQ
* **RABBITMQ_USERNAME**: Usuário do RabbitMQ
* **RABBITMQ_VHOST**: Host virtual do RabbitMQ

#### MySQL
* **STACKHERO_MYSQL_HOST**: Host do mysql
* **STACKHERO_MYSQL_PORT**: Porta do mysql
* **STACKHERO_MYSQL_USER**: Usuário do mysql
* **STACKHERO_MYSQL_ROOT_PASSWORD**: Senha do mysql

#### API de validação de CPF
* **INVERTEXT_TOKEN**: Token de acesso do invertext.com

#### Aplicação
* **SESSION_DURATION**: Duração padrão da sessão de votação
* **PORT**: Porta que a aplicação deve utilizar

## Acesso em cloud

Você pode acessar a aplicação online para testes
neste [link](https://voting-service-1e6c6ca48389.herokuapp.com/swagger-ui/index.html#/pauta-controller/resultados).

## Endpoints

* **GET /pautas**: retorna pautas cadastradas
* **POST /pautas**: cadastra pauta. Abaixo exemplo de body da requisição:
  ```json
  {
     "titulo": "Porcentagem de lucros",
     "descricao": "Diz respeito ao aumento de 1% na faixa de lucro distribuído"
  }
  ```
* **POST /pautas/{pautaId}/votos**: cadastra pauta. Abaixo exemplo de body da requisição:
  ```json
  {
     "cpf": "06412721380",
     "voto": false
  }
  ```
* **POST /pautas/{pautaId}/sessoes**: abre sessão de votação.
* **GET /pautas/{pautaId}/resultados**: retorna resultados da votação

## Arquitetura

Eu utilizei arquitetura hexagonal para organizar e desacoplar as camadas da minha aplicação. Criei as seguintes camadas
e estrutura:

```shell
├── api # Camada que expõe os endpoints
├── core 
│ ├── application # Camada com os use cases da aplicação
│ └── domain # Camada com o domínio
└── infra # Camada de acesso a recursos externos
```

![Diagrama sem nome drawio](https://github.com/danielarrais/voting-system/assets/28496479/1516e586-e998-41d2-b8c8-51c6ac6a502a)

## Recursos externos

* **Mysql**: Banco de dados utilizado para armazenar os pautas, sessões, votos e resultados
* **Invertext.com**: API utilizada para validar CPF
* **RabbitMQ**: Mensageria utilizada para publicar os resultados para outros clientes

## Melhorias

* Implementar restante dos endpoints;
* Salvar os resultados em um banco de dado com mais disponibilidades, tipo o mongodb, para disponibilizar os resultados
  de forma mais performática;
* Utilizar mais recursos do swagger para detalhar melhor o body e outras informações dos endpoints;
