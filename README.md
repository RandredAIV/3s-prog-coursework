# RU
# AVRAMCHUK BOLID MOTORSPORT 🏎️

> Веб-приложение для просмотра каталога болидов Формулы-1,
> оформления заявок на покупку и управления системой через административную панель.
>
> Репозиторий: https://github.com/Randred/CourseWork

---

## 📋 О проекте

AVRAMCHUK BOLID MOTORSPORT — полноценное веб-приложение для условного автосалона
спортивных болидов Formula 1. Система позволяет пользователям просматривать каталог
болидов прошлых сезонов, фильтровать их по параметрам, добавлять в корзину и
оформлять заявки на покупку. Администратор управляет каталогом, пользователями
и заявками через отдельную административную панель.

Визуальный стиль: минималистичный интерфейс в спортивной стилистике Formula 1,
тёмная палитра с красными акцентными элементами.

---

## ⚙️ Стек технологий
```
Backend          │ Java 17, Spring Boot
Frontend         │ HTML, CSS, JavaScript, Thymeleaf
База данных      │ PostgreSQL 15 / 16
Сборка           │ Maven
Контейнеризация  │ Docker, Docker Compose
Безопасность     │ Spring Security, BCrypt
ORM              │ Spring Data JPA, Hibernate
```

---

## 📦 Зависимости Maven
```
spring-boot-starter-web          │ Обработка HTTP-запросов, MVC-логика
spring-boot-starter-thymeleaf    │ Серверная генерация HTML-страниц
spring-boot-starter-data-jpa     │ Доступ к БД через JPA и Hibernate
spring-boot-starter-security     │ Аутентификация и авторизация
spring-boot-starter-validation   │ Валидация форм (@NotNull, @Size, @Min, @Max)
spring-boot-starter-test         │ Модуль тестирования
postgresql                       │ JDBC-драйвер для PostgreSQL
bcrypt                           │ Безопасное хэширование паролей
```

Все зависимости подтягиваются автоматически через Maven при первом запуске —
вручную ничего скачивать не нужно.

---

## 💻 Что нужно установить перед запуском

Для работы приложения необходимо установить четыре инструмента.
Ниже — пошаговые инструкции для каждого.

---

### 1. Java Development Kit (JDK 17+)

JDK — среда выполнения для Java-приложений. Без неё сервер не запустится.

**Linux (Ubuntu/Debian):**
```
sudo apt update
sudo apt install openjdk-17-jdk -y
```

**Проверка установки:**
```
java -version
```
Должно появиться: `openjdk version "17.x.x"`

**Windows:**
Скачать установщик с официального сайта: https://adoptium.net
Выбрать: Temurin 17 (LTS) → Windows → .msi → установить с галочкой "Add to PATH"

**Проверка (в cmd или PowerShell):**
```
java -version
```

---

### 2. Maven

Maven — система сборки проекта. Нужна для компиляции и запуска Spring Boot.

**Linux (Ubuntu/Debian):**
```
sudo apt install maven -y
```

**Проверка:**
```
mvn -version
```
Должно появиться: `Apache Maven 3.x.x`

**Windows:**
Скачать архив с официального сайта: https://maven.apache.org/download.cgi
Распаковать, добавить папку bin/ в переменную среды PATH.

**Проверка:**
```
mvn -version
```

---

### 3. Docker

Docker — платформа для запуска контейнеров. Нужна для запуска базы данных PostgreSQL.

**Linux (Ubuntu/Debian):**
```
sudo apt update
sudo apt install docker.io -y
sudo systemctl start docker
sudo systemctl enable docker
```

Добавить текущего пользователя в группу docker (чтобы не писать sudo):
```
sudo usermod -aG docker $USER
```
После этой команды необходимо перезайти в систему (logout/login).

**Проверка:**
```
docker --version
```
Должно появиться: `Docker version 24.x.x`

**Windows:**
Скачать Docker Desktop: https://www.docker.com/products/docker-desktop
Установить и запустить. Docker Desktop включает в себя и Docker Compose.

---

### 4. Docker Compose

Docker Compose — инструмент для управления несколькими контейнерами.
Нужен для поднятия контейнера с PostgreSQL одной командой.

**Linux (Ubuntu/Debian):**
```
sudo apt install docker-compose -y
```

**Проверка:**
```
docker-compose --version
```

