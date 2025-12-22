# Sistema de Gestão de Clientes e Pedidos

Sistema web para gestão de clientes e pedidos desenvolvido como parte da **Prova Técnica - Engenheiro de Software**. A aplicação foi desenvolvida utilizando Java, JPA/Hibernate, JSF e PrimeFaces, seguindo boas práticas de desenvolvimento e arquitetura em camadas.

## 📋 Sobre o Projeto

Este projeto foi desenvolvido para atender aos requisitos de uma prova técnica que avalia a capacidade de modelar, desenvolver e organizar código limpo, funcional e bem estruturado, utilizando tecnologias Java no contexto de uma aplicação web corporativa.

### Objetivo
Desenvolver uma aplicação web para gestão de clientes e pedidos, onde é possível:
- ✅ Cadastrar clientes
- ✅ Cadastrar pedidos vinculados a um cliente
- ✅ Listar, editar e remover clientes
- ✅ Listar, editar e remover pedidos
- ✅ Filtrar pedidos por cliente

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 8** - Linguagem de programação
- **Maven 3.6+** - Gerenciamento de dependências e build
- **JPA / Hibernate 5.4.32** - ORM para persistência de dados
- **CDI (Contexts and Dependency Injection)** - Injeção de dependências
- **Bean Validation** - Validação de dados
- **Java Util Logging (JUL)** - Sistema de logs estruturado

### Frontend
- **JSF 2.3** - Framework web baseado em componentes
- **PrimeFaces 8.0** - Biblioteca de componentes UI
- **HTML5 / CSS3** - Estrutura e estilização

### Banco de Dados
- **PostgreSQL 14** - Banco de dados relacional
- **H2 Database** - Banco em memória para testes (console integrado)

### Servidor de Aplicação
- **WildFly 20.0.1.Final** - Servidor de aplicação Java EE 8

### IDE e Ferramentas
- **Eclipse IDE** - Ambiente de desenvolvimento integrado
- **Docker** - Containerização do banco de dados

## 📦 Requisitos e Downloads

### Pré-requisitos

Antes de executar o projeto, você precisará instalar as seguintes ferramentas:

