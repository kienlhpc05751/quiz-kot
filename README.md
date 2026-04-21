# Quiz-KOT

Quiz-KOT là một monorepo cho hệ thống tạo và làm bài quiz. Repo hiện tại gồm backend Spring Boot, frontend Next.js và PostgreSQL, kèm cấu hình Docker Compose để chạy toàn bộ stack.

## Tổng quan

- Backend: Spring Boot 4, Java 21, JPA/Hibernate, Spring Security
- Frontend: Next.js 16, React 19, TypeScript
- Database: PostgreSQL 15
- Orchestration: Docker Compose

## Cấu trúc repo

```text
quiz-kot/
├── database/
│   ├── 01-schema..sql
│   └── 02-data.sql
├── quiz-kot-backend/
│   └── quizkot/
│       ├── pom.xml
│       ├── Dockerfile
│       └── src/
├── quiz-kot-frontend/
│   ├── package.json
│   ├── Dockerfile
│   └── app/
├── docker-compose.yml
└── README.md
```

## Tính năng chính

- Đăng ký và đăng nhập người dùng
- Danh sách quiz và chi tiết quiz
- Tạo, sửa, quản lý quiz ở phía backend
- Làm bài quiz, lưu kết quả và xem summary
- Dashboard, share quiz và invite flow

## Công nghệ

### Backend

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- Spring Security
- PostgreSQL driver
- Lombok

### Frontend

- Next.js 16.2.4
- React 19.2.4
- TypeScript
- Tailwind CSS 4

## Chạy bằng Docker

Đây là cách chạy khuyến nghị.

### Yêu cầu

- Docker
- Docker Compose

### Khởi động

```bash
docker compose up --build
```

Sau khi chạy xong:

- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- PostgreSQL: localhost:5433

### Dừng stack

```bash
docker compose down
```

### Xoá luôn dữ liệu database

```bash
docker compose down -v
```

## Chạy local không dùng Docker

### Backend

```bash
cd quiz-kot-backend/quizkot
./mvnw spring-boot:run
```

Backend dùng cấu hình trong [application.properties](quiz-kot-backend/quizkot/src/main/resources/application.properties).

Các biến môi trường hỗ trợ:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/quiz_system
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
```

### Frontend

```bash
cd quiz-kot-frontend
npm install
npm run dev
```

Frontend dùng biến môi trường:

```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

## Database

Thư mục [database](database) chứa SQL schema và data seed cho PostgreSQL.

Trong Docker Compose, các file trong thư mục này được mount vào container Postgres để tự khởi tạo database khi volume còn trống.

## API chính

Theo backend hiện tại, các nhóm endpoint chính gồm:

- `/api/auth/login`
- `/api/auth/register`
- `/api/quizzes`
- `/api/quizzes/{id}`
- `/api/quizzes/{id}/questions`
- `/api/quizzes/{id}/attempts`
- `/api/quizzes/{id}/attempts/me`
- `/api/quizzes/{id}/share`
- `/api/quizzes/{id}/invites`
- `/api/dashboard/summary`
- `/api/dashboard/activity`
- `/api/users`

## Docker files

- Backend image: [quiz-kot-backend/quizkot/Dockerfile](quiz-kot-backend/quizkot/Dockerfile)
- Frontend image: [quiz-kot-frontend/Dockerfile](quiz-kot-frontend/Dockerfile)
- Compose stack: [docker-compose.yml](docker-compose.yml)

## Ghi chú cấu hình

- Backend đọc datasource từ biến môi trường và có giá trị mặc định cho chạy local.
- Frontend build theo kiểu Next.js standalone, nên container chạy trên cổng 3000.
- Compose hiện publish PostgreSQL ra cổng 5433 trên máy host để tránh đụng cổng 5432 nếu máy đã có PostgreSQL local.

## Kiểm thử

```bash
cd quiz-kot-backend/quizkot
./mvnw test
```

```bash
cd quiz-kot-frontend
npm run lint
```

## Tác giả

Le Hoang Kien