**Windows:**
Docker Compose уже входит в состав Docker Desktop — отдельно устанавливать не нужно.

---

### 5. PostgreSQL (опционально)

Устанавливать PostgreSQL напрямую не обязательно — база данных запускается
автоматически в Docker-контейнере. Но если хочется работать с БД через
консоль psql или графический клиент pgAdmin — установить можно:

**Linux:**
```
sudo apt install postgresql postgresql-contrib -y
```

**pgAdmin (графический интерфейс для PostgreSQL):**
```
curl https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo apt-key add
sudo sh -c 'echo "deb https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list && apt update'
sudo apt install pgadmin4 -y
```

---

## 🚀 Запуск проекта — пошаговая инструкция

### Шаг 1 — Клонировать репозиторий
```
git clone https://github.com/Randred/CourseWork
cd CourseWork
```

Если git не установлен:
```
sudo apt install git -y       # Linux
```
На Windows скачать с: https://git-scm.com/download/win

---

### Шаг 2 — Перейти в папку с проектом
```
cd autosalon-web
```

Именно в этой папке находится файл docker-compose.yml и pom.xml.

---

### Шаг 3 — Запустить контейнер базы данных PostgreSQL
```
docker-compose up -d
```

Флаг `-d` запускает контейнер в фоновом режиме.

После выполнения команды автоматически происходит:
- скачивается образ PostgreSQL (при первом запуске)
- создаётся база данных приложения
- настраиваются параметры подключения (имя БД, пользователь, пароль)
- инициализируются таблицы

Проверить, что контейнер запущен:
```
docker ps
```
В списке должен отображаться контейнер с образом postgres.

---

### Шаг 4 — Настройка подключения к базе данных

Параметры подключения к БД хранятся в файле:
```
src/main/resources/application.yml
```

Убедитесь, что параметры совпадают с теми, что указаны в docker-compose.yml:
```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/autosalon
    username: postgres
    password: ваш_пароль
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

Если параметры в docker-compose.yml отличаются — приведите application.yml в соответствие.

---

### Шаг 5 — Запустить серверную часть (Spring Boot)

**Вариант A — через Maven (рекомендуется):**
```
mvn spring-boot:run
```

Maven автоматически скачает все зависимости и запустит приложение.
При первом запуске загрузка зависимостей может занять несколько минут.

**Вариант Б — через скомпилированный JAR:**
```
mvn clean package
java -jar target/autosalon-web-0.0.1-SNAPSHOT.jar
```

---

### Шаг 6 — Открыть приложение в браузере
```
http://localhost:8080
```

Поддерживаемые браузеры: Google Chrome, Mozilla Firefox, Yandex Browser, Opera, Edge.

---

### Шаг 7 — Остановить контейнер (когда закончили работу)
```
docker-compose down
```

---

## 🗂️ Архитектура проекта
```
com.coursework.autosalon.web
│
├── model/                          # Доменные сущности (JPA-классы)
│   ├── User                        # Пользователь
│   ├── Car                         # Болид
│   ├── Team                        # Команда F1
│   ├── Driver                      # Гонщик
│   ├── UserCartItem                # Элемент корзины
│   └── PurchaseRequest             # Заявка на покупку
│
├── repository/                     # Слой доступа к данным (Spring Data JPA)
│   ├── UserRepository
│   ├── CarRepository
│   ├── TeamRepository
│   ├── DriverRepository
│   ├── UserCartRepository
│   └── PurchaseRequestRepository
│
├── service/                        # Бизнес-логика приложения
│   ├── Регистрация и валидация пользователей
│   ├── Фильтрация и поиск болидов
│   ├── Управление корзиной
│   ├── Оформление и обработка заявок
│   └── Проверка возможности удаления болида
│
├── controller/                     # Веб-контроллеры
│   ├── AuthController              # /login, /register
│   ├── CarController               # Каталог, карточка болида
│   ├── CartController              # /cart/add, /cart/remove
│   ├── OrderController             # /checkout
│   ├── AdminCarController          # CRUD болидов (ADMIN)
│   ├── AdminUserController         # Управление пользователями (ADMIN)
│   └── AdminRequestController      # Управление заявками (ADMIN)
│
├── config/                         # Конфигурация Spring Security
└── security/                       # Механизм авторизации и сессий

