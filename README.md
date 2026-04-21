# 🚀 Quiz-KOT – Fullstack Quiz System

## 📌 Overview

**Quiz-KOT** là hệ thống tạo và làm bài quiz gồm:

* 🧠 Backend: Spring Boot (REST API + Authentication + Business logic)
* 🎨 Frontend: React (UI + Quiz interaction)
* 🗄️ Database: PostgreSQL / MySQL (tuỳ config)

Mục tiêu:

* Quản lý quiz, câu hỏi, người dùng
* Làm bài quiz theo thời gian thực
* Áp dụng JWT authentication + role-based authorization

---

## 🏗️ Project Structure

```
quiz-kot/
│
├── backend/              # Spring Boot API
├── frontend/             # React App
├── database/             # SQL schema + seed data
├── docs/                 # ERD, API docs
├── pythonquiz.json       # Sample quiz data
├── README.md
└── .gitignore
```

---

## ⚙️ Tech Stack

### Backend

* Java 17+
* Spring Boot
* Spring Security + JWT
* JPA / Hibernate
* Redis (optional)
* PostgreSQL / MySQL

### Frontend

* ReactJS
* Axios
* Tailwind CSS
* React Router

---

## 🔑 Features

### 👤 Authentication

* Đăng ký / đăng nhập
* JWT token
* Role-based (ADMIN / USER)

### 🧠 Quiz System

* Tạo quiz
* Làm quiz theo thời gian
* Submit & chấm điểm
* Random câu hỏi (optional)

### 📊 Admin

* Quản lý user
* CRUD quiz
* Phân quyền

---

## 🛠️ Setup & Run

## 1️⃣ Clone project

```bash
git clone https://github.com/your-username/quiz-kot.git
cd quiz-kot
```

---

## 2️⃣ Backend (Spring Boot)

### 📁 Di chuyển

```bash
cd backend
```

### ⚙️ Config `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quiz_system
spring.datasource.username=your_user
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_secret_key
jwt.expiration=86400000
```

### ▶️ Run

```bash
./mvnw spring-boot:run
```

👉 API chạy tại:

```
http://localhost:8080
```

---

## 3️⃣ Frontend (React)

### 📁 Di chuyển

```bash
cd frontend
```

### 📦 Install

```bash
npm install
```

### ▶️ Run

```bash
npm start
```

👉 App chạy tại:

```
http://localhost:3000
```

---

## 4️⃣ Database

### 📁 Thư mục

```
/database
```

### 📌 Bao gồm:

* Schema SQL
* Sample data
* Seed quiz

👉 Import:

```bash
psql -U postgres -d quiz_system -f database/schema.sql
```

---

## 🔌 API Example

### Login

```http
POST /api/auth/login
```

### Response

```json
{
  "token": "jwt_token_here",
  "roles": ["USER"]
}
```

---

### Get Quiz

```http
GET /api/quizzes/{id}
```

---

## 📦 Environment Variables

### Backend

```
DB_URL=
DB_USER=
DB_PASS=
JWT_SECRET=
```

### Frontend

```
REACT_APP_API_URL=http://localhost:8080
```

---

## 🧪 Testing

Backend:

```bash
mvn test
```

Frontend:

```bash
npm test
```

---

## 🚀 Deployment (Future)

* Backend: Docker / VPS
* Frontend: Vercel / Netlify
* Database: Cloud PostgreSQL
* CI/CD: GitHub Actions

---

## 📚 Documentation

📁 `/docs`

* ERD Diagram
* API Spec
* Flow system

---

## 🧠 Roadmap

* [ ] Timer toàn bài quiz
* [ ] Realtime (WebSocket)
* [ ] Leaderboard
* [ ] AI generate câu hỏi
* [ ] Mobile responsive UI

---

## 🤝 Contributing

1. Fork repo
2. Create branch: `feature/your-feature`
3. Commit
4. Push & Pull Request

---

## 📄 License

MIT License

---

## 👨‍💻 Author

**Le Hoang Kien**

---

## 💡 Notes

* Đây là project học tập + portfolio
* Có thể mở rộng thành SaaS Quiz Platform

---

🔥 *If you find this useful, give it a ⭐ on GitHub!*
