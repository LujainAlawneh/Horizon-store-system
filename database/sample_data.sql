-- ============================================================================
-- Horizon Store System - sample data (all names, emails and phones are fictional)
-- Run once, AFTER schema.sql:  mysql -u root -p < database/sample_data.sql
-- To reload, drop the database, re-run schema.sql, then run this file again.
-- ============================================================================

USE HorizonDB;

SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO Category (Category_ID, Category_Name, Description) VALUES
  (1, 'Laptops', 'Notebooks and ultrabooks'),
  (2, 'Smartphones', 'Mobile phones'),
  (3, 'Tablets', 'Tablets and e-readers'),
  (4, 'TVs & Displays', 'Televisions and monitors'),
  (5, 'Audio', 'Headphones, earbuds and speakers'),
  (6, 'Accessories', 'Chargers, cables and peripherals');

INSERT INTO Product (Product_ID, Product_Name, Brand, Price, Warranty_Period, Category_ID) VALUES
  (1, 'UltraBook 14 Pro', 'Nordica', 1299.00, 24, 1),
  (2, 'Gamer X15', 'Vortex', 1599.00, 24, 1),
  (3, 'WorkBook 15', 'Nordica', 749.00, 12, 1),
  (4, 'Nova S5', 'Lumina', 899.00, 24, 2),
  (5, 'Nova Lite', 'Lumina', 399.00, 12, 2),
  (6, 'Orbit Phone 9', 'Orbit', 1099.00, 24, 2),
  (7, 'TabPro 11', 'Orbit', 649.00, 12, 3),
  (8, 'TabMini 8', 'Lumina', 299.00, 12, 3),
  (9, 'Vision 55" 4K TV', 'Vistara', 799.00, 36, 4),
  (10, 'Vision 65" OLED TV', 'Vistara', 1799.00, 36, 4),
  (11, 'ViewPro 27" Monitor', 'Nordica', 349.00, 24, 4),
  (12, 'SoundBuds Air', 'Orbit', 129.00, 12, 5),
  (13, 'BassBox Speaker', 'Vortex', 89.00, 12, 5),
  (14, 'Studio Headphones', 'Vistara', 219.00, 24, 5),
  (15, 'USB-C Fast Charger 65W', 'Vortex', 39.00, 6, 6),
  (16, 'Wireless Mouse M2', 'Nordica', 25.00, 12, 6),
  (17, 'Mechanical Keyboard K1', 'Vortex', 79.00, 12, 6);

INSERT INTO Warehouse (Warehouse_ID, Location, Maximum_Storage, Available_Storage) VALUES
  (1, 'Main Warehouse', 5000, 3200),
  (2, 'North Depot', 2000, 350),
  (3, 'South Depot', 3000, 1800),
  (4, 'East Annex', 1000, 120);

INSERT INTO Inventory (Inventory_ID, Inventory_Date, Quantity_Available, Minimum_Stock_Level, Product_ID, Warehouse_ID) VALUES
  (1, '2026-10-15', 34, 10, 1, 1),
  (2, '2026-10-15', 12, 8, 2, 1),
  (3, '2026-10-15', 6, 10, 3, 1),
  (4, '2026-10-15', 40, 15, 4, 2),
  (5, '2026-10-15', 55, 20, 5, 2),
  (6, '2026-10-15', 9, 12, 6, 3),
  (7, '2026-10-15', 22, 10, 7, 3),
  (8, '2026-10-15', 30, 10, 8, 3),
  (9, '2026-10-15', 14, 6, 9, 1),
  (10, '2026-10-15', 3, 5, 10, 4),
  (11, '2026-10-15', 25, 8, 11, 1),
  (12, '2026-10-15', 120, 40, 12, 2),
  (13, '2026-10-15', 70, 25, 13, 3),
  (14, '2026-10-15', 18, 10, 14, 4),
  (15, '2026-10-15', 200, 60, 15, 1),
  (16, '2026-10-15', 150, 50, 16, 2),
  (17, '2026-10-15', 45, 15, 17, 3);

