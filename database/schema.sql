-- ============================================================================
-- Horizon Store System - database schema (MySQL 8+)
--
-- Run:   mysql -u root -p < database/schema.sql
-- Then:  mysql -u root -p < database/sample_data.sql     (optional demo data)
--
-- To start over from scratch, uncomment the next line (DELETES ALL DATA):
-- DROP DATABASE IF EXISTS HorizonDB;
--
-- NOTE: table names are case-sensitive on Linux/macOS MySQL. The Java code uses
-- exactly the names below, so do not change their capitalisation.
-- ============================================================================

CREATE DATABASE IF NOT EXISTS HorizonDB CHARACTER SET utf8mb4;
USE HorizonDB;

-- ── Catalog & stock ─────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS Category (
    Category_ID    INT AUTO_INCREMENT PRIMARY KEY,
    Category_Name  VARCHAR(100) NOT NULL UNIQUE,
    Description    VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Product (
    Product_ID       INT AUTO_INCREMENT PRIMARY KEY,
    Product_Name     VARCHAR(150) NOT NULL,
    Brand            VARCHAR(100),
    Price            DECIMAL(10,2) NOT NULL CHECK (Price >= 0),
    Warranty_Period  INT NOT NULL DEFAULT 0 CHECK (Warranty_Period >= 0),  -- months
    Category_ID      INT NOT NULL,
    FOREIGN KEY (Category_ID) REFERENCES Category (Category_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Warehouse (
    Warehouse_ID       INT AUTO_INCREMENT PRIMARY KEY,
    Location           VARCHAR(150) NOT NULL,
    Maximum_Storage    INT NOT NULL CHECK (Maximum_Storage > 0),
    Available_Storage  INT NOT NULL CHECK (Available_Storage >= 0)
) ENGINE=InnoDB;

-- One stock row per product (the Products screen joins on Product_ID).
CREATE TABLE IF NOT EXISTS Inventory (
    Inventory_ID         INT AUTO_INCREMENT PRIMARY KEY,
    Inventory_Date       DATE NOT NULL,
    Quantity_Available   INT NOT NULL DEFAULT 0 CHECK (Quantity_Available >= 0),
    Minimum_Stock_Level  INT NOT NULL DEFAULT 0 CHECK (Minimum_Stock_Level >= 0),
    Product_ID           INT NOT NULL UNIQUE,
    Warehouse_ID         INT NOT NULL,
    FOREIGN KEY (Product_ID)   REFERENCES Product (Product_ID)     ON DELETE CASCADE,
    FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse (Warehouse_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS StockMovement (
    Movement_ID    INT AUTO_INCREMENT PRIMARY KEY,
    Movement_Type  VARCHAR(20) NOT NULL,          -- 'IN' or 'OUT'
    Quantity       INT NOT NULL CHECK (Quantity > 0),
    Movement_Date  DATE NOT NULL,
    Inventory_ID   INT NOT NULL,
    FOREIGN KEY (Inventory_ID) REFERENCES Inventory (Inventory_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ── Employees (with Technician / Sales / Warehouse subtypes) ────────────────

-- Employee_ID is typed in by the user on the Employees screen, so it is NOT auto-generated.
CREATE TABLE IF NOT EXISTS Employee (
    Employee_ID  INT PRIMARY KEY,
    First_Name   VARCHAR(60) NOT NULL,
    Last_Name    VARCHAR(60) NOT NULL,
    shift        VARCHAR(20) NOT NULL,
    Salary       DECIMAL(10,2) NOT NULL CHECK (Salary >= 0),
    Address      VARCHAR(255),
    Manager_ID   INT NULL,
    Hire_Date    DATE NOT NULL,
    FOREIGN KEY (Manager_ID) REFERENCES Employee (Employee_ID) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS EmployeePhone (
    Employee_ID   INT NOT NULL,
    Phone_Number  VARCHAR(30) NOT NULL,
    PRIMARY KEY (Employee_ID, Phone_Number),
    FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Technician (
    Employee_ID          INT PRIMARY KEY,
    Specialty            VARCHAR(100),
    Certification_Level  VARCHAR(50),
    FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Sales_Employee (
    Employee_ID       INT PRIMARY KEY,
    Sales_Commission  DECIMAL(10,2) NOT NULL DEFAULT 0,   -- percent
    Sales_Target      DECIMAL(12,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Warehouse_Employee (
    Employee_ID         INT PRIMARY KEY,
    Forklift_Certified  BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ── Customers ───────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS Customer (
    Customer_ID         INT AUTO_INCREMENT PRIMARY KEY,
    First_Name          VARCHAR(60) NOT NULL,
    Last_Name           VARCHAR(60) NOT NULL,
    Customer_BirthDate  DATE,
    Customer_Email      VARCHAR(150) UNIQUE,
    Address             VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS CustomerPhone (
    Customer_ID   INT NOT NULL,
    Phone_Number  VARCHAR(30) NOT NULL,
    PRIMARY KEY (Customer_ID, Phone_Number),
    FOREIGN KEY (Customer_ID) REFERENCES Customer (Customer_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ── Suppliers & purchasing ──────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS Supplier (
    Supplier_ID       INT AUTO_INCREMENT PRIMARY KEY,
    Supplier_Name     VARCHAR(100) NOT NULL,
    Supplier_Address  VARCHAR(255),
    Company_Name      VARCHAR(150),
    Email             VARCHAR(150)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS SupplierPhone (
    Supplier_ID     INT NOT NULL,
    Supplier_Phone  VARCHAR(30) NOT NULL,
    PRIMARY KEY (Supplier_ID, Supplier_Phone),
    FOREIGN KEY (Supplier_ID) REFERENCES Supplier (Supplier_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Supplies (
    Supplier_ID  INT NOT NULL,
    Product_ID   INT NOT NULL,
    PRIMARY KEY (Supplier_ID, Product_ID),
    FOREIGN KEY (Supplier_ID) REFERENCES Supplier (Supplier_ID) ON DELETE CASCADE,
    FOREIGN KEY (Product_ID)  REFERENCES Product (Product_ID)   ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS SupplierOrder (
    SupplierOrder_ID  INT AUTO_INCREMENT PRIMARY KEY,
    Purchase_Date     DATE NOT NULL,
    Total_Cost        DECIMAL(12,2) NOT NULL DEFAULT 0,
    Supplier_ID       INT NOT NULL,
    Employee_ID       INT NOT NULL,
    FOREIGN KEY (Supplier_ID) REFERENCES Supplier (Supplier_ID),
    FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS SupplierOrderDetails (
    SupplierOrder_ID  INT NOT NULL,
    Product_ID        INT NOT NULL,
    Quantity          INT NOT NULL CHECK (Quantity > 0),
    Unit_Cost         DECIMAL(10,2) NOT NULL CHECK (Unit_Cost >= 0),
    PRIMARY KEY (SupplierOrder_ID, Product_ID),
    FOREIGN KEY (SupplierOrder_ID) REFERENCES SupplierOrder (SupplierOrder_ID) ON DELETE CASCADE,
    FOREIGN KEY (Product_ID)       REFERENCES Product (Product_ID)
) ENGINE=InnoDB;

-- ── Sales: orders, payments, serials, returns ───────────────────────────────

CREATE TABLE IF NOT EXISTS Orders (
    Order_ID     INT AUTO_INCREMENT PRIMARY KEY,
    Order_Date   DATE NOT NULL,
    Total_Cost   DECIMAL(12,2) NOT NULL DEFAULT 0,   -- kept in sync by the app (OrderDetails sum)
    Customer_ID  INT NOT NULL,
    Employee_ID  INT NOT NULL,
    FOREIGN KEY (Customer_ID) REFERENCES Customer (Customer_ID),
    FOREIGN KEY (Employee_ID) REFERENCES Employee (Employee_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS OrderDetails (
    OrderDetails_ID  INT AUTO_INCREMENT PRIMARY KEY,
    Quantity         INT NOT NULL CHECK (Quantity > 0),
    Unit_Price       DECIMAL(10,2) NOT NULL CHECK (Unit_Price >= 0),
    Line_Total       DECIMAL(12,2) NOT NULL CHECK (Line_Total >= 0),
    Order_ID         INT NOT NULL,
    Product_ID       INT NOT NULL,
    FOREIGN KEY (Order_ID)   REFERENCES Orders (Order_ID),
    FOREIGN KEY (Product_ID) REFERENCES Product (Product_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Payment (
    Payment_ID      INT AUTO_INCREMENT PRIMARY KEY,
    Payment_Date    DATE NOT NULL,
    Amount          DECIMAL(12,2) NOT NULL CHECK (Amount >= 0),
    Payment_Status  VARCHAR(20) NOT NULL,            -- 'Paid' / 'Pending'
    Order_ID        INT NOT NULL,
    FOREIGN KEY (Order_ID) REFERENCES Orders (Order_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Cash_Payment (
    Payment_ID     INT PRIMARY KEY,
    Cash_Received  DECIMAL(12,2) NOT NULL,
    Change_Amount  DECIMAL(12,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (Payment_ID) REFERENCES Payment (Payment_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Installment_Payment (
    Payment_ID              INT PRIMARY KEY,
    Down_Payment            DECIMAL(12,2) NOT NULL,
    Number_Of_Installments  INT NOT NULL CHECK (Number_Of_Installments > 0),
    Monthly_Amount          DECIMAL(12,2) NOT NULL,
    Remaining_Balance       DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (Payment_ID) REFERENCES Payment (Payment_ID)
) ENGINE=InnoDB;

-- Order_ID is NULL while a unit is still in stock; it is set when the unit is sold.
CREATE TABLE IF NOT EXISTS ProductSerial (
    ProductSerial_ID     INT AUTO_INCREMENT PRIMARY KEY,
    Serial_Number        VARCHAR(60) NOT NULL UNIQUE,
    Warranty_Start_Date  DATE,
    Warranty_End_Date    DATE,
    Status               VARCHAR(20) NOT NULL DEFAULT 'In Stock',   -- In Stock / Sold / Returned
    Order_ID             INT NULL,
    Product_ID           INT NOT NULL,
    FOREIGN KEY (Order_ID)   REFERENCES Orders (Order_ID),
    FOREIGN KEY (Product_ID) REFERENCES Product (Product_ID)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS TheReturns (
    Return_ID      INT AUTO_INCREMENT PRIMARY KEY,
    Return_Date    DATE NOT NULL,
    Quantity       INT NOT NULL CHECK (Quantity > 0),
    Reason         VARCHAR(255),
    Refund_Amount  DECIMAL(12,2) NOT NULL CHECK (Refund_Amount >= 0),
    Order_ID       INT NOT NULL,
    Product_ID     INT NOT NULL,
    Inventory_ID   INT NOT NULL,
    FOREIGN KEY (Order_ID)     REFERENCES Orders (Order_ID),
    FOREIGN KEY (Product_ID)   REFERENCES Product (Product_ID),
    FOREIGN KEY (Inventory_ID) REFERENCES Inventory (Inventory_ID)
) ENGINE=InnoDB;
