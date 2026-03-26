# Order Service

Serviço de gerenciamento de pedidos da Anymarket, responsável por orquestrar a criação, atualização e rastreamento de pedidos através de um sistema orientado por eventos.

## 📋 Visão Geral

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.5.12
- **Banco de Dados**: PostgreSQL 18
- **Message Broker**: Apache Kafka (Confluent Platform 7.6.0 - Kafka 3.x)
- **Build**: Maven

---

## 🚀 Como Subir a Infraestrutura

### Pré-requisitos

- Docker e Docker Compose instalados
- Git

### Subir os Serviços com Docker Compose

A infraestrutura é composta por PostgreSQL, Zookeeper e Kafka. Para iniciá-la:

```bash
# Subir todos os serviços em background
docker-compose up -d

# Visualizar logs em tempo real
docker-compose logs -f

# Parar todos os serviços
docker-compose down

# Parar e remover volumes (apaga dados)
docker-compose down -v
```

### Serviços Disponíveis

| Serviço | Container | Porta | Credenciais |
|---------|-----------|-------|-------------|
| **PostgreSQL** | pg-order | 5443 | `user: pg-order` / `pass: 0706` |
| **Zookeeper** | zookeeper | 2181 | - |
| **Kafka** | kafka | 9092 | - |

---

## 🏃 Como Executar o Serviço

### Pré-requisitos

- Java 21 JDK instalado
- Maven 3.8+
- Infraestrutura em execução (Docker Compose e PostgreSQL)

### Build do Projeto

```bash
# Compilar o projeto
./mvnw clean package

# Compilar sem executar testes
./mvnw clean package -DskipTests
```

### Executar o Serviço

```bash
# Executar diretamente
./mvnw spring-boot:run

# Ou após build
java -jar target/orders-0.0.1-SNAPSHOT.jar
```

O serviço estará disponível em **http://localhost:8080**

Obs: Como o projeto já possui o launch.json configurado, basta rodá-lo com F5 que irá buildar e executar em modo debug.

### Verificar Saúde do Serviço

```bash
# Health check
curl http://localhost:8080/actuator/health

#Infos
curl http://localhost:8080/actuator/info

# Métricas
curl http://localhost:8080/actuator/metrics

# Métricas do Prometheus
curl http://localhost:8080/actuator/prometheus
```

---

## 🔧 Variáveis de Ambiente

### Banco de Dados (PostgreSQL)

```properties
# URL de conexão
spring.datasource.url=jdbc:postgresql://localhost:5443/pg-order

# Credenciais
spring.datasource.username=pg-order
spring.datasource.password=0706

# Estratégia de DDL (validate, update, create, create-drop)
spring.jpa.hibernate.ddl-auto=validate

# Logs SQL
spring.jpa.show-sql=false
```

### Kafka

```properties
# Bootstrap servers
spring.kafka.bootstrap-servers=localhost:9092

# Producer
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3

# Consumer
spring.kafka.consumer.group-id=order-service
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*
```

### Observabilidade (Prometheus & Actuator)

```properties
# Health check detalhado
management.endpoint.health.show-details=always

# Endpoints expostos
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Prometheus
management.prometheus.metrics.export.enabled=true
```

### Swagger/OpenAPI

```properties
# API Docs
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Outbox Relay (Disseminação de Eventos)

```properties
# Intervalo de verificação do outbox (em ms)
outbox.relay.fixed-delay-ms=5000
```

## 🧪 Como Rodar Testes

### Pré-requisitos para Testes

- Docker e Docker Compose (TestContainers cria ambientes isolados)
- Java 21 JDK

### Rodar Todos os Testes

```bash
# Executar todos os testes
./mvnw test

# Com output detalhado
./mvnw test -X

