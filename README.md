# Horizon Store System

A desktop store-management application built with **JavaFX** and **MySQL** (JDBC).
It manages products and inventory, customers, employees, suppliers, orders and
payments, and includes a reports section with charts.

> Course project - [Course name / University / Semester]
> Authors: [Your name(s)]

<!-- Add a screenshot: ![Dashboard](docs/dashboard.png) -->

## Features

- **Products & inventory** - add/edit/delete products, top sellers, low-stock and warehouse-capacity reports
- **Customers, Employees, Suppliers** - full CRUD with validation
- **Orders** - create orders, add order items, record cash or installment payments
- **Reports** - pie charts (top products / customers) and tables (category summary,
  low stock, daily sales, revenue & returns)
- Live search on every table, confirmation before deleting, and a database status indicator

## Requirements

- JDK 21
- [JavaFX SDK 21](https://openjfx.io/)
- MySQL 8+ and the [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) jar

## Setup

1. **Create the database.** From the project folder run:
   ```bash
   mysql -u root -p < database/schema.sql        # creates HorizonDB and all 24 tables
   mysql -u root -p < database/sample_data.sql   # optional: fictional demo data
   ```
   The demo data includes 17 products, 10 customers, 8 employees, 4 suppliers and
   16 orders, so every screen and report has something to show.
2. **Configure the connection.** Copy `db.properties.example` to `db.properties`
   and put your MySQL password in it. This file is git-ignored.
   You can also use the environment variables `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. **Run the app** (Eclipse or command line, below).

### Eclipse

1. *File > Import > Existing Projects into Workspace* and select this folder.
2. Add the JavaFX SDK jars and the MySQL Connector/J jar to the build path.
3. Run `gui.StartApp` with VM arguments:
   `--module-path "/path/to/javafx-sdk-21/lib" --add-modules javafx.controls`

### Command line

```bash
# Linux / macOS
FX=/path/to/javafx-sdk-21/lib
javac -encoding UTF-8 --module-path $FX --add-modules javafx.controls -d bin $(find src -name "*.java")
cp src/gui/style.css bin/gui/
java --module-path $FX --add-modules javafx.controls -cp "bin:path/to/mysql-connector-j.jar" gui.StartApp
```

```bat
:: Windows
set FX=C:\path\to\javafx-sdk-21\lib
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 --module-path %FX% --add-modules javafx.controls -d bin @sources.txt
copy src\gui\style.css bin\gui\
java --module-path %FX% --add-modules javafx.controls -cp "bin;path\to\mysql-connector-j.jar" gui.StartApp
```

## Project structure

```
database/
  schema.sql        table definitions
  sample_data.sql   fictional demo data
src/
  database/   DBConnection - reads settings from db.properties / environment
  entity/     plain data classes (Customer, Product, Orders, ...)
  dao/        database access (prepared statements)
  gui/        JavaFX screens, shared Theme.java and style.css
```
