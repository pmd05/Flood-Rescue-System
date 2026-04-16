IF DB_ID('flood_rescue_db') IS NULL
BEGIN
    CREATE DATABASE flood_rescue_db;
END
GO

USE flood_rescue_db;
GO

IF OBJECT_ID('inventory_logs', 'U') IS NOT NULL DROP TABLE inventory_logs;
IF OBJECT_ID('assignments', 'U') IS NOT NULL DROP TABLE assignments;
IF OBJECT_ID('vehicles', 'U') IS NOT NULL DROP TABLE vehicles;
IF OBJECT_ID('rescue_requests', 'U') IS NOT NULL DROP TABLE rescue_requests;
IF OBJECT_ID('relief_items', 'U') IS NOT NULL DROP TABLE relief_items;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
IF OBJECT_ID('rescue_teams', 'U') IS NOT NULL DROP TABLE rescue_teams;
IF OBJECT_ID('roles', 'U') IS NOT NULL DROP TABLE roles;
GO

CREATE TABLE roles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL UNIQUE
);
GO

CREATE TABLE rescue_teams (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE
);
GO

CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    role_id BIGINT,
    team_id BIGINT NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_users_team FOREIGN KEY (team_id) REFERENCES rescue_teams(id)
);
GO

CREATE TABLE rescue_requests (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    description NVARCHAR(500),
    latitude FLOAT,
    longitude FLOAT,
    status NVARCHAR(50),
    priority NVARCHAR(50),
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_requests_user FOREIGN KEY (user_id) REFERENCES users(id)
);
GO

CREATE TABLE vehicles (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    status NVARCHAR(50)
);
GO

CREATE TABLE assignments (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    request_id BIGINT,
    team_id BIGINT,
    vehicle_id BIGINT,
    status NVARCHAR(50),
    assigned_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_assignments_request FOREIGN KEY (request_id) REFERENCES rescue_requests(id),
    CONSTRAINT fk_assignments_team FOREIGN KEY (team_id) REFERENCES rescue_teams(id),
    CONSTRAINT fk_assignments_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);
GO

CREATE TABLE relief_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 0
);
GO

CREATE TABLE inventory_logs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    item_id BIGINT,
    change_amount INT,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_inventory_logs_item FOREIGN KEY (item_id) REFERENCES relief_items(id)
);
GO

INSERT INTO roles (name) VALUES
('citizen'),
('rescue_team'),
('coordinator'),
('manager'),
('admin');
GO

INSERT INTO rescue_teams (name) VALUES
('Team A'),
('Team B');
GO

INSERT INTO users (username, password, role_id, team_id) VALUES
('citizen1', '123', 1, NULL),
('team1', '123', 2, 1),
('coordinator1', '123', 3, NULL),
('manager1', '123', 4, NULL),
('admin1', '123', 5, NULL);
GO

INSERT INTO vehicles (name, status) VALUES
('Boat 1', 'available'),
('Truck 1', 'available');
GO

INSERT INTO relief_items (name, quantity) VALUES
('Water', 100),
('Food', 200);
GO

CREATE INDEX idx_requests_status ON rescue_requests(status);
CREATE INDEX idx_requests_user ON rescue_requests(user_id);
CREATE INDEX idx_users_team ON users(team_id);
CREATE INDEX idx_assignments_request_status ON assignments(request_id, status);
CREATE INDEX idx_assignments_team_status ON assignments(team_id, status);
CREATE INDEX idx_assignments_vehicle_status ON assignments(vehicle_id, status);
GO