INSERT INTO StockMovement (Movement_ID, Movement_Type, Quantity, Movement_Date, Inventory_ID) VALUES
  (1, 'IN', 20, '2026-09-01', 1),
  (2, 'OUT', 6, '2026-09-12', 1),
  (3, 'IN', 10, '2026-09-03', 4),
  (4, 'OUT', 5, '2026-09-20', 4),
  (5, 'IN', 4, '2026-09-05', 10),
  (6, 'OUT', 6, '2026-10-02', 10),
  (7, 'IN', 50, '2026-09-08', 15),
  (8, 'OUT', 20, '2026-10-09', 15),
  (9, 'OUT', 4, '2026-10-11', 6);

INSERT INTO Employee (Employee_ID, First_Name, Last_Name, shift, Salary, Address, Manager_ID, Hire_Date) VALUES
  (101, 'Sarah', 'Mitchell', 'Morning', 4500.00, '8 Harbor Road, Riverton', NULL, '2019-03-01'),
  (102, 'Omar', 'Haddad', 'Morning', 2800.00, '22 Pine Street, Riverton', 101, '2021-06-14'),
  (103, 'Layla', 'Nasser', 'Evening', 2700.00, '5 Maple Avenue, Riverton', 101, '2022-01-10'),
  (104, 'James', 'Walker', 'Morning', 3000.00, '91 Oak Lane, Lakeside', 101, '2020-09-07'),
  (105, 'Maya', 'Rossi', 'Evening', 3100.00, '17 Birch Court, Lakeside', 101, '2021-02-22'),
  (106, 'Karim', 'Aziz', 'Night', 2400.00, '3 Mill Road, Riverton', 101, '2020-11-02'),
  (107, 'Elena', 'Petrova', 'Morning', 2350.00, '40 Cedar Close, Lakeside', 106, '2023-04-18'),
  (108, 'Tom', 'Becker', 'Evening', 2650.00, '12 Elm Street, Riverton', 101, '2022-08-29');

INSERT INTO EmployeePhone (Employee_ID, Phone_Number) VALUES
  (101, '0591-100-101'),
  (102, '0591-100-102'),
  (103, '0591-100-103'),
  (104, '0591-100-104'),
  (105, '0591-100-105'),
  (106, '0591-100-106'),
  (108, '0591-100-108');

INSERT INTO Technician (Employee_ID, Specialty, Certification_Level) VALUES
  (104, 'Laptops & Computers', 'Senior'),
  (105, 'Mobile Devices', 'Intermediate');

INSERT INTO Sales_Employee (Employee_ID, Sales_Commission, Sales_Target) VALUES
  (102, 5.00, 15000.00),
  (103, 4.50, 12000.00),
  (108, 4.00, 20000.00);

INSERT INTO Warehouse_Employee (Employee_ID, Forklift_Certified) VALUES
  (106, TRUE),
  (107, FALSE);

INSERT INTO Customer (Customer_ID, First_Name, Last_Name, Customer_BirthDate, Customer_Email, Address) VALUES
  (1, 'Ahmad', 'Karim', '1990-05-14', 'ahmad.karim@example.com', '14 Cedar Lane, Riverton'),
  (2, 'Noor', 'Salem', '1995-11-02', 'noor.salem@example.com', '7 Olive Street, Riverton'),
  (3, 'Daniel', 'Green', '1988-03-21', 'daniel.green@example.com', '52 Lake View, Lakeside'),
  (4, 'Rana', 'Khalil', '1992-08-09', 'rana.khalil@example.com', '9 Garden Road, Riverton'),
  (5, 'Yusuf', 'Ibrahim', '1985-12-30', 'yusuf.ibrahim@example.com', '31 Hill Street, Lakeside'),
  (6, 'Sofia', 'Moretti', '1998-01-17', 'sofia.moretti@example.com', '26 River Walk, Riverton'),
  (7, 'Hassan', 'Odeh', '1979-07-04', 'hassan.odeh@example.com', '4 Station Road, Lakeside'),
  (8, 'Emily', 'Clark', '2001-09-25', 'emily.clark@example.com', '63 Park Avenue, Riverton'),
  (9, 'Tariq', 'Mansour', '1993-04-12', 'tariq.mansour@example.com', '18 Spring Street, Lakeside'),
  (10, 'Lina', 'Barakat', '1987-06-28', 'lina.barakat@example.com', '2 Orchard Way, Riverton');

