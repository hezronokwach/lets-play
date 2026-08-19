# LetsPlay API - RESTful Microservice

A RESTful web API built with Spring Boot, MongoDB, and Spring Security. This application implements user authentication using JSON Web Tokens (JWT), role-based permissions, resource ownership checks, and an automated Swagger documentation interface.

---

## Tech Stack and Features

* **Framework:** Spring Boot 3 (Java 21)
* **Security:** Spring Security with JWT (JJWT library)
* **Database:** MongoDB (Spring Data MongoDB)
* **Documentation:** OpenAPI 3 / Swagger UI
* **Build Tool:** Maven

### Key Capabilities

* **Stateless Token Authentication:** Users register and log in to receive a secure token. This token is sent with future requests to prove who you are.
* **Role-Based Access Control:** Distinguishes between standard users (`ROLE_USER`) and administrators (`ROLE_ADMIN`).
* **Resource Ownership Checks:** Standard users can only modify or delete products that they created. Administrators can manage any product.
* **Public Product Viewing:** Anyone can view the list of products without needing to register or log in.
* **Clean Error Handling:** Errors return standard messages without exposing internal database details or software errors.

---

## Access Permissions Summary

* **Public (No token needed):**
* Registering a new account (`POST /api/auth/register`)
* Logging into an account (`POST /api/auth/login`)
* Viewing all products (`GET /api/products`)
* Viewing a single product (`GET /api/products/{id}`)


* **Authenticated User (`ROLE_USER` or `ROLE_ADMIN`):**
* Creating a product (`POST /api/products`)


* **Product Owner or Admin:**
* Updating a product (`PUT /api/products/{id}` or `PATCH /api/products/{id}`)
* Deleting a product (`DELETE /api/products/{id}`)


* **Admin Only (`ROLE_ADMIN`):**
* Managing users (`GET /api/users`)



---

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 21 installed.
* Maven installed (or use the provided `./mvnw` script).

### Running the Application

1. Open your terminal in the project root directory.
2. Build and run the project:
```bash
./mvnw spring-boot:run

```


3. The server will start on `http://localhost:8080`.

---

## API Reference and Test Data Guide

This section lists every available API endpoint and the exact data format required to test it.

### 1. Authentication Endpoints

#### Register a Standard User

* **HTTP Method:** `POST`
* **URL:** `http://localhost:8080/api/auth/register`
* **Header:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "Password123!",
  "role": "ROLE_USER"
}

```



#### Register an Admin User

* **HTTP Method:** `POST`
* **URL:** `http://localhost:8080/api/auth/register`
* **Header:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "email": "admin.user@example.com",
  "password": "AdminPassword123!",
  "role": "ROLE_ADMIN"
}

```



#### Log In (Get Token)

* **HTTP Method:** `POST`
* **URL:** `http://localhost:8080/api/auth/login`
* **Header:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "Password123!"
}

```


* **Sample Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSI..."
}

```


*(Note: Copy the token string returned here. You will need it to authorize protected requests.)*

---

### 2. Product Endpoints

#### View All Products

* **HTTP Method:** `GET`
* **URL:** `http://localhost:8080/api/products`
* **Authorization:** None required
* **Request Body:** None

#### View a Single Product by ID

* **HTTP Method:** `GET`
* **URL:** `http://localhost:8080/api/products/65f1a2b3c4d5e6f7a8b9c0d1`
* **Authorization:** None required
* **Request Body:** None

#### Create a Product

* **HTTP Method:** `POST`
* **URL:** `http://localhost:8080/api/products`
* **Authorization:** Bearer Token required
* **Header:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "name": "Wireless Gaming Mouse",
  "description": "Ergonomic optical mouse with customizable DPI settings",
  "price": 49.99
}

```



#### Replace/Full Update a Product

* **HTTP Method:** `PUT`
* **URL:** `http://localhost:8080/api/products/65f1a2b3c4d5e6f7a8b9c0d1`
* **Authorization:** Bearer Token required (must be owner or admin)
* **Header:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "name": "Pro Wireless Gaming Mouse",
  "description": "Updated ergonomic mouse with 80-hour battery life",
  "price": 69.99
}

```



#### Partial Update a Product

* **HTTP Method:** `PATCH`
* **URL:** `http://localhost:8080/api/products/65f1a2b3c4d5e6f7a8b9c0d1`
* **Authorization:** Bearer Token required (must be owner or admin)
* **Header:** `Content-Type: application/json`
* **Request Body:**
```json
{
  "price": 39.99
}

```



#### Delete a Product

* **HTTP Method:** `DELETE`
* **URL:** `http://localhost:8080/api/products/65f1a2b3c4d5e6f7a8b9c0d1`
* **Authorization:** Bearer Token required (must be owner or admin)
* **Request Body:** None

---

## How to Test Using Swagger UI (Web Browser)

Swagger UI provides an interactive web webpage to test all endpoints without installing extra software.

1. Ensure the application is running.
2. Open your web browser and go to:
   `http://localhost:8080/swagger-ui/index.html`
3. **Step 1 - Register:**
* Click on `POST /api/auth/register`.
* Click **Try it out**.
* Replace the box contents with the registration JSON provided in the API reference section above.
* Click **Execute**.


4. **Step 2 - Log In:**
* Click on `POST /api/auth/login`.
* Click **Try it out**.
* Enter your email and password, then click **Execute**.
* In the Response Body below, copy the long text string inside `"token": "..."` (do not copy the quotation marks).


5. **Step 3 - Authorize:**
* Scroll to the top of the Swagger page and click the green **Authorize** button on the right.
* Paste your copied token into the **Value** box.
* Click **Authorize**, then click **Close**.


6. **Step 4 - Test Protected Actions:**
* You can now open any endpoint (like `POST /api/products`), click **Try it out**, paste the sample product JSON, and click **Execute**.



---

## How to Test Using Postman

1. Open Postman.
2. Create a new request and select the matching HTTP method (`GET`, `POST`, `PUT`, `DELETE`).
3. Enter the URL (e.g., `http://localhost:8080/api/auth/login`).
4. For requests requiring data (`POST`, `PUT`, `PATCH`):
* Go to the **Body** tab.
* Select **raw**.
* Change the dropdown on the far right from `Text` to `JSON`.
* Copy and paste the corresponding JSON snippet from the guide above.


5. For requests requiring authentication:
* Go to the **Authorization** tab in Postman.
* Change **Type** to **Bearer Token**.
* Paste the token string received from logging in into the **Token** field.


6. Click **Send**.