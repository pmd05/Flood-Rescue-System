# 🌊 Flood Rescue System

### Hệ Thống Điều Phối Cứu Hộ Lụt Bão

## 📌 Giới thiệu

**Flood Rescue System** là hệ thống hỗ trợ điều phối cứu hộ trong tình huống khẩn cấp như lũ lụt, thiên tai.
Hệ thống cho phép quản lý yêu cầu cứu trợ, phân công đội cứu hộ và theo dõi trạng thái xử lý theo thời gian thực.

Dự án được xây dựng theo mô hình **Full-stack**:

* 🔧 Backend: Spring Boot (REST API + JWT Security)
* 🌐 Frontend: HTML, CSS, JavaScript
* 🗄️ Database: SQL Server

---

## 🏗️ Kiến trúc hệ thống

```
Flood-Rescue-System/
│
├── backend/        # Spring Boot API + Business Logic
├── frontend/       # Giao diện người dùng
├── database/       # Script SQL
└── README.md
```

---

## ⚙️ Công nghệ sử dụng

| Thành phần | Công nghệ             |
| ---------- | --------------------- |
| Backend    | Java, Spring Boot     |
| Security   | JWT Authentication    |
| Database   | SQL Server            |
| Frontend   | HTML, CSS, JavaScript |
| Build Tool | Maven                 |

---

## 📋 Yêu cầu hệ thống

Trước khi chạy project, cần cài đặt:

* Java JDK 17+
* Apache Maven
* SQL Server (SSMS)
* Visual Studio Code (khuyến nghị)

---

## 🚀 Hướng dẫn chạy dự án

### 🔹 1. Cấu hình Database

Mở SQL Server và chạy:

```sql
CREATE DATABASE flood_rescue_db;
```

Cập nhật thông tin trong file:

```
backend/src/main/resources/application.properties
```

Ví dụ:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=flood_rescue_db
spring.datasource.username=sa
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_secret_key
```

---

### 🔹 2. Chạy Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

👉 Server chạy tại:

```
http://localhost:8080
```

---

### 🔹 3. Chạy Frontend

**Cách 1 (Khuyên dùng):**

* Mở thư mục `frontend` bằng VS Code
* Cài extension **Live Server**
* Chuột phải `login.html` → *Open with Live Server*

**Cách 2:**

* Mở trực tiếp file `login.html` bằng trình duyệt

---

## 🔐 Xác thực & Phân quyền

Hệ thống sử dụng **JWT Authentication**:

### 🔑 Quy trình:

1. User đăng nhập → nhận token
2. Gửi token trong header:

```
Authorization: Bearer <token>
```

3. Backend xác thực token trước khi xử lý request

---

## 👥 Vai trò người dùng

| Role        | Mô tả            |
| ----------- | ---------------- |
| Admin       | Quản lý hệ thống |
| Coordinator | Điều phối cứu hộ |
| Rescue Team | Đội cứu hộ       |
| Citizen     | Người dân        |

---

## 📡 API cơ bản

### 🔹 Đăng nhập

```
POST /api/auth/login
```

### 🔹 Gọi API có bảo mật

```
Authorization: Bearer <token>
```

---

## 📌 Tính năng chính

* ✅ Quản lý yêu cầu cứu hộ
* ✅ Phân công đội cứu hộ
* ✅ Theo dõi trạng thái xử lý
* ✅ Xác thực JWT
* ✅ Phân quyền người dùng

---

## 📷 Demo

> (Có thể thêm ảnh hoặc video demo tại đây)

---

## 🧹 Ghi chú

* Không commit các file:

  * `.idea`
  * `.vscode`
  * `target`
  * `out`

---

🚀 **Chúc bạn demo thành công!**