| Ferramenta | Versão | Link de Download |
|------------|--------|-------------------|
| **Java JDK** | 8 ou superior | [Oracle JDK 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html) ou [OpenJDK 8](https://adoptium.net/temurin/releases/?version=8) |
| **Maven** | 3.6+ | [Apache Maven](https://maven.apache.org/download.cgi) |
| **WildFly** | 20.0.1.Final | [WildFly 20.0.1.Final](https://www.wildfly.org/downloads/) |
| **PostgreSQL** | 14+ | [PostgreSQL Downloads](https://www.postgresql.org/download/) |
| **Docker** (opcional) | Latest | [Docker Desktop](https://www.docker.com/products/docker-desktop) |
| **Eclipse IDE** (opcional) | Latest | [Eclipse IDE for Enterprise Java](https://www.eclipse.org/downloads/packages/) |

### Instalação Rápida

1. **Java 8**: 
   - Baixe e instale o JDK 8
   - Configure a variável de ambiente `JAVA_HOME`
   - Verifique: `java -version`

2. **Maven**:
   - Baixe e extraia o Maven
   - Adicione ao `PATH`
   - Verifique: `mvn -version`

3. **WildFly 20**:
   - Baixe o WildFly 20.0.1.Final
   - Extraia em um diretório (ex: `C:\wildfly-20.0.1.Final`)
   - Configure `JAVA_HOME` se necessário

4. **Docker** (para PostgreSQL):
   - Instale o Docker Desktop
   - Verifique: `docker --version`

## 🚀 Como Executar o Projeto

### 1. Configuração do Ambiente

#### 1.1. Configurar WildFly no Eclipse

Este projeto foi desenvolvido utilizando **Eclipse IDE** com os seguintes passos:

1. **Instalar WildFly no Eclipse**:
   - Abra o Eclipse
   - Vá em `Help` → `Eclipse Marketplace`
   - Procure por "JBoss Tools" ou "WildFly"
   - Instale o plugin do WildFly

2. **Adicionar Servidor WildFly**:
   - Vá em `Window` → `Preferences` → `Server` → `Runtime Environments`
   - Clique em `Add`
   - Selecione "WildFly 20.x"
   - Configure o caminho do WildFly (ex: `C:\wildfly-20.0.1.Final`)
   - Clique em `Finish`

3. **Criar Usuário de Administração**:
   - **IMPORTANTE**: Antes de iniciar o WildFly, você DEVE criar um usuário administrativo
   - Navegue até a pasta `bin` do WildFly
   - Execute o script `add-user.bat` (Windows) ou `add-user.sh` (Linux/Mac)
   - Escolha a opção para criar um usuário de **Management Realm**
   - Defina username e password (ex: `admin` / `admin123`)
   - Confirme as opções
   - **Sem este usuário, o console de administração não funcionará!**

#### 1.2. Configurar PostgreSQL

**Opção A: Usando Docker (Recomendado)**

```bash
# Iniciar PostgreSQL via Docker Compose
docker-compose up -d postgres

# Verificar se está rodando
docker ps
```

**Opção B: PostgreSQL Local**

1. Instale o PostgreSQL
2. Crie o banco de dados:
```sql
CREATE DATABASE projeto;
CREATE USER admin WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE projeto TO admin;
```

### 2. Configuração do Datasource no WildFly

#### 2.1. Adicionar Driver PostgreSQL

1. Baixe o driver PostgreSQL JDBC: [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/download/)
2. Crie a estrutura de módulo no WildFly:
   ```
   wildfly-20.0.1.Final/
   └── modules/
       └── org/
           └── postgresql/
               └── main/
                   ├── postgresql-42.7.1.jar
                   └── module.xml
   ```

3. Crie o arquivo `module.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.7.1.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

<img width="357" height="349" alt="Image" src="https://github.com/user-attachments/assets/459b9de5-2071-4630-85a3-199260436751" />

#### 2.2. Configurar Datasource no standalone.xml

Edite o arquivo `wildfly-20.0.1.Final/standalone/configuration/standalone.xml` e adicione/configure o datasource:

```xml
<subsystem xmlns="urn:jboss:domain:datasources:5.0">
    <datasources>
        <!-- Datasource do Projeto -->
        <datasource jndi-name="java:/jdbc/ProjetoDS" 
                    pool-name="ProjetoDS" 
                    enabled="true" 
                    use-java-context="true" 
                    statistics-enabled="false">
            <connection-url>jdbc:postgresql://localhost:5432/projeto?autoReconnect=true</connection-url>
            <driver>postgresql</driver>
            <security>
                <user-name>admin</user-name>
                <password>password</password>
            </security>
            <pool>
                <min-pool-size>5</min-pool-size>
                <max-pool-size>20</max-pool-size>
            </pool>
        </datasource>
        
        <!-- Driver PostgreSQL -->
        <drivers>
            <driver name="postgresql" module="org.postgresql">
                <driver-class>org.postgresql.Driver</driver-class>
            </driver>
        </drivers>
    </datasources>
</subsystem>
```

**⚠️ Observações Importantes:**
- O atributo `statistics-enabled` deve ser `false` (não use expressões como `${...}`)
- O `driver` deve referenciar o módulo criado (`org.postgresql`)
- Verifique se o `deployment-scanner` também tem `runtime-failure-causes-rollback="false"` (valor booleano, não expressão)

### 3. Compilar e Fazer Deploy

#### 3.1. Compilar o Projeto

```bash
# Navegue até a pasta do projeto
cd projeto

# Compilar o projeto
mvn clean package

# O arquivo WAR será gerado em: target/gestao-clientes-pedidos.war
```

#### 3.2. Deploy no WildFly

**Opção A: Deploy Automático (Recomendado)**

1. Copie o arquivo `target/gestao-clientes-pedidos.war` para:
   ```
   wildfly-20.0.1.Final/standalone/deployments/
   ```

2. O WildFly detectará automaticamente e fará o deploy

**Opção B: Deploy via Console Web**

1. Inicie o WildFly
2. Acesse o console de administração: http://localhost:9990
3. Faça login com o usuário criado via `add-user`
4. Vá em `Deployments` → `Add` → Selecione o arquivo WAR
5. Clique em `Finish`

**Opção C: Deploy via Eclipse**

1. Clique com botão direito no projeto
2. Selecione `Run As` → `Run on Server`
3. Escolha o servidor WildFly configurado
4. O Eclipse fará o deploy automaticamente

### 4. Acessar a Aplicação

<img width="1248" height="898" alt="Image" src="https://github.com/user-attachments/assets/80396d63-80e0-4bda-bb08-1616f71be0e8" />

Após o deploy bem-sucedido, acesse as seguintes URLs:

| Página | URL |
|--------|-----|
| **Home** | http://localhost:8080/gestao-clientes-pedidos/ |
| **Clientes** | http://localhost:8080/gestao-clientes-pedidos/pages/clientes.xhtml |
| **Pedidos** | http://localhost:8080/gestao-clientes-pedidos/pages/pedidos.xhtml |
| **Console H2** | http://localhost:8080/gestao-clientes-pedidos/h2-console.xhtml |

<img width="1219" height="607" alt="Image" src="https://github.com/user-attachments/assets/b71f5a73-4b45-43de-b9ce-8fdabf56e14a" />

<img width="1229" height="705" alt="Image" src="https://github.com/user-attachments/assets/e82d0567-1263-4329-908c-442087048041" />

<img width="1224" height="889" alt="Image" src="https://github.com/user-attachments/assets/c544529c-93cf-4eb8-931d-3c0f8cba89a8" />


#### Console de Administração do WildFly

Acesse: http://localhost:9990

**⚠️ Lembre-se**: Você precisa ter executado o `add-user.bat`/`add-user.sh` para criar um usuário de administração antes de acessar o console.

<!-- 
[INSERIR SCREENSHOT DO CONSOLE WILDFLY AQUI]
![Console WildFly](screenshots/wildfly-console.png)
-->

## 📁 Estrutura do Projeto

```
projeto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/projeto/
│   │   │       ├── domain/              # Entidades JPA (Cliente, Pedido)
│   │   │       ├── dto/                 # DTOs (ClienteDTO, PedidoDTO)
│   │   │       ├── mapper/               # Mappers para conversão Entity ↔ DTO
│   │   │       ├── repository/           # Camada de acesso a dados
│   │   │       ├── service/             # Camada de lógica de negócio
│   │   │       ├── controller/          # ManagedBeans JSF
│   │   │       └── converter/           # Conversores JSF (LocalDate, Cliente)
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml      # Configuração JPA
│   │   └── webapp/
│   │       ├── pages/                   # Páginas XHTML
│   │       │   ├── clientes.xhtml
│   │       │   └── pedidos.xhtml
│   │       ├── WEB-INF/
│   │       │   ├── web.xml              # Descritor de deploy
│   │       │   ├── beans.xml            # Configuração CDI
│   │       │   └── faces-config.xml     # Configuração JSF
│   │       ├── h2-console.xhtml         # Console H2 Database
│   │       └── index.xhtml              # Página inicial
│   └── test/
│       └── java/                        # Testes unitários (futuro)
├── docker/
│   ├── init-db.sql                      # Script de inicialização do DB
│   └── docker-compose.yml               # Configuração Docker
├── pom.xml                              # Configuração Maven
└── README.md                            # Este arquivo
```

## 🗄️ Modelo de Dados

### Entidade Cliente

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | Chave primária, auto increment |
| `nome` | String(100) | Nome completo do cliente (obrigatório) |
| `cpf` | String(11) | CPF sem formatação (obrigatório, único) |
| `email` | String(100) | E-mail do cliente (obrigatório) |
| `data_cadastro` | LocalDate | Data de cadastro (obrigatório) |

### Entidade Pedido

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | Long | Chave primária, auto increment |
| `numero_pedido` | String(20) | Número único do pedido (obrigatório, único) |
| `descricao` | String(500) | Descrição do pedido (obrigatório) |
| `valor_total` | BigDecimal(10,2) | Valor total do pedido (obrigatório) |
| `data_pedido` | LocalDate | Data do pedido (obrigatório) |
| `cliente_id` | Long | Chave estrangeira para Cliente (obrigatório) |

### Relacionamento

- **Cliente** 1:N **Pedido** (Um cliente pode ter vários pedidos)
- A exclusão de um cliente só é permitida se não houver pedidos vinculados

## 📝 Funcionalidades Implementadas

### ✅ Clientes

- [x] Cadastrar novo cliente com validação de CPF
- [x] Listar todos os clientes com paginação
- [x] Editar dados do cliente
- [x] Remover cliente (com validação de pedidos vinculados)
- [x] Validação de CPF (formato e duplicidade)
- [x] Formatação automática de CPF na interface
- [x] Ordenação e filtros na listagem
- [x] **Validação Bean Validation**: Nome, CPF e E-mail obrigatórios; E-mail com formato válido

### ✅ Pedidos

- [x] Cadastrar novo pedido vinculado a um cliente
- [x] Listar todos os pedidos com paginação
- [x] Listar pedidos filtrados por cliente
- [x] Editar pedido existente
- [x] Remover pedido
- [x] Validação de número de pedido único
- [x] Formatação de valores monetários
- [x] Formatação de datas
- [x] **Validação Bean Validation**: Todos os campos obrigatórios; Valor mínimo de 0.01; Cliente obrigatório

### ✅ Recursos Adicionais

- [x] Console H2 Database integrado para visualização do banco
- [x] Tratamento de erros com mensagens amigáveis
- [x] Logs estruturados (Java Util Logging)
- [x] **Validações com Bean Validation** (JSR 303/380)
  - Validação de campos obrigatórios (@NotBlank, @NotNull)
  - Validação de formato de e-mail (@Email)
  - Validação de valores numéricos (@DecimalMin)
  - Mensagens customizadas em português
- [x] Conversores customizados para LocalDate
- [x] Interface responsiva com PrimeFaces
- [x] **Maven Profiles** para diferentes ambientes (dev/prod)

## 🎯 Itens Diferenciais da Prova Técnica

Conforme os requisitos da prova técnica, os seguintes itens diferenciais foram implementados:

### ✅ Implementados

| Item Diferencial | Status | Detalhes |
|------------------|--------|----------|
| **Validações com Bean Validation** | ✅ Implementado | Todas as entidades utilizam anotações Bean Validation (@NotBlank, @NotNull, @Email, @DecimalMin) com mensagens customizadas |
| **Paginação nas listas** | ✅ Implementado | Todas as listas (Clientes e Pedidos) possuem paginação configurável via PrimeFaces DataTable |
| **Maven Profiles** | ✅ Implementado | Profiles configurados para desenvolvimento (dev) e produção (prod) |
| **Docker Compose completo** | ✅ Implementado | Docker Compose configurado para PostgreSQL com script de inicialização |

### ❌ Não Implementados

| Item Diferencial | Status | Justificativa |
|------------------|--------|---------------|
| **Uso de DTOs** | ✅ Implementado | DTOs implementados para Cliente e Pedido, com mappers para conversão entre entidades e DTOs, isolando a camada de apresentação |
| **Testes unitários básicos** | ❌ Não implementado | Estrutura preparada, mas testes não foram desenvolvidos devido ao foco nas funcionalidades principais |
| **Uso de Java 21 com recursos modernos** | ❌ Não implementado | Projeto utiliza Java 8 para compatibilidade com WildFly 20 e maior estabilidade |

## 🔧 Configurações Técnicas

### Bean Validation

O projeto utiliza **Bean Validation (JSR 303/380)** para validação de dados nas entidades. As validações são aplicadas automaticamente pelo JSF e Hibernate.

#### Validações Implementadas

**Entidade Cliente:**
```java
@NotBlank(message = "Nome é obrigatório")
private String nome;

@NotBlank(message = "CPF é obrigatório")
private String cpf;

@NotBlank(message = "E-mail é obrigatório")
@Email(message = "E-mail inválido")
private String email;

@NotNull(message = "Data de cadastro é obrigatória")
private LocalDate dataCadastro;
```

**Entidade Pedido:**
```java
@NotBlank(message = "Número do pedido é obrigatório")
private String numeroPedido;

@NotBlank(message = "Descrição é obrigatória")
private String descricao;

@NotNull(message = "Valor total é obrigatório")
@DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
private BigDecimal valorTotal;

@NotNull(message = "Data do pedido é obrigatória")
private LocalDate dataPedido;

@NotNull(message = "Cliente é obrigatório")
private Cliente cliente;
```

#### Como Funciona

1. **Validação no Frontend (JSF)**: Os componentes PrimeFaces validam automaticamente os campos usando as anotações Bean Validation
2. **Validação no Backend (JPA)**: O Hibernate valida as entidades antes de persistir no banco de dados
3. **Mensagens Personalizadas**: Cada validação possui uma mensagem customizada em português

#### Dependências

```xml
<!-- Bean Validation API -->
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
    <scope>provided</scope>
</dependency>

<!-- Hibernate Validator (implementação) -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.2.5.Final</version>
    <scope>provided</scope>
</dependency>
```

### DTOs (Data Transfer Objects)

**DTOs foram implementados** para isolar a camada de apresentação das entidades JPA, seguindo boas práticas de arquitetura em camadas.

#### Estrutura de DTOs

**ClienteDTO:**
```java
public class ClienteDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataCadastro;
    // Getters e Setters
}
```

**PedidoDTO:**
```java
public class PedidoDTO {
    private Long id;
    private String numeroPedido;
    private String descricao;
    private BigDecimal valorTotal;
    private LocalDate dataPedido;
    private Long clienteId;        // ID do cliente (ao invés do objeto completo)
    private String clienteNome;    // Nome do cliente (para exibição)
    // Getters e Setters
}
```

#### Mappers

Foram criados mappers para conversão entre entidades e DTOs:

- **ClienteMapper**: Converte entre `Cliente` (entidade) e `ClienteDTO`
- **PedidoMapper**: Converte entre `Pedido` (entidade) e `PedidoDTO`

**Exemplo de uso:**
```java
// Converter entidade para DTO
ClienteDTO dto = clienteMapper.toDTO(cliente);

// Converter DTO para entidade
Cliente cliente = clienteMapper.toEntity(dto);
```

#### Benefícios da Implementação de DTOs

- ✅ **Isolamento de camadas**: A camada de apresentação não conhece as entidades JPA
- ✅ **Segurança**: Controle sobre quais dados são expostos
- ✅ **Flexibilidade**: Facilita mudanças no modelo de domínio sem impactar a apresentação
- ✅ **Performance**: Evita problemas de lazy loading ao trabalhar apenas com dados necessários
- ✅ **Manutenibilidade**: Separação clara de responsabilidades

#### Fluxo de Dados com DTOs

```
View (XHTML) 
    ↓
Controller (Bean) - trabalha com DTOs
    ↓
Service - converte DTO para Entity, processa, retorna DTO
    ↓
Repository - trabalha apenas com Entities
    ↓
Database
```

### Profiles Maven

O projeto utiliza **Maven Profiles** para diferentes ambientes de execução:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <environment>development</environment>
        </properties>
    </profile>

    <profile>
        <id>prod</id>
        <properties>
            <environment>production</environment>
        </properties>
    </profile>
</profiles>
```

#### Como Usar

**Perfil de Desenvolvimento (padrão):**
```bash
mvn clean package
# ou explicitamente
mvn clean package -Pdev
```

**Perfil de Produção:**
```bash
mvn clean package -Pprod
```

#### Expansão Futura dos Profiles

Os profiles podem ser expandidos para incluir:
- Configurações diferentes de banco de dados por ambiente
- Propriedades específicas de logging
- Configurações de datasource diferentes
- Flags de build específicas

**Exemplo de expansão:**
```xml
<profile>
    <id>dev</id>
    <properties>
        <db.url>jdbc:postgresql://localhost:5432/projeto_dev</db.url>
        <hibernate.show_sql>true</hibernate.show_sql>
    </properties>
</profile>

<profile>
    <id>prod</id>
    <properties>
        <db.url>jdbc:postgresql://prod-server:5432/projeto</db.url>
        <hibernate.show_sql>false</hibernate.show_sql>
    </properties>
</profile>
```

### persistence.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2" xmlns="http://xmlns.jcp.org/xml/ns/persistence">
    <persistence-unit name="Projeto" transaction-type="JTA">
        <jta-data-source>java:/jdbc/ProjetoDS</jta-data-source>
        <class>br.com.projeto.domain.Cliente</class>
        <class>br.com.projeto.domain.Pedido</class>
        <properties>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

### Arquitetura em Camadas

O projeto segue o padrão de arquitetura em camadas, separando responsabilidades:

#### 📐 Por que usar Controller, Service e Repository?

Cada camada tem uma responsabilidade específica:

1. **Domain Layer** (`domain/`): Entidades JPA que representam o modelo de dados
2. **Repository Layer** (`repository/`): **Apenas** acesso a dados (CRUD no banco)
   - Não contém regras de negócio
   - Facilita troca de banco de dados
   - Facilita testes
3. **Service Layer** (`service/`): **Regras de negócio** e validações
   - Validações de CPF, duplicidade, etc.
   - Pode ser usado por qualquer interface (web, API REST)
   - Não conhece JSF ou páginas web
4. **Controller Layer** (`controller/`): ManagedBeans JSF que conectam a interface com o Service
   - Gerencia estado da página
   - Trata eventos do usuário
   - Mostra mensagens
5. **View Layer** (`webapp/`): Páginas XHTML com componentes PrimeFaces

#### 🔀 Converter - Para que serve?

**Converters** transformam dados entre formatos diferentes no JSF:

- **LocalDateConverter**: Converte `LocalDate` ↔ `String` formatada (ex: "22/12/2025")
- **ClienteConverter**: Converte `Cliente` ↔ `String` (ID) para uso em `selectOneMenu`

**Exemplo:**
```java
// Sem converter: JSF não sabe como exibir LocalDate
<h:outputText value="#{pedido.dataPedido}" /> // ❌ Erro!

// Com converter: JSF sabe converter LocalDate para String
<h:outputText value="#{pedido.dataPedido}">
    <f:converter converterId="localDateConverter" />
</h:outputText> // ✅ Funciona!
```

#### 📊 Fluxo de Dados

```
View (XHTML) 
    ↓ [Evento do usuário]
Controller (Bean) 
    ↓ [Chama Service]
Service (Regras de negócio)
    ↓ [Chama Repository]
Repository (Acesso ao banco)
    ↓ [Persiste/Consulta]
Database
```

**💡 Dica**: Veja o arquivo `ARQUITETURA_PROJETO.md` para uma explicação detalhada com exemplos!

## 🧪 Testando a Aplicação

### 1. Cadastrar Cliente

1. Acesse: http://localhost:8080/gestao-clientes-pedidos/pages/clientes.xhtml
2. Preencha o formulário:
   - **Nome**: João Silva
   - **CPF**: 12345678901 (será formatado automaticamente)
   - **E-mail**: joao@email.com
   - **Data de Cadastro**: (preenchida automaticamente)
3. Clique em "Salvar"

### 2. Cadastrar Pedido

1. Acesse: http://localhost:8080/gestao-clientes-pedidos/pages/pedidos.xhtml
2. Preencha o formulário:
   - **Número do Pedido**: PED-001
   - **Cliente**: Selecione um cliente da lista
   - **Descrição**: Pedido de exemplo
   - **Valor Total**: 100,00
   - **Data do Pedido**: (data atual)
3. Clique em "Salvar"

### 3. Filtrar Pedidos por Cliente

1. Na página de pedidos, use o filtro "Filtrar Pedidos"
2. Selecione um cliente no dropdown
3. Clique em "Filtrar"

### 4. Acessar Console H2

1. Acesse: http://localhost:8080/gestao-clientes-pedidos/h2-console.xhtml
2. Para conectar ao PostgreSQL:
   - **Driver Class**: `org.postgresql.Driver`
   - **JDBC URL**: `jdbc:postgresql://localhost:5432/projeto`
   - **User Name**: `admin`
   - **Password**: `password`
3. Clique em "Connect"

## 📊 Logs

Os logs da aplicação são registrados usando **Java Util Logging (JUL)** e aparecem no console do WildFly e no arquivo `standalone/log/server.log`.

Principais eventos registrados:
- ✅ Criação, atualização e remoção de entidades
- ✅ Erros de validação
- ✅ Exceções durante operações
- ✅ Queries SQL executadas (quando `hibernate.show_sql=true`)

## ⚠️ Problemas Conhecidos e Soluções

### 1. Erro: "Connection is not valid"

**Causa**: PostgreSQL não está acessível ou credenciais incorretas.

**Solução**:
- Verifique se o PostgreSQL está rodando: `docker ps` ou `pg_isready -h localhost -p 5432`
- Teste a conexão: `psql -h localhost -U admin -d projeto`
- Verifique as credenciais no `standalone.xml`

### 2. Erro: "Cannot find component for expression"

**Causa**: Referência incorreta a componente JSF no XHTML.

**Solução**: Verifique se o `id` do componente existe e está acessível no contexto.

### 3. Erro: "LazyInitializationException"

**Causa**: Tentativa de acessar relacionamento lazy fora da sessão JPA.

**Solução**: Use `JOIN FETCH` nas queries do repository para carregar relacionamentos.

### 4. Erro ao acessar console WildFly

**Causa**: Usuário de administração não foi criado.

**Solução**: Execute `add-user.bat`/`add-user.sh` na pasta `bin` do WildFly antes de iniciar o servidor.

## 📜 Scripts SQL

### Criação Manual das Tabelas (se necessário)

```sql
-- Tabela Cliente
CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    data_cadastro DATE NOT NULL
);

-- Tabela Pedido
CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    numero_pedido VARCHAR(20) NOT NULL UNIQUE,
    descricao VARCHAR(500) NOT NULL,
    valor_total NUMERIC(10,2) NOT NULL,
    data_pedido DATE NOT NULL,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Índices
CREATE INDEX idx_pedido_cliente ON pedido(cliente_id);
CREATE INDEX idx_cliente_cpf ON cliente(cpf);
```

## 🐳 Docker

### Docker Compose

O projeto inclui um `docker-compose.yml` para facilitar a execução do PostgreSQL:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:14-alpine
    container_name: projeto-postgres
    environment:
      POSTGRES_DB: projeto
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./docker/init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U admin"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

### Comandos Docker

```bash
# Iniciar PostgreSQL
docker-compose up -d postgres

# Parar PostgreSQL
docker-compose down

# Ver logs
docker-compose logs -f postgres

# Verificar status
docker ps
```

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais e de avaliação técnica como parte de um processo seletivo.

## 👤 Autor

Desenvolvido como parte da **Prova Técnica - Engenheiro de Software**.

---

## 📞 Suporte e Dúvidas

Em caso de dúvidas ou problemas:

1. Verifique os logs do WildFly: `standalone/log/server.log`
2. Verifique se o PostgreSQL está acessível
3. Teste a conexão do datasource via console do WildFly
4. Verifique se todas as dependências foram baixadas corretamente (`mvn dependency:resolve`)

**Comandos úteis:**

```bash
# Ver logs do WildFly
tail -f standalone/log/server.log

# Verificar se o PostgreSQL está rodando
docker ps | grep postgres

# Recompilar o projeto
mvn clean package

# Verificar dependências
mvn dependency:tree
```

---

**Desenvolvido com ❤️ utilizando Java, JPA, JSF e PrimeFaces**
