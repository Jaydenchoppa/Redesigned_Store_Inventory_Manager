# Store Inventory Manager

A full-stack store inventory management system built with a **Spring Boot** REST API backend and a **Java Swing** desktop client frontend. The system supports two types of users — **Employees**, who manage inventory and view order history, and **Customers**, who browse products by category and make purchases.

## Project Structure

This is a Maven multi-module project with two modules:

```
Store_Inventory_Manager_Backend/
├── backend/    → Spring Boot REST API (Java, JPA/Hibernate, H2 database)
├── frontend/   → Java Swing desktop client (consumes the backend via HTTP)
└── data/       → Local H2 database files (generated at runtime)
```

The `backend` and `frontend` modules are independent — the frontend communicates with the backend exclusively through HTTP calls to `http://localhost:8080`, the same way a web or mobile client would. Both must be running for the application to function.

## Features

### Employee (Staff)
- Login with username/password
- Register new employee accounts (only accessible once logged in)
- Add and delete inventory items (product, category, supplier, quantity, unit price, location)
- View a full history of customer orders — who bought what, how much, and when

### Customer
- Sign up and log in with username/password
- Browse available products, filterable by category
- Select a quantity and purchase an item
- Purchases automatically reduce stock and are recorded as an order

## Tech Stack

**Backend**
- Java 21
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- H2 Database (file-based, embedded)
- Maven

**Frontend**
- Java 21
- Java Swing
- Jackson (JSON serialization)
- `java.net.http.HttpClient` for REST calls

## Domain Model

| Entity | Description |
|---|---|
| `Employee` | Staff accounts with login credentials, used for inventory management |
| `Customer` | Shopper accounts with login credentials and contact details |
| `Product` | A sellable item, identified by product ID |
| `Supplier` | The supplier of a product |
| `Category` | Embedded within `Inventory` — a product grouping (e.g. Peripherals, Furniture) |
| `Inventory` | A stock record linking a Product, Supplier, and Category, with quantity, price, and location |
| `Order` | A record of a completed purchase — customer, item, quantity, total, and status |

## Getting Started

### Prerequisites
- JDK 21
- Maven
- IntelliJ IDEA (recommended) or another Java IDE

### Running the backend
1. Open the `backend` module in your IDE
2. Run `RedesignedStoreInventoryManagerApplication.java`
3. The API starts on `http://localhost:8080`
4. The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/storeDB`)

### Running the frontend
1. Open the `frontend` module in your IDE
2. Run `ClientApp.java`
3. The Login window opens, with a toggle to log in as either an **Employee** or a **Customer**

> The backend must be running before the frontend, since the Swing client depends on the API being reachable.

### First-time setup
On first start the backend creates a default employee automatically (see `DataInitializer`):

- Username: `admin`
- Password: `admin123`

Log in as an **Employee** with those details. **Change this default password in `DataInitializer` before sharing or deploying the project.** Passwords are stored BCrypt-hashed, so inserting a plaintext row through the H2 console will not work.

Further employees are added from the Inventory screen via **Employees → Add** (username, password, name, position, salary). Customers sign up from the login screen. Every employee screen (Order History, Suppliers, Customers, Employees) is opened with the buttons across the top of the Inventory screen, and **Log out** returns to the login screen.

## API Overview

| Endpoint | Purpose |
|---|---|
| `POST /api/auth/login` | Employee login (returns the account's role) |
| `POST /api/customer-auth/login`, `/register` | Customer authentication |
| `GET / POST / PUT / DELETE /api/inventory` | Inventory CRUD |
| `GET /api/orders` | Order history |
| `POST /api/purchase` | Customer purchase (decrements stock, records an order) |
| `GET / POST / DELETE /api/customers` | Customer records |
| `GET /api/v1/employee/getAll`, `POST /save`, `DELETE /delete?id=` | Employee records and creation |
