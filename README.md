# Todo App usando Spring Boot
Este repositório contém o desafio de projeto _Explorando Padrões de Projetos, na Prática com Java_
do bootcamp **GFT Start #5 Java** da [DIO](https://web.dio.me/).

A aplicação representa uma _API Rest_ que permite criar uma lista de tarefas
a fazer. Cada tarefa pode ser criada por um usuário e não é permitido multiplos
usuários na mesma atividade.

## Arquitetura do Sistema

O sistema está dividido em ``main`` e `test`, onde em `test` estão os testes unitários e
alguns testes de integração.

Os pacotes dentro de `main.java` representam:
- ``Config``: Contém arquivos de configuração de execução do sistema, como a documentação;
- ``Controller``: Contém o mapeamento das rotas para a execução dos serviços;
- ``Entity``: Contém todas as entidades utilizadas no programa;
- ``Exceptions``: Contém todas as exceções que podem ser lançadas pelo programa;
- ``Repository``: Contém os repositórios CRUD que são utilizados;
- ``Service``: Contém as regras de negócio da aplicação;
- ``Specification``: Contém especificações para a busca nos repositórios;
- ``Utils``: Contém algumas funções utilizadas constantemente.

Os pacotes dentro de ``main.resources`` contém algumas configurações para a execução do Spring Boot.

