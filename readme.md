Claro! Aqui está o texto formatado em Markdown:


# System Places Manage

Este projeto tem como objetivo implementar uma API com CRUD completo para cadastrar lugares com Bairros (Districts), Cidade(City) e Estado(State). As principais funcionalidades incluem:
- Criar
- Atualizar
- Deletar
- Listar por ordem de data de criação

Para garantir a funcionalidade da API, foram implementados testes unitários. Além disso, o Swagger foi configurado para visualizar e testar todos os endpoints em uma interface amigável.

Alguns testes de falha para as entidades cidade, Bairro e Estado foram omitidas, pois o autor entendeu que os testes unitários de Lugares já cobre todos as funcionalidades requiridas do sistema.

## Principais Pacotes e Dependências

- **Spring Boot Starter Data JPA**: Para integração com bancos de dados relacionais.
- **Spring Boot Starter Validation**: Para validação de dados.
- **Spring Boot Starter Web**: Para construir aplicações web RESTful.
- **Spring Boot Starter HATEOAS**: Para adicionar links aos recursos REST.
- **PostgreSQL**: Banco de dados relacional utilizado.
- **JDK 21**: Versão do Java Development Kit.
- **SpringDoc OpenAPI Starter WebMVC UI**: Para documentação e interface de teste da API.
- **Lombok**: Para reduzir o código boilerplate.
- **Spring Boot Starter Test**: Para testes unitários e de integração.
- **H2 Database**: Banco de dados em memória para testes.
- **Spring Boot Starter WebFlux**: Para suporte a programação reativa.
- **Docker e Docker-compose**


## Arquitetura do Sistema
Foi utilizado Clean code para definir os componentes e seus limites. A seguinte imagem descreve como funciona a 
relação entre os componentes do sistema e suas responsabilidades.


![Imagem da arquitetura do sistema](/aquitetura.png)

### Application
Esta camada contém a lógica de aplicação do sistema, incluindo DTOs (Data Transfer Objects) para as requisições 
, respostas para padronização das respostas do sistema e controladores.

* **dtos**: Contém classes que representam os objetos de transferência de dados entre as requisições.
* **responses**: Classes que representam as respostas padronizadas do sistema, fornecendo uma interface consistente para os clientes.
* **controllers**: Controladores que definem os endpoints da API e gerenciam a lógica de apresentação dos recursos.
swagger: Configurações relacionadas ao Swagger, utilizado para documentar a API.

### Domain
Esta camada encapsula a lógica de negócios do sistema, incluindo entidades que representam os objetos de domínio, 
exceções específicas do domínio, interfaces de repositório para acesso aos dados e serviços que implementam a lógica de negócios.

* **entity**: Classes que representam as entidades do domínio, contendo os atributos e comportamentos essenciais.
* **exception**: Exceções específicas do domínio, utilizadas para sinalizar erros ou situações excepcionais durante a execução do sistema.
* **repository**: Interfaces que definem os contratos para acesso aos dados, permitindo a separação entre a lógica de negócios e o acesso aos dados.
* **service**: Classes que implementam a lógica de negócios do sistema, coordenando as operações entre as entidades e os repositório

### Infra
Esta camada fornece suporte técnico para as camadas superiores, incluindo utilitários diversos e tratamento de exceções genéricas.

* **utils**: Classes utilitárias que fornecem funcionalidades genéricas e reutilizáveis em todo o sistema.
* **handleController**: Tratamento de exceções genéricas, como exceções de infraestrutura, validação de dados e erros inesperados.

## Como Rodar a Aplicação

As seguintes instruções são para usuários de Linux ou Windows com WSL.

### Pré-requisitos

1.**Instalar JDK 21**: Siga as intruções na [Documentação Oficial](https://www.oracle.com/java/technologies/downloads/)

2. **Instalar Maven**:
   ```bash
   sudo apt-get install maven
   ```

2. **Instalar Docker e Docker Compose**:
   Siga as instruções na [Documentação Oficial](https://docs.docker.com/desktop/install/linux-install/).

### Passos para Rodar a Aplicação
Todos os comandos são executados na raiz do projeto


1. **Faça o build da aplicação**:
   ```bash
   mvn package
   ```

2. **Crie a imagem Docker e suba o container**:
   ```bash
   sudo docker build -t nuvem/system_place_manager:latest .
   sudo docker-compose up -d
   ```

### Acessando a Aplicação

Após subir o container, a aplicação estará disponível em `http://localhost:8080`. Você pode acessar a documentação do Swagger em `http://localhost:8080/swagger-ui.html` para visualizar e testar os endpoints.

### Realizando os testes

Execute o seguinte comando dentro da pasta do projeto

```bash
   mvn test
```

## Contribuição

Para contribuir com este projeto, siga os passos abaixo:

1. Faça um fork do repositório.
2. Crie uma nova branch:
   ```bash
   git checkout -b minha-nova-feature
   ```
3. Faça suas alterações e commit:
   ```bash
   git commit -m 'Adiciona nova feature'
   ```
4. Envie para o repositório remoto:
   ```bash
   git push origin minha-nova-feature
   ```
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.


Se precisar de mais alguma coisa ou tiver outras dúvidas, estou aqui para ajudar!