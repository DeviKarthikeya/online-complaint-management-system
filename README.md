# 📝 Online Complaint Management System (OCMS)

An end-to-end **Online Complaint Management System** built using **Spring Boot**, **Thymeleaf**, **PostgreSQL**, and **Bootstrap**.  
The system allows users to raise complaints and admins to manage, respond, and track complaint statuses efficiently.

---

### Application runs at

https://ocms-3qk4.onrender.com


## 🚀 Features

### 👤 User Module
- User registration & login
- Raise complaints
- View complaint status (OPEN / IN PROGRESS / CLOSED)
- Read admin replies
- Close or reopen complaints
- Export complaints as CSV

### 🛠️ Admin Module
- View all complaints
- Filter complaints by status & username
- Reply to complaints
- Close complaints
- Dashboard with status summary cards
- Export complaints as CSV

---

## 🎨 UI Highlights
- Responsive dashboards (Admin & User)
- Color-coded status cards:
  - 🔵 Total / Summary
  - 🟢 OPEN
  - 🟡 IN PROGRESS
  - 🔴 CLOSED
- Bootstrap 5 + Icons
- Custom theme support

---

## 🧑‍💻 Tech Stack

| Layer        | Technology |
|--------------|------------|
| Backend      | Spring Boot 3.x |
| Frontend     | Thymeleaf, Bootstrap 5 |
| Database     | PostgreSQL |
| ORM          | Spring Data JPA (Hibernate) |
| Build Tool   | Maven |
| Deployment   | Render (Docker) |
| Java Version | Java 17 |

---

## 🗂️ Project Structure

```text
ComplaintManagementSystem
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── complaintmanagement
│   │   │               ├── controller
│   │   │               ├── service
│   │   │               ├── repository
│   │   │               └── model
│   │   └── resources
│   │       ├── templates
│   │       ├── static
│   │       │   ├── css
│   │       │   └── js
│   │       └── application.properties
├── Dockerfile
├── pom.xml
└── README.md

---


## ⚙️ Environment Variables (Render)

Configure the following Environment Variables in Render:

SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>:5432/<DB_NAME>
SPRING_DATASOURCE_USERNAME=<DB_USERNAME>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

PORT=8080


⚠️ **Do not hardcode database credentials in application.properties for production.**

---

## 🐳 Docker Support

This project uses Docker for deployment.

### Dockerfile (Overview)
- Java 17 (Eclipse Temurin)
- Maven build inside container
- Runs Spring Boot JAR on port `8080`

---

## ▶️ Run Locally

### Prerequisites
- Java 17
- Maven
- PostgreSQL


### 🌐 Deployment

Deployed on Render

Uses Docker-based deployment

PostgreSQL hosted on Render

### 🔒 Security Notes

Session-based authentication

Role-based access (Admin / User)

Environment-based configuration

### 🧑‍🎓 Author
K.Devi Karthikeya
📍 KL University
🎯 Java | Spring Boot | Full-Stack Developer
