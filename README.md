lood Rescue System - Hệ Thống Điều Phối Căn Cứ Cứu Hộ Lụt Bão

Đây là mã nguồn đầy đủ của Hệ thống Điều phối Cứu hộ Khẩn cấp. Hệ thống được chia làm hai phần chính: **Backend** (xử lý logic, API tích hợp) và **Frontend** (giao diện người dùng Premium với bản đồ tương tác).

## 📋 Yều Cầu Hệ Thống (Prerequisites)

Trước khi chạy dự án, máy tính của bạn cần cài đặt:
1. **Java Development Kit (JDK 17 trở lên)**.
2. **Apache Maven** (để build backend).
3. **Microsoft SQL Server** (hệ quản trị cơ sở dữ liệu).
4. **Visual Studio Code** (hoặc trình duyệt để chạy Frontend, khuyến nghị dùng extension *Live Server*).

---

## ⚙️ Hướng Dẫn Cài Đặt & Chạy Dự Án

### BƯỚC 1: Thiết Lập Cơ Sở Dữ Liệu (SQL Server)
Hệ thống sử dụng SQL Server để lưu trữ toàn bộ dữ liệu. Bộ mã nguồn đã được cấu hình tự động tạo bảng (tự động hóa Hibernate), nên bạn chỉ cần tạo một Database rỗng là đủ.
1. Mở **SQL Server Management Studio (SSMS)**.
2. Chọn kết nối Database Engine bằng tài khoản `sa` (SQL Server Authentication).
3. Chạy lệnh SQL sau để tạo Database:
   ```sql
   CREATE DATABASE flood_rescue_db;
   ```
4. Đảm bảo cấu hình SQL Server của bạn có thông tin đăng nhập khớp với tệp `backend/src/main/resources/application.properties` bảo gồm:
   - Username: `sa`
   - Password: `Sa@12345678` 
   (*Nếu mật khẩu máy bạn khác, hãy sửa lại tệp `application.properties` cho khớp*).

### BƯỚC 2: Khởi Động Máy Chủ (Backend Spring Boot)
1. Mở Terminal (Command Prompt / PowerShell) và điều hướng vào thư mục backend:
   ```bash
   cd flood-rescue-system-core/backend
   ```
2. Build (biên dịch) toàn bộ mã nguồn sử dụng Maven:
   ```bash
   mvn clean package -DskipTests
   ```
3. Chạy máy chủ Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
   > **Lưu ý:** Server sẽ chạy tại địa chỉ `http://localhost:8080`. Hãy chắc chắn cổng 8080 trên máy của bạn hiện đang không bị phần mềm khác khóa. Máy chủ cần được treo (chạy ngầm) trong suốt quá trình bạn dùng Frontend.

### BƯỚC 3: Mở Giao Diện Người Dùng (Frontend)
Frontend bao gồm các tệp HTML thuần kết hợp CSS/JS (không cần cài Node.js rườm rà). Bạn có hai cách để chạy:

**Cách 1 (Khuyên Dùng - Tránh lỗi CORS tĩnh):**
1. Mở thư mục `flood-rescue-system-core/frontend` bằng trình soạn thảo mã **Visual Studio Code (VS Code)**.
2. Cài đặt tiện ích mở rộng (Extension) tên là **Live Server** của Ritwick Dey.
3. Nhấn chuột phải vào tệp `login.html`, sau đó chọn `Open with Live Server`.
4. Trình duyệt ảo sẽ lập tức bung ra giao diện.

**Cách 2 (Mở trực tiếp):**
Đi thẳng vào thư mục `frontend/` và nhấp đúp tệp (Double-Click) `login.html` để chạy trực tiếp trên trình duyệt Chrome/Edge của bạn.

---

## 👥 Cấu Trúc Phân Quyền Vai Trò
Sau khi khởi động lên, do CSDL mới tinh nên chưa có tài khoản. Hệ thống sử dụng cơ chế cấp quyền theo **Mã Đặc Vụ (Role)**. 

Bởi vì bạn cần khởi tạo dữ liệu ban đầu, bạn hãy gọi trực tiếp API hoặc thêm một dòng SQL insert đầu tiên cho một User Admin, sau đó sử dụng User admin này để khởi tạo thêm các tài khoản Citizen (Người Dân), Coordinator (Điều phối viên) hoặc Rescue_Team (Đội cấp cứu).

(Hãy tham khảo các API mapping trong thư mục Controller tại `backend` để tự do thao tác nhé).

Chúc bạn thành công! 🚀
