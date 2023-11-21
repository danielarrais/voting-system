# Sistema de votos de cooperativa

Projeto para voto de pautas em assembleia de cooperativas. É uma aplicação rest simples que consiste em alguns endpoints
básico para realizar tal tarefa.

## Importante

Usei a aplicação para refinar meus conhecimentos de arquitetura, afinal testes como esse são demorados então sempre os
aproveito como objeto de estudo. Logo acabei causando adicionando alguma complexidade.

## Requisitos para execução

* Criar token no site [invertext.com]() para usar seu validador de CPF e armazenar ele localmente em uma variável de ambiente chamada `INVERTEXT_TOKEN`:

    ![image](https://github.com/danielarrais/voting-system/assets/28496479/21087259-bb22-4367-99ca-2d129ffa3aea)
  
* Ter o docker instalado

## Execução

Para rodar a aplicação localmente basta executar `docker compose up` na raiz do projeto

## Acesso Local

Para testar essa aplicação localmente acesse (http://localhost:8080/swagger-ui/index.html#/pauta-controller/resultados)[http://localhost:8080/swagger-ui/index.html#/pauta-controller/resultados]

## Acesso em cloud

Você pode acessar a aplicação online para testes neste (link)[https://voting-service-1e6c6ca48389.herokuapp.com/swagger-ui/index.html#/pauta-controller/resultados].

## Arquitetura

Eu utilizei arquitetura hexagonal para organizar e desacoplar as camadas da minha aplicação. Criei as seguintes camadas e
estrutura:

```shell
├── api # Camada que expõe os endpoints
├── core 
│ ├── application # Camada com os use cases da aplicação
│ └── domain # Camada com o domínio
└── infra # Camada de acesso a recursos externos
```

![Diagrama sem nome drawio](https://github.com/danielarrais/voting-system/assets/28496479/1516e586-e998-41d2-b8c8-51c6ac6a502a)
