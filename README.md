# 🛡️ Multi-Tenant Code Execution Platform (Judge)

B2B-платформа для безопасного удаленного хранения, версионирования и изолированного исполнения пользовательского кода в Docker-песочницах с поддержкой мультитенантности (разграничение ресурсов по компаниям) и настраиваемыми квотами CPU/RAM.

---

## 🚀 Ключевые возможности

* **Мультитенантность (Multi-tenancy):** Регистрация компаний-арендаторов с криптостойким хешированием паролей (`PBKDF2WithHmacSHA512`) и генерацией персональных API-ключей (`SecureRandom`).
* **Гибкие профили песочниц (JSONB):** Каждая компания настраивает собственные конфигурации окружений под нужные языки программирования (лимиты CPU, RAM, таймауты исполнения) с хранением в PostgreSQL `JSONB`.
* **Изолированное исполнение кода:** Запуск пользовательских сценариев в эфемерных Docker-контейнерах через Docker Engine API (`docker-java`) с жесткими ограничениями ядра Linux (cgroups):
    * Полная сетевая изоляция (`--network none`).
    * Файловая система только для чтения (`--read-only`) с выделенным `tmpfs` в памяти.
    * Жесткие квоты на процессорное время (`NanoCPUs`), оперативную память без swap и количество процессов (`PidsLimit` против fork-бомб).
    * Передача исходного кода внутрь контейнера через TAR-архивы в оперативной памяти без привязки к файловой системе хоста.
* **Управление скриптами:** Сохранение исходного кода, версионирование и декларативное описание схемы входных параметров (`input_schema` в `JSONB`).
* **Cloud-Native Observability:** Встроенные пробы здоровья (`/health/live`, `/health/ready`) и экспорт метрик в формате Prometheus (`/metrics`) из коробки через Eclipse MicroProfile.

---

## 🏗 Архитектура проекта

Проект спроектирован как **модульный монолит** на базе стандартов **Jakarta EE 10** и **Eclipse MicroProfile 6.1** с четким разделением границ зависимостей на уровне Maven-модулей:

```text
judge/
├── judge-domain/         # Чистое доменное ядро: модели (Java 21 Records), контракты песочниц, лимиты
├── judge-persistence/    # Слой доступа к данным: JPA (EclipseLink), JSONB-конвертеры, Liquibase-миграции
└── judge-api/            # Внешний слой: JAX-RS контроллеры, @NameBinding фильтры безопасности, Docker-раннер
```

### Поток обработки запроса
1. Клиент отправляет HTTP-запрос с заголовком `X-API-Key`.
2. JAX-RS фильтр (`ContainerRequestFilter` + `@NameBinding`) валидирует ключ и помещает профиль компании в `@RequestScoped` контекст.
3. При запросе на выполнение скрипта сервис загружает исходный код и лимиты песочницы компании из PostgreSQL.
4. `DockerSandbox` поднимает изолированный контейнер (например, `eclipse-temurin:21-jdk-alpine`), загружает код в память, запускает компиляцию/исполнение с контролем таймаута, собирает `stdout`/`stderr` и гарантированно удаляет контейнер.

---

## 🛠 Технологический стек

* **Язык и платформа:** Java 21 (Records, Pattern Matching), Jakarta EE 10, Eclipse MicroProfile 6.1
* **Сервер приложений:** Open Liberty (с декларативной конфигурацией фич в `server.xml`)
* **База данных и миграции:** PostgreSQL 16 (`JSONB`), Jakarta Persistence (EclipseLink), Liquibase
* **Контейнеризация и раннер:** Docker Engine API (`docker-java`), Apache Commons Compress
* **Сборка и мониторинг:** Maven (Multi-module), MicroProfile Health & Metrics (Prometheus)

---

## ⚙️ Локальный запуск

### Требования
* JDK 21+
* Maven 3.9+
* Docker (запущенный демон с доступом к `/var/run/docker.sock`)
* PostgreSQL 15+

### 1. Подготовка базового образа песочницы
```bash
docker pull eclipse-temurin:21-jdk-alpine
```

### 2. Настройка переменных окружения
Создайте файл `judge-api/src/main/liberty/config/server.env`:
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=judge_db
DB_USER=postgres
DB_PASSWORD=secret
keystore_password=changeit
```

### 3. Запуск в режиме разработки (Hot Reload)
Из корневой директории проекта выполните:
```bash
mvn clean package liberty:dev -pl judge-api -am
```
При старте Liquibase автоматически применит все миграции из `db/changelog/`. Сервер будет доступен по адресу `http://localhost:9080`.

---

## 📡 Примеры API-запросов

### 1. Регистрация компании
```bash
curl -X POST http://localhost:9080/api/company/register \
  -H "Content-Type: application/json" \
  -d '{"name": "acme-corp", "password": "strongPassword123"}'
```

### 2. Регистрация конфигурации песочницы
```bash
curl -X POST http://localhost:9080/api/sandbox/register \
  -H "Content-Type: application/json" \
  -H "X-API-Key: jdg_your_api_key" \
  -d '{
    "name": "java-21-standard",
    "language": "JAVA",
    "limits": {
      "maxCpu": 0.5,
      "maxMemoryMb": 128,
      "timeoutMs": 2000
    }
  }'
```

### 3. Сохранение и запуск скрипта
```bash
# Создание скрипта
curl -X POST http://localhost:9080/api/script \
  -H "Content-Type: application/json" \
  -H "X-API-Key: jdg_your_api_key" \
  -d '{
    "sandboxId": "<SANDBOX_UUID>",
    "name": "HelloWorld",
    "sourceCode": "public class Main { public static void main(String[] args) { System.out.println(\"Hello, \" + System.getenv(\"username\")); } }",
    "inputSchema": [
      {"name": "username", "type": "STRING", "defaultValue": "World", "required": false}
    ]
  }'

# Запуск скрипта
curl -X POST http://localhost:9080/api/script/<SCRIPT_UUID>/execute \
  -H "Content-Type: application/json" \
  -H "X-API-Key: jdg_your_api_key" \
  -d '{"username": "Aleksey"}'
```

---

## 🗺 Roadmap (План развития)

* [ ] Поддержка Python-песочницы (`python-sandbox`) и автоматическая маршрутизация через CDI `Instance<LanguageSandbox>`.
* [ ] Асинхронное исполнение тяжелых задач через `ManagedExecutorService` / брокер сообщений (Kafka / RabbitMQ).
* [ ] Пул прогретых контейнеров (`docker exec`) для снижения задержки холодного старта.
* [ ] Упаковка приложения в multi-stage `Dockerfile` (на базе `open-liberty:kernel-slim-java21-openj9`) и оркестрация через `docker-compose` с Prometheus + Grafana.
