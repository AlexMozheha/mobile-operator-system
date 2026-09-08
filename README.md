# Mobile Operator System

A distributed, microservice-based system simulating core mobile network operator operations, billing, CRM features, and self-service user portals. 
The project is fully containerized and ready for local development.

## Architecture & Tech Stack

The system is built as a multi-module Java project utilizing a microservices architecture:

*   **Backend:** Java 21, Spring Boot, Maven 3.8 (Multi-module `pom.xml`)
*   **Services:**
    *   `auth-service` – Handles token-based user authentication and security (will be implemented in the future).
    *   `billing-service` – Manages financial transactions, balances, and tariff charges.
    *   `crm-service` – Controls customer data, profiles, and administration.
    *   `self-service-portal` & `operator-frontend` – User and operator web interfaces.
    *   `validation` – Shared data validation routines.
*   **Infrastructure:** PostgreSQL (Database), HashiCorp Consul 1.22.7 (Service Discovery / Service Registry).
*   **DevOps & Orchestration:** Docker, Docker Compose (with healthcheck orchestration).

## Business Logic
Tariff changes go through billing-service: the customer is validated via CRM, an invoice is issued and paid against their balance (rejected if funds are insufficient), and usage quotas (minutes/SMS/data) are updated to match the new tariff.

### Prerequisites
Make sure you have the following installed:
*   Docker Desktop (with Docker Compose v2+)
*   Java 21 (for local development)
*   Maven 3.8 or higher

### Environment Setup
The project uses an `.env` file to inject dynamic configurations into containerized environments.
Open .env and adjust the variables (ports, database credentials) if necessary.

### Run the whole system

```bash
git clone https://github.com/AlexMozheha/mobile-operator-system.git
cd mobile-operator-system
docker compose up --build
```

