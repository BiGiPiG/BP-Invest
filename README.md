# BP Invest 📈

[![Vue](https://img.shields.io/badge/Vue-3.x-%234FC08D?logo=vuedotjs)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-%236DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-%232496ED?logo=docker)](https://docker.com)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-%234169E1?logo=postgresql)](https://postgresql.org)

**BP Invest** — это веб-приложение для комплексного анализа акций. Приложение предоставляет инвесторам инструментарий для принятия инвестиционных решений на основе актуальных финансовых данных и AI-аналитики.

---

## ✨ Возможности

### 📊 Фронтенд (Vue.js)
- **Комплексный анализ мультипликаторов:** P/E, P/S, EPS и другие ключевые показатели
- **Интерактивные графики цен:** Динамика цен с выбором временных интервалов
- **AI-анализ акций:** Аналитика и прогнозы на основе искусственного интеллекта
- **Адаптивный интерфейс:** Оптимизирован для разных устройств

### 🔧 Бэкенд (Spring Boot)
- **REST API:** Документированное API для работы с данными
- **База данных:** Хранение пользовательских данных
- **Безопасность:** JWT, OAuth 2 аутентификация и авторизация

### 🛠️ Технологический стек
#### Frontend (Vue.js Ecosystem)
- Фреймворк: Vue 3
- Сборка: Vite
- Роутинг: Vue Router 4 с lazy loading
- Графики: Chart.js

#### Backend (Spring Boot Ecosystem)
- Фреймворк: Spring Boot 3.x + Java 17+
- Безопасность: Spring Security 6 + JWT + OAuth 2.0
- База данных: PostgreSQL + Hibernate/JPA
- Документация: OpenAPI 3 + Swagger UI
- Миграции: Liquibase
- Тестирование: JUnit 5 + Mockito + JaCoCo

#### Инфраструктура
- Контейнеризация: Docker + Docker Compose
- База данных: PostgreSQL 14+
- Сборка: Maven
- Code Quality: Lombok

---

## 🖼️ Пользователский интерфейс

| Страница анализа мультипликаторов | Интерактивный график цен |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/f43f6f6a-2ec1-4657-bd9f-99357cb662cd" width="400" alt="Анализ мультипликаторов"/> | <img src="https://github.com/user-attachments/assets/046dbd33-ef69-48aa-83c4-7ed87bbcb780" width="400" alt="График цен"/> |

| AI-анализ акций | Страница аутентификации |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/29968229-846d-487b-9eb6-74ca945be201" width="400" alt="AI-анализ"/> | <img width="400" alt="Страница аутентификации" src="https://github.com/user-attachments/assets/ee8fb46c-4124-48f8-86d9-d54148baabf6" />
 |

---

## 🏗️ Архитектура проекта

```mermaid
graph TB
    A[Пользователь] --> B[Vue.js Frontend]
    B --> C[Spring Boot Backend]
    C --> D[🧠 AI Analysis Service]
    C --> E[(💾 База данных<br/>PostgreSQL)]
    D --> F[AI Provider]
    C --> G[📈 API биржи]

    style A fill:#e1f5fe
    style B fill:#f3e5f5
    style C fill:#fff3e0
    style D fill:#e8f5e8
    style E fill:#fce4ec
    style F fill:#e8f5e8
```

## 🚀 Быстрый старт
### Предварительные требования
- Docker 28.3.2+
- Docker Compose 2.38.2+

### Запуск приложения
Клонируйте репозиторий:

```bash
git clone https://github.com/BiGiPiG/BP-Invest.git
cd BP-Invest
```

Отредактируйте application.properties файл, указав ваши API-ключи
Запустите все сервисы:

```bash
docker-compose up --build
```