src/main/resources/
├── templates/                      # HTML-шаблоны Thymeleaf
│   ├── index.html                  # Главная страница
│   ├── login.html                  # Страница входа
│   ├── register.html               # Регистрация
│   ├── catalog.html                # Каталог с фильтрами
│   ├── car-card.html               # Карточка болида
│   ├── cart.html                   # Корзина
│   ├── checkout.html               # Оформление заявки
│   └── admin/                      # Административные страницы
├── application.yml                 # Настройки приложения и БД
└── docker-compose.yml              # Конфигурация Docker-контейнера PostgreSQL
```

---

## 🗃️ База данных
```
users                    # Пользователи системы
├── id
├── фамилия, имя, отчество
├── email
├── password_hash          # Только BCrypt-хэш
├── телефон, дата_рождения, пол
└── role                   # USER или ADMIN

cars                     # Болиды
├── id, название, описание, изображение
├── сезон, цена, статус_наличия
├── team_id  ──────────────────────► teams.id
└── driver_id ─────────────────────► drivers.id

teams                    # Команды Formula 1
└── id, название, страна, основатель, год_основания

drivers                  # Гонщики
└── id, имя, национальность, team_id ──► teams.id

user_cart_items          # Корзина пользователя
├── user_id ────────────────────────► users.id
└── car_id  ────────────────────────► cars.id

purchase_requests        # Заявки на покупку
├── id, номер_заявки, дата, статус
├── цена_снимок            # Зафиксированная цена на момент заявки
├── user_id ────────────────────────► users.id
└── car_id  ────────────────────────► cars.id
```

> ⚠️ Удалить болид нельзя, пока он есть в активных заявках или корзине.
> Это защищено на уровне бизнес-логики сервера.

---

## 🔐 Безопасность
```
BCrypt-хэширование       │ Пароли хранятся только в виде хэшей
Ролевое разграничение    │ Роли USER и ADMIN — admin-маршруты закрыты для пользователей
SQL-инъекции             │ JPA/Hibernate использует параметризованные запросы
XSS                      │ Thymeleaf автоматически экранирует вывод данных
Привязка к user_id       │ Пользователь управляет только своей корзиной и заявками
Переменные среды         │ Пароли БД задаются через ENV, не хранятся в исходниках
Docker-изоляция          │ PostgreSQL доступна только по локальному порту
Журналирование           │ Все обращения к защищённым ресурсам логируются
Сессии                   │ Завершение по /logout
```

---

## 👤 Роли пользователей

### Пользователь (USER)
```
Регистрация:
  → Ввести ФИО, дату рождения, телефон, пол, email, пароль
  → Нажать «Зарегистрироваться» → редирект на страницу входа

Авторизация:
  → Ввести email и пароль → нажать «Войти»
  → Автоматически открывается каталог болидов

Каталог:
  → Настроить фильтры: сезон, производитель, цена, наличие
  → Нажать «Применить фильтр»
  → Нажать «Подробнее» на нужном болиде

Карточка болида:
  → Просмотр характеристик, цены и изображения
  → Нажать «Добавить в корзину»
  → Нажать «Каталог» для возврата

Корзина:
  → Просмотр добавленных болидов
  → Удалить позицию → «Удалить»
  → Очистить всё → «Очистить заявку»
  → Вернуться → «Продолжить покупки»
  → Оформить → «Оформить заявку»

После оформления заявки:
  → Фиксируется модель, цена, дата, номер заявки
  → Статус болида меняется на «Недоступен»
  → Корзина очищается
  → Отображается уведомление об успешной отправке

Выход:
  → Нажать «Выйти» → сессия завершается → главная страница
```

### Администратор (ADMIN)
```
Все возможности USER, плюс:

Каталог:
  → Просмотр скрытых болидов
  → Добавление нового болида (все поля)
  → Редактирование (цена, описание, сезон, изображение)
  → Удаление (только если нет активных заявок)

Пользователи:
  → Полный список пользователей
  → Карточка пользователя с историей заказов

Заявки:
  → Просмотр всех заявок системы
  → Удаление позиций (недоступно для завершённых заявок)
  → Завершение заявки → болид освобождается для удаления