# Com relatório de cobertura
./mvnw test jacoco:report
```

---

## 🏗️ Decisões Arquiteturais

### 1. Arquitetura Hexagonal (Ports & Adapters) + Clean Architecture

**Decisão**: Implementar uma arquitetura hexagonal combinada com princípios de Clean Architecture para máxima desacoplamento entre domínio e infraestrutura.

**Trade-offs**:
- ✅ **Vantagens**:
  - Fácil testes unitários (domínio sem dependências externas)
  - Migração de tecnologias sem alterar domínio (trocar BD, trocar Kafka)
  - Independência entre camadas
  - Manutenibilidade a longo prazo
  - Alta coesão e baixo acoplamento

- ❌ **Desvantagens**:
  - Curva de aprendizado mais acentuada
  - Mais arquivos para mudanças simples

---

### 2. Event-Driven Architecture (Comunicação Assíncrona)

**Decisão**: Usar publicação de eventos no Kafka para comunicação entre serviços.

**Trade-offs**:
- ✅ **Vantagens**:
  - Desacoplamento entre serviços
  - Escalabilidade horizontal
  - Auditoria de eventos
  - Permitir múltiplos subscribers

- ❌ **Desvantagens**:
  - Eventual consistency (não absoluta)
  - Complexidade de debugging
  - Necessidade de compensating transactions
  - Overhead de infraestrutura

---

### 3. Padrão Outbox (Transactional Outbox)

**Decisão**: Garantir entrega de eventos mesmo com falhas de broker usando o padrão Outbox.

**Trade-offs**:
- ✅ **Vantagens**:
  - Persistência de eventos (não serão perdidos)
  - Recuperação de falhas automática
  - Auditoria completa

- ❌ **Desvantagens**:
  - Complexidade adicional (job de relay)
  - Latência de disseminação (até 5s)

---

### 4. Idempotência

**Decisão**: Implementar chave de idempotência para operações críticas.

**Trade-offs**:
- ✅ **Vantagens**:
  - Seguro a retentativas de rede
  - Prevenção de duplicação de eventos
  - Consistência mesmo com falhas

- ❌ **Desvantagens**:
  - Armazenamento de chaves em BD
  - Validação adicional em cada requisição
  - Cleanup periódico necessário
---

### 5. Java 21 + Spring Boot 3.5

**Decisão**: Usar versões LTS mais recentes para segurança, performance e features modernas.

**Trade-offs**:
- ✅ **Vantagens**:
  - LTS com suporte até 2029 (Java 21)
  - Virtual threads para I/O
  - Record types
  - Performance melhorada
  - Correções de segurança

- ❌ **Desvantagens**:
  - Compatibilidade com bibliotecas antigas
  - Mais quebra de compatibilidade

---

### 6. Spring Data JPA + Flyway para Persistência

**Decisão**: Usar ORM com JPA e versionamento de schema com Flyway.

**Trade-offs**:
- ✅ **Vantagens**:
  - Queries type-safe
  - Migrações versionadas
  - Rollback automático

- ❌ **Desvantagens**:
  - Performance inferior vs SQLs nativos
  - Overhead de JPA
  - Queries geradas podem ser ineficientes

---

### 7. TestContainers para Testes de Integração

**Decisão**: Usar TestContainers para provisionar BD e Kafka durante testes.

**Trade-offs**:
- ✅ **Vantagens**:
  - Ambiente real de testes (não mocks)
  - Isolamento completo entre testes
  - Reproduzibilidade

- ❌ **Desvantagens**:
  - Testes mais lentos (~2-3x mais)
  - Requer Docker instalado
  - Overhead de resource

---

### 8. Observabilidade com Prometheus & Actuator

**Decisão**: Expor métricas via Prometheus e health checks via Actuator.

**Trade-offs**:
- ✅ **Vantagens**:
  - Monitoramento em tempo real
  - Alertas automáticos
  - Debugging facilitado
  - Observability pronta

- ❌ **Desvantagens**:
  - Overhead de coleta de métricas
  - Necessário Prometheus/Grafana
  - Dados de saúde públicos

---



## 🔐 Melhorias Posteriores p/ Segurança

- [ ] Mudar senha PostgreSQL em produção
- [ ] Usar HTTPS para comunicação Spring Boot
- [ ] Configurar autenticação Kafka (SASL)
- [ ] Colocar variáveis sensíveis em vault (HashiCorp Vault, AWS Secrets Manager)
- [ ] Habilitar Spring Security para endpoints

---