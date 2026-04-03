-- XÓA DATABASE
DROP DATABASE IF EXISTS rescue_db;

-- TẠO DATABASE NẾU CHƯA CÓ
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'rescue_db')
BEGIN
    CREATE DATABASE rescue_db;
END
GO

USE rescue_db;
GO

-- XÓA CÁC TABLE CŨ
DROP TABLE IF EXISTS inventory_logs;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS relief_items;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS rescue_teams;
DROP TABLE IF EXISTS rescue_requests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- ===== 1. ROLES =====
CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE
);

-- ===== 2. USERS =====
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL, 
    role_id BIGINT NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_users_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ===== 3. RESCUE REQUESTS =====
CREATE TABLE rescue_requests (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    description NVARCHAR(500) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    rescue_status NVARCHAR(50) NOT NULL DEFAULT N'pending', -- pending, verified, assigned, completed
    priority NVARCHAR(50) NOT NULL DEFAULT N'medium', -- low, medium, high
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_requests_users FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===== 4. RESCUE TEAMS =====
CREATE TABLE rescue_teams (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE()
);

-- ===== 5. VEHICLES =====
CREATE TABLE vehicles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    vehicle_status NVARCHAR(50) NOT NULL DEFAULT N'available' -- available, in_use
);

-- ===== 6. ASSIGNMENTS =====
CREATE TABLE assignments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    request_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    vehicle_id BIGINT NULL,
    assignment_status NVARCHAR(50) NOT NULL DEFAULT N'pending',
    CONSTRAINT FK_assignments_requests FOREIGN KEY (request_id) REFERENCES rescue_requests(id),
    CONSTRAINT FK_assignments_teams FOREIGN KEY (team_id) REFERENCES rescue_teams(id),
    CONSTRAINT FK_assignments_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- ===== 7. RELIEF ITEMS =====
CREATE TABLE relief_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 0
);

-- ===== 8. INVENTORY LOGS =====
CREATE TABLE inventory_logs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    item_id BIGINT NOT NULL,
    change_amount INT NOT NULL, -- dương: nhập, âm: xuất
    description NVARCHAR(255),
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_logs_items FOREIGN KEY (item_id) REFERENCES relief_items(id)
);

-- ===== INDEXES =====
CREATE INDEX idx_requests_user ON rescue_requests(user_id);
CREATE INDEX idx_assignments_request ON assignments(request_id);

-- ===== SEED DATA =====
-- Roles
INSERT INTO roles (name) VALUES 
(N'citizen'), (N'rescue_team'), (N'coordinator'), (N'manager'), (N'admin');

-- Users
INSERT INTO users (username, password, role_id) VALUES 
('citizen1', '123', 1),
('team1', '123', 2),
('coordinator1', '123', 3),
('manager1', '123', 4),
('admin1', '123', 5);

-- Rescue Teams
INSERT INTO rescue_teams (name) VALUES 
(N'Đội Đặc Nhiệm A'), 
(N'Đội Phản Ứng Nhanh B');

-- Vehicles
INSERT INTO vehicles (name, vehicle_status) VALUES 
(N'Cano 01', N'available'), 
(N'Xe Tải Cứu Trợ 02', N'available');

-- Relief Items
INSERT INTO relief_items (name, quantity) VALUES 
(N'Nước suối (thùng)', 500), 
(N'Mì tôm (thùng)', 1000);

-- Rescue Requests (Test)
INSERT INTO rescue_requests (user_id, description, latitude, longitude, rescue_status, priority) VALUES
(1, N'Nước dâng cao tại khu dân cư A, cần hỗ trợ di dời', 10.762622, 106.660172, N'pending', N'high');

-- Inventory Logs
INSERT INTO inventory_logs (item_id, change_amount, description) VALUES
(1, 500, N'Nhập kho đợt 1'),
(2, 1000, N'Nhập kho đợt 1');