```

---

## 💬 Системные сообщения
```
Ситуация                                  │ Реакция системы
──────────────────────────────────────────┼────────────────────────────────────────
Неверный email или пароль                 │ Ошибка авторизации, предложение повторить
Болид уже в корзине                       │ Предупреждение о дублировании
Болид недоступен для покупки              │ Статус «Недоступен» / «Продан»
Успешное оформление заявки                │ Уведомление с номером и датой заявки
Удаление болида с активной заявкой        │ Отказ с сообщением об ошибке
Нет соединения с БД                       │ Сообщение о недоступности сервиса
Некорректные данные в форме               │ Подсветка полей с ошибками и подсказки
```

---

## ⚡ Нефункциональные характеристики
```
Производительность   │ Загрузка страницы — не более 3 секунд при стандартной нагрузке
Сохранность данных   │ Корзина хранится в БД — не исчезает при перезагрузке сервера
Отказоустойчивость   │ При потере соединения с БД отображается информативное сообщение
Масштабируемость     │ Архитектура позволяет расширять функционал без переработки системы
Поддерживаемость     │ Слоистая архитектура упрощает сопровождение и доработку
```
# EN
# AVRAMCHUK BOLID MOTORSPORT 🏎️

> Web application for browsing a Formula 1 car catalog,
> submitting purchase requests and managing the system via an admin panel.
>
> Repository: https://github.com/Randred/CourseWork

---

## 📋 About the Project

AVRAMCHUK BOLID MOTORSPORT is a full-featured web application for a fictional
Formula 1 sports car dealership. The system allows users to browse a catalog of
cars from past seasons, filter them by parameters, add them to a cart and
submit purchase requests. The administrator manages the catalog, users
and requests through a dedicated admin panel.

Visual style: minimalist interface in Formula 1 sports aesthetics,
dark palette with red accent elements.

---

## ⚙️ Tech Stack
```
Backend          │ Java 17, Spring Boot
Frontend         │ HTML, CSS, JavaScript, Thymeleaf
Database         │ PostgreSQL 15 / 16
Build Tool       │ Maven
Containerization │ Docker, Docker Compose
Security         │ Spring Security, BCrypt
ORM              │ Spring Data JPA, Hibernate
```

---

## 📦 Maven Dependencies
```
spring-boot-starter-web          │ HTTP request handling, MVC logic
spring-boot-starter-thymeleaf    │ Server-side HTML page generation
spring-boot-starter-data-jpa     │ Database access via JPA and Hibernate
spring-boot-starter-security     │ Authentication and authorization
spring-boot-starter-validation   │ Form validation (@NotNull, @Size, @Min, @Max)
spring-boot-starter-test         │ Testing module
postgresql                       │ JDBC driver for PostgreSQL
bcrypt                           │ Secure password hashing
```

All dependencies are pulled automatically via Maven on first launch —
nothing needs to be downloaded manually.

---

## 💻 What to Install Before Running

Four tools are required to run the application.
Step-by-step instructions for each are provided below.

---

### 1. Java Development Kit (JDK 17+)

JDK is the runtime environment for Java applications. Without it the server will not start.

**Linux (Ubuntu/Debian):**
```
sudo apt update
sudo apt install openjdk-17-jdk -y
```

**Verify installation:**
```
java -version
```
Expected output: `openjdk version "17.x.x"`

**Windows:**
Download the installer from the official site: https://adoptium.net
Choose: Temurin 17 (LTS) → Windows → .msi → install with "Add to PATH" checked

**Verify (in cmd or PowerShell):**
```
java -version
```

---

### 2. Maven

Maven is the project build system. Required to compile and run Spring Boot.

**Linux (Ubuntu/Debian):**
```
sudo apt install maven -y
```

**Verify:**
```
mvn -version
```
Expected output: `Apache Maven 3.x.x`

**Windows:**
Download the archive from the official site: https://maven.apache.org/download.cgi
Extract it and add the bin/ folder to the PATH environment variable.

**Verify:**
```
mvn -version
```

---

### 3. Docker

Docker is a container platform. Required to run the PostgreSQL database container.

**Linux (Ubuntu/Debian):**
```
sudo apt update
sudo apt install docker.io -y
sudo systemctl start docker
sudo systemctl enable docker
```

Add current user to the docker group (to avoid using sudo every time):
```
sudo usermod -aG docker $USER
```
After this command you need to log out and log back in.

**Verify:**
```
docker --version
```
Expected output: `Docker version 24.x.x`

**Windows:**
Download Docker Desktop: https://www.docker.com/products/docker-desktop
Install and launch. Docker Desktop includes Docker Compose as well.

---

### 4. Docker Compose

Docker Compose is a tool for managing multiple containers.
Required to bring up the PostgreSQL container with a single command.

**Linux (Ubuntu/Debian):**
```
sudo apt install docker-compose -y
```

**Verify:**
```
docker-compose --version
```

**Windows:**
Docker Compose is already included in Docker Desktop — no separate installation needed.

---

### 5. PostgreSQL (optional)

Installing PostgreSQL directly is not required — the database starts
automatically in a Docker container. However, if you want to access the DB
through the psql console or the pgAdmin GUI client, you can install it:

**Linux:**
```
sudo apt install postgresql postgresql-contrib -y
```

**pgAdmin (graphical interface for PostgreSQL):**
```
curl https://www.pgadmin.org/static/packages_pgadmin_org.pub | sudo apt-key add
sudo sh -c 'echo "deb https://ftp.postgresql.org/pub/pgadmin/pgadmin4/apt/$(lsb_release -cs) pgadmin4 main" > /etc/apt/sources.list.d/pgadmin4.list && apt update'
sudo apt install pgadmin4 -y
```

---

## 🚀 Running the Project — Step by Step

### Step 1 — Clone the repository
```
git clone https://github.com/Randred/CourseWork
cd CourseWork
```

If git is not installed:
```
sudo apt install git -y       # Linux
```
On Windows download from: https://git-scm.com/download/win

---

### Step 2 — Navigate to the project folder
```
cd autosalon-web
```

This folder contains both docker-compose.yml and pom.xml.

---

### Step 3 — Start the PostgreSQL database container
```
docker-compose up -d
```

The `-d` flag runs the container in the background.

After running this command the following happens automatically:
- the PostgreSQL image is downloaded (on first launch)
- the application database is created
- connection parameters are configured (DB name, user, password)
- tables are initialized

Check that the container is running:
```
docker ps
```
The list should show a container with the postgres image.

---

### Step 4 — Configure the database connection

The database connection parameters are stored in:
```
src/main/resources/application.yml
```

Make sure the parameters match those specified in docker-compose.yml:
```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/autosalon
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

