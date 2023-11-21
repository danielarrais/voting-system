# Sistema de votos de cooperativa

Projeto para voto de pautas em assembleia de cooperativas. É uma aplicação rest simples que consiste em alguns endpoints
básico para realizar tal recurso.

## Importante

Usei a aplicação para refinar meus conhecimentos de arquitetura, afinal testes como esse são demorados então sempre os
aproveito como objeto de estudo. Logo acabei causando adicionando alguma complexidade.

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