INSERT INTO CustomerPhone (Customer_ID, Phone_Number) VALUES
  (1, '0599-200-001'),
  (2, '0599-200-002'),
  (3, '0599-200-003'),
  (4, '0599-200-004'),
  (5, '0599-200-005'),
  (6, '0599-200-006'),
  (7, '0599-200-007'),
  (8, '0599-200-008'),
  (9, '0599-200-009');

INSERT INTO Supplier (Supplier_ID, Supplier_Name, Supplier_Address, Company_Name, Email) VALUES
  (1, 'Marcus Hale', '100 Industrial Park, Port City', 'Nordica Distribution', 'sales@nordica-dist.example.com'),
  (2, 'Priya Shah', '45 Trade Center, Port City', 'Lumina & Orbit Wholesale', 'orders@lumina-orbit.example.com'),
  (3, 'Viktor Novak', '12 Harbor Docks, Port City', 'Vistara Electronics', 'b2b@vistara.example.com'),
  (4, 'Amira Said', '7 Commerce Street, Riverton', 'Vortex Accessories', 'trade@vortex-acc.example.com');

INSERT INTO SupplierPhone (Supplier_ID, Supplier_Phone) VALUES
  (1, '0592-300-001'),
  (2, '0592-300-002'),
  (3, '0592-300-003'),
  (4, '0592-300-004');

INSERT INTO Supplies (Supplier_ID, Product_ID) VALUES
  (1, 1),
  (1, 3),
  (1, 11),
  (1, 16),
  (2, 4),
  (2, 5),
  (2, 6),
  (2, 7),
  (2, 8),
  (2, 12),
  (3, 9),
  (3, 10),
  (3, 14),
  (4, 2),
  (4, 13),
  (4, 15),
  (4, 17);

INSERT INTO SupplierOrder (SupplierOrder_ID, Purchase_Date, Total_Cost, Supplier_ID, Employee_ID) VALUES
  (1, '2026-08-20', 35250.00, 1, 106),
  (2, '2026-09-02', 36900.00, 2, 106),
  (3, '2026-09-25', 19320.00, 3, 107);

INSERT INTO SupplierOrderDetails (SupplierOrder_ID, Product_ID, Quantity, Unit_Cost) VALUES
  (1, 1, 20, 1050.00),
  (1, 3, 15, 590.00),
  (1, 11, 20, 270.00),
  (2, 4, 25, 720.00),
  (2, 6, 15, 880.00),
  (2, 12, 60, 95.00),
  (3, 9, 12, 610.00),
  (3, 10, 6, 1450.00),
  (3, 14, 20, 165.00);

INSERT INTO Orders (Order_ID, Order_Date, Total_Cost, Customer_ID, Employee_ID) VALUES
  (1, '2026-08-03', 2648.00, 1, 102),
  (2, '2026-08-07', 938.00, 2, 103),
  (3, '2026-08-12', 3598.00, 3, 102),
  (4, '2026-08-19', 778.00, 1, 108),
  (5, '2026-08-26', 4797.00, 4, 102),
  (6, '2026-09-02', 1776.00, 5, 103),
  (7, '2026-09-09', 2456.00, 1, 102),
  (8, '2026-09-14', 853.00, 6, 108),
  (9, '2026-09-21', 4595.00, 7, 102),
  (10, '2026-09-27', 676.00, 2, 103),
  (11, '2026-10-01', 2237.00, 1, 102),
  (12, '2026-10-06', 798.00, 8, 108),
  (13, '2026-10-10', 1678.00, 3, 103),
  (14, '2026-10-14', 2897.00, 9, 102),
  (15, '2026-10-18', 616.00, 2, 108),
  (16, '2026-10-21', 1517.00, 5, 103);