If the parameters in docker-compose.yml differ — update application.yml accordingly.

---

### Step 5 — Start the server (Spring Boot)

**Option A — via Maven (recommended):**
```
mvn spring-boot:run
```

Maven will automatically download all dependencies and start the application.
On the first launch, downloading dependencies may take a few minutes.

**Option B — via compiled JAR:**
```
mvn clean package
java -jar target/autosalon-web-0.0.1-SNAPSHOT.jar
```

---

### Step 6 — Open the application in a browser
```
http://localhost:8080
```

Supported browsers: Google Chrome, Mozilla Firefox, Yandex Browser, Opera, Edge.

---

### Step 7 — Stop the container (when done)
```
docker-compose down
```

---

## 🗂️ Project Architecture
```
com.coursework.autosalon.web
│
├── model/                          # Domain entities (JPA classes)
│   ├── User                        # User
│   ├── Car                         # Car / Bolide
│   ├── Team                        # F1 Team
│   ├── Driver                      # Racing driver
│   ├── UserCartItem                # Cart item
│   └── PurchaseRequest             # Purchase request
│
├── repository/                     # Data access layer (Spring Data JPA)
│   ├── UserRepository
│   ├── CarRepository
│   ├── TeamRepository
│   ├── DriverRepository
│   ├── UserCartRepository
│   └── PurchaseRequestRepository
│
├── service/                        # Business logic
│   ├── User registration and validation
│   ├── Car filtering and search
│   ├── Cart management
│   ├── Purchase request processing
│   └── Car deletion validation
│
├── controller/                     # Web controllers
│   ├── AuthController              # /login, /register
│   ├── CarController               # Catalog, car card
│   ├── CartController              # /cart/add, /cart/remove
│   ├── OrderController             # /checkout
│   ├── AdminCarController          # Car CRUD (ADMIN)
│   ├── AdminUserController         # User management (ADMIN)
│   └── AdminRequestController      # Request management (ADMIN)
│
├── config/                         # Spring Security configuration
└── security/                       # Authorization and session mechanism

src/main/resources/
├── templates/                      # Thymeleaf HTML templates
│   ├── index.html                  # Home page
│   ├── login.html                  # Login page
│   ├── register.html               # Registration page
│   ├── catalog.html                # Catalog with filters
│   ├── car-card.html               # Car detail page
│   ├── cart.html                   # Cart page
│   ├── checkout.html               # Order confirmation
│   └── admin/                      # Admin panel pages
├── application.yml                 # Application and DB settings
└── docker-compose.yml              # Docker container configuration
```