INSERT INTO OrderDetails (Quantity, Unit_Price, Line_Total, Order_ID, Product_ID) VALUES
  (2, 1299.00, 2598.00, 1, 1),
  (2, 25.00, 50.00, 1, 16),
  (1, 899.00, 899.00, 2, 4),
  (1, 39.00, 39.00, 2, 15),
  (2, 1799.00, 3598.00, 3, 10),
  (1, 649.00, 649.00, 4, 7),
  (1, 129.00, 129.00, 4, 12),
  (3, 1599.00, 4797.00, 5, 2),
  (2, 799.00, 1598.00, 6, 9),
  (2, 89.00, 178.00, 6, 13),
  (2, 1099.00, 2198.00, 7, 6),
  (2, 129.00, 258.00, 7, 12),
  (1, 749.00, 749.00, 8, 3),
  (1, 79.00, 79.00, 8, 17),
  (1, 25.00, 25.00, 8, 16),
  (3, 1299.00, 3897.00, 9, 1),
  (2, 349.00, 698.00, 9, 11),
  (2, 299.00, 598.00, 10, 8),
  (2, 39.00, 78.00, 10, 15),
  (1, 1799.00, 1799.00, 11, 10),
  (2, 219.00, 438.00, 11, 14),
  (2, 399.00, 798.00, 12, 5),
  (1, 1599.00, 1599.00, 13, 2),
  (1, 79.00, 79.00, 13, 17),
  (2, 899.00, 1798.00, 14, 4),
  (1, 1099.00, 1099.00, 14, 6),
  (1, 349.00, 349.00, 15, 11),
  (3, 89.00, 267.00, 15, 13),
  (2, 649.00, 1298.00, 16, 7),
  (1, 219.00, 219.00, 16, 14);

INSERT INTO Payment (Payment_ID, Payment_Date, Amount, Payment_Status, Order_ID) VALUES
  (1, '2026-08-03', 2648.00, 'Paid', 1),
  (2, '2026-08-07', 938.00, 'Paid', 2),
  (3, '2026-08-12', 3598.00, 'Paid', 3),
  (4, '2026-08-19', 778.00, 'Paid', 4),
  (5, '2026-08-26', 4797.00, 'Pending', 5),
  (6, '2026-09-02', 1776.00, 'Paid', 6),
  (7, '2026-09-09', 2456.00, 'Paid', 7),
  (8, '2026-09-14', 853.00, 'Paid', 8),
  (9, '2026-09-21', 4595.00, 'Pending', 9),
  (10, '2026-09-27', 676.00, 'Paid', 10),
  (11, '2026-10-01', 2237.00, 'Pending', 11),
  (12, '2026-10-06', 798.00, 'Paid', 12),
  (13, '2026-10-10', 1678.00, 'Paid', 13),
  (14, '2026-10-14', 2897.00, 'Pending', 14),
  (15, '2026-10-21', 1517.00, 'Pending', 16);

INSERT INTO Cash_Payment (Payment_ID, Cash_Received, Change_Amount) VALUES
  (1, 2650.00, 2.00),
  (2, 950.00, 12.00),
  (4, 800.00, 22.00),
  (6, 1800.00, 24.00),
  (7, 2500.00, 44.00),
  (8, 900.00, 47.00),
  (10, 700.00, 24.00),
  (12, 800.00, 2.00),
  (13, 1700.00, 22.00),
  (15, 1517.00, 0.00);

INSERT INTO Installment_Payment (Payment_ID, Down_Payment, Number_Of_Installments, Monthly_Amount, Remaining_Balance) VALUES
  (3, 1079.40, 6, 419.77, 0.00),
  (5, 1439.10, 12, 279.82, 3357.90),
  (9, 1378.50, 6, 536.08, 3216.50),
  (11, 671.10, 3, 521.97, 1565.90),
  (14, 869.10, 6, 337.98, 2027.90);