---

## 🗃️ Database Structure
```
users                    # System users
├── id
├── last_name, first_name, middle_name
├── email
├── password_hash          # BCrypt hash only — plain text never stored
├── phone, date_of_birth, gender
└── role                   # USER or ADMIN

cars                     # Cars / Bolides
├── id, name, description, image
├── season, price, availability_status
├── team_id  ──────────────────────► teams.id
└── driver_id ─────────────────────► drivers.id

teams                    # Formula 1 Teams
└── id, name, country, founder, founding_year

drivers                  # Racing Drivers
└── id, name, nationality, team_id ──► teams.id

user_cart_items          # User cart
├── user_id ────────────────────────► users.id
└── car_id  ────────────────────────► cars.id

purchase_requests        # Purchase requests
├── id, request_number, date, status
├── price_snapshot         # Price locked at the time of the request
├── user_id ────────────────────────► users.id
└── car_id  ────────────────────────► cars.id
```

> ⚠️ A car cannot be deleted while it is present in active requests or a cart.
> This is enforced at the server business logic level.

---

## 🔐 Security
```
BCrypt hashing           │ Passwords stored as hashes only — never in plain text
Role-based access        │ USER and ADMIN roles — admin routes closed to regular users
SQL injection            │ JPA/Hibernate uses parameterized queries
XSS protection           │ Thymeleaf automatically escapes all output data
user_id binding          │ Users can only manage their own cart and requests
Environment variables    │ DB passwords set via ENV — never stored in source code
Docker isolation         │ PostgreSQL accessible only on local port
Logging                  │ All access to protected resources is logged
Sessions                 │ Terminated via /logout
```

---

## 👤 User Roles

### User (USER)
```
Registration:
  → Enter full name, date of birth, phone, gender, email, password
  → Click "Register" → redirect to login page

Login:
  → Enter email and password → click "Sign In"
  → Car catalog opens automatically

Catalog:
  → Set filters: season, manufacturer, price, availability
  → Click "Apply Filter"
  → Click "Details" on the desired car

Car Card:
  → View specifications, price and image
  → Click "Add to Cart"
  → Click "Catalog" to go back

Cart:
  → View added cars
  → Remove an item → "Delete"
  → Clear everything → "Clear Cart"
  → Go back → "Continue Shopping"
  → Submit → "Place Order"

After placing an order:
  → Car model, price, date and request number are recorded
  → Car status changes to "Unavailable"
  → Cart is cleared automatically
  → Success notification is displayed

Logout:
  → Click "Sign Out" → session ends → home page
```

### Administrator (ADMIN)
```
All USER capabilities, plus:

Catalog management:
  → View hidden cars
  → Add a new car (all fields)
  → Edit car (price, description, season, image)
  → Delete car (only if no active requests exist)

User management:
  → Full list of all users
  → User card with order history

Request management:
  → View all system requests
  → Delete items from requests (unavailable for completed requests)
  → Complete a request → car becomes available for deletion
```

---

## 💬 System Messages
```
Situation                                 │ System Response
──────────────────────────────────────────┼────────────────────────────────────────
Wrong email or password                   │ Auth error, prompt to try again
Car already in cart                       │ Duplicate warning
Car unavailable for purchase              │ Status "Unavailable" / "Sold"
Order placed successfully                 │ Notification with request number and date
Deleting a car with active request        │ Rejection with error message
No database connection                    │ Service unavailable message
Invalid form data                         │ Field highlighting with error hints
```

---

## ⚡ Non-functional Characteristics
```
Performance          │ Page load time — no more than 3 seconds under standard load
Data persistence     │ Cart stored in DB — survives page reload and server restart
Fault tolerance      │ Informative message shown when DB connection is lost
Scalability          │ Architecture allows adding features without system redesign
Maintainability      │ Layered architecture simplifies support and further development
```