INSERT INTO ProductSerial (ProductSerial_ID, Serial_Number, Warranty_Start_Date, Warranty_End_Date, Status, Order_ID, Product_ID) VALUES
  (1, 'SN-01-01001', '2026-08-03', '2028-08-03', 'Sold', 1, 1),
  (2, 'SN-01-01002', '2026-08-03', '2028-08-03', 'Sold', 1, 1),
  (3, 'SN-04-01003', '2026-08-07', '2028-08-07', 'Sold', 2, 4),
  (4, 'SN-10-01004', '2026-08-12', '2029-08-12', 'Sold', 3, 10),
  (5, 'SN-10-01005', '2026-08-12', '2029-08-12', 'Sold', 3, 10),
  (6, 'SN-02-01006', '2026-08-26', '2028-08-26', 'Sold', 5, 2),
  (7, 'SN-02-01007', '2026-08-26', '2028-08-26', 'Sold', 5, 2),
  (8, 'SN-02-01008', '2026-08-26', '2028-08-26', 'Sold', 5, 2),
  (9, 'SN-09-01009', '2026-09-02', '2029-09-02', 'Sold', 6, 9),
  (10, 'SN-09-01010', '2026-09-02', '2029-09-02', 'Sold', 6, 9),
  (11, 'SN-06-01011', '2026-09-09', '2028-09-09', 'Sold', 7, 6),
  (12, 'SN-06-01012', '2026-09-09', '2028-09-09', 'Sold', 7, 6),
  (13, 'SN-01-01013', '2026-09-21', '2028-09-21', 'Sold', 9, 1),
  (14, 'SN-01-01014', '2026-09-21', '2028-09-21', 'Sold', 9, 1),
  (15, 'SN-01-01015', '2026-09-21', '2028-09-21', 'Sold', 9, 1),
  (16, 'SN-11-01016', '2026-09-21', '2028-09-21', 'Sold', 9, 11),
  (17, 'SN-11-01017', '2026-09-21', '2028-09-21', 'Sold', 9, 11),
  (18, 'SN-10-01018', '2026-10-01', '2029-10-01', 'Sold', 11, 10),
  (19, 'SN-14-01019', '2026-10-01', '2028-10-01', 'Sold', 11, 14),
  (20, 'SN-14-01020', '2026-10-01', '2028-10-01', 'Sold', 11, 14),
  (21, 'SN-02-01021', '2026-10-10', '2028-10-10', 'Sold', 13, 2),
  (22, 'SN-04-01022', '2026-10-14', '2028-10-14', 'Sold', 14, 4),
  (23, 'SN-04-01023', '2026-10-14', '2028-10-14', 'Sold', 14, 4),
  (24, 'SN-06-01024', '2026-10-14', '2028-10-14', 'Sold', 14, 6),
  (25, 'SN-11-01025', '2026-10-18', '2028-10-18', 'Sold', 15, 11),
  (26, 'SN-14-01026', '2026-10-21', '2028-10-21', 'Sold', 16, 14),
  (27, 'SN-01-01027', NULL, NULL, 'In Stock', NULL, 1),
  (28, 'SN-01-01028', NULL, NULL, 'In Stock', NULL, 1),
  (29, 'SN-04-01029', NULL, NULL, 'In Stock', NULL, 4),
  (30, 'SN-04-01030', NULL, NULL, 'In Stock', NULL, 4),
  (31, 'SN-09-01031', NULL, NULL, 'In Stock', NULL, 9),
  (32, 'SN-09-01032', NULL, NULL, 'In Stock', NULL, 9);

INSERT INTO TheReturns (Return_ID, Return_Date, Quantity, Reason, Refund_Amount, Order_ID, Product_ID, Inventory_ID) VALUES
  (1, '2026-08-10', 1, 'Defective screen', 1299.00, 1, 1, 1),
  (2, '2026-09-18', 1, 'Changed mind', 89.00, 6, 13, 13),
  (3, '2026-10-05', 1, 'Faulty battery', 1099.00, 7, 6, 6);

SET FOREIGN_KEY_CHECKS = 1;
