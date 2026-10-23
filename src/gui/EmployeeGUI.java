 package gui;

import java.sql.Date;
import java.util.List;

import dao.EmployeeDAO;
import dao.OrdersDAO;
import entity.BestSellingEmployee;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EmployeeGUI {


private static final String PURPLE = "#2D1B69";
private static final String ORANGE = "#F5A623";
private static final String BG = "#F0EBF8";

private TableView<Employee> table;
private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

private TextField idField, firstNameField, lastNameField, shiftField, salaryField;
private TextField addressField, managerField, dateField, phoneField;

private Label statusLabel;

public void show(Stage stage) {

    stage.setTitle("Horizon – Employees");
    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: " + BG + ";");

    root.setTop(buildTopBar(stage));
    root.setLeft(buildFormPanel());
    root.setCenter(buildTablePanel());

    Theme.show(stage, root, 1100, 700);

    loadEmployees();
}

private HBox buildTopBar(Stage stage) {
    return Theme.topBar(stage, "Employees");
}

private ScrollPane buildFormPanel() {

    VBox panel = new VBox(12);
    panel.setPrefWidth(290);
    panel.setPadding(new Insets(18));
    panel.getStyleClass().add("form-panel");

    Label formTitle = new Label("Employee Details");
    formTitle.getStyleClass().add("form-title");

    idField = field("Employee ID");
    firstNameField = field("First Name");
    lastNameField = field("Last Name");
    shiftField = field("Shift");
    salaryField = field("Salary");
    addressField = field("Address");
    managerField = field("Manager ID");
    dateField = field("Hire Date (YYYY-MM-DD)");
    phoneField = field("Phone Number");

    Button addBtn = actionButton("Add", PURPLE, "white");
    Button updateBtn = actionButton("Update", PURPLE, "white");
    Button deleteBtn = actionButton("Delete", PURPLE, "white");
    Button clearBtn = actionButton("Clear", PURPLE, "white");

    Button bestEmployeeBtn = actionButton("Best Selling Employee", ORANGE, "white");
    Button techniciansBtn = actionButton("Technicians Details", PURPLE, "white");
    Button salesBtn = actionButton("Sales Employees Exceeded target", PURPLE, "white");
    Button warehouseBtn = actionButton("Warehouse Employees Details", PURPLE, "white");

    addBtn.setMaxWidth(Double.MAX_VALUE);
    updateBtn.setMaxWidth(Double.MAX_VALUE);
    deleteBtn.setMaxWidth(Double.MAX_VALUE);
    clearBtn.setMaxWidth(Double.MAX_VALUE);
    bestEmployeeBtn.setMaxWidth(Double.MAX_VALUE);
    techniciansBtn.setMaxWidth(Double.MAX_VALUE);
    salesBtn.setMaxWidth(Double.MAX_VALUE);
    warehouseBtn.setMaxWidth(Double.MAX_VALUE);

    addBtn.setOnAction(e ->addEmployee());
    updateBtn.setOnAction(e ->updateEmployee());
    deleteBtn.setOnAction(e ->deleteEmployee());
    clearBtn.setOnAction(e ->clearForm());

    bestEmployeeBtn.setOnAction(e-> showBestSellingEmployee());

    techniciansBtn.setOnAction(e-> showStringTable( "Technicians Details",
            EmployeeDAO.getTechniciansWithDetails(),
            new String[]{"ID","Full Name","Shift","Salary","Address","Hire Date","Specialty","Certification"}
    ));

    salesBtn.setOnAction(e -> showStringTable(
            "Sales Employees Exceeded Target",
            EmployeeDAO.getSalesEmployeesExceededTarget(),
            new String[]{"ID", "Full Name", "Shift", "Salary", "Commission", "Target", "Total Sales"}
    ));

    warehouseBtn.setOnAction(e -> showStringTable(
            "Warehouse Employees Details",
            EmployeeDAO.getWarehouseEmployeesWithDetails(),
            new String[]{"ID", "Full Name", "Shift", "Salary", "Address", "Hire Date", "Forklift Certified"}
    ));

    statusLabel = new Label("");
    statusLabel.setFont(Font.font("Arial", 12));
    statusLabel.setWrapText(true);

    panel.getChildren().addAll(
            formTitle,
            label("Employee ID"), idField,
            label("First Name"), firstNameField,
            label("Last Name"), lastNameField,
            label("Shift"), shiftField,
            label("Salary"), salaryField,
            label("Address"), addressField,
            label("Manager ID"), managerField,
            label("Hire Date"), dateField,
            label("Phone Number"), phoneField,
            addBtn, updateBtn, deleteBtn, clearBtn,
            bestEmployeeBtn, techniciansBtn, salesBtn, warehouseBtn,
            statusLabel
    );

    ScrollPane scrollPane = new ScrollPane(panel);
    scrollPane.getStyleClass().add("form-scroll");
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefWidth(290);

    return scrollPane;
}

private VBox buildTablePanel() {

    VBox panel = new VBox(15);
    panel.setPadding(new Insets(25));

    Button refreshBtn = actionButton("Refresh", PURPLE, "white");
    refreshBtn.setOnAction(e -> loadEmployees());

    HBox topRow = new HBox(10, refreshBtn);
    topRow.setAlignment(Pos.CENTER_LEFT);

    table = new TableView<>();
    Theme.styleTable(table);

    table.getColumns().addAll(
            col("ID", "employeeId", 60),
            col("First Name", "firstName", 110),
            col("Last Name", "lastName", 110),
            col("Shift", "shift", 90),
            col("Salary", "salary", 90),
            col("Address", "address", 130),
            col("Manager", "managerId", 90),
            col("Hire Date", "hireDate", 120),
            col("Phone", "phoneNumber", 120)
    );

    TextField searchField = Theme.searchBox(table, employeeList);
    topRow.getChildren().add(0, searchField);

    table.getSelectionModel().selectedItemProperty().addListener((obs, old, emp) -> {
        if (emp != null) {
            idField.setText(String.valueOf(emp.getEmployeeId()));
            firstNameField.setText(emp.getFirstName());
            lastNameField.setText(emp.getLastName());
            shiftField.setText(emp.getShift());
            salaryField.setText(String.valueOf(emp.getSalary()));
            addressField.setText(emp.getAddress());
            managerField.setText(emp.getManagerId() == null ? "" : String.valueOf(emp.getManagerId()));
            dateField.setText(emp.getHireDate() == null ? "" : emp.getHireDate().toString());
            phoneField.setText(emp.getPhoneNumber() == null ? "" : emp.getPhoneNumber());
        }
    });

    panel.getChildren().addAll(topRow, table);
    return panel;
}

private void loadEmployees() {
    employeeList.clear();
    employeeList.addAll(EmployeeDAO.getAllEmployees());
}

private void addEmployee() {

    if (!valid()) return;

    Employee emp = new Employee(
            Integer.parseInt(idField.getText().trim()),
            firstNameField.getText().trim(),
            lastNameField.getText().trim(),
            shiftField.getText().trim(),
            Double.parseDouble(salaryField.getText().trim()),
            addressField.getText().trim(),
            getManagerId(),
            Date.valueOf(dateField.getText().trim()),
            phoneField.getText().trim()
    );

    EmployeeDAO.addEmployee(emp);
    status("Employee added.", PURPLE);
    clearForm();
    loadEmployees();
}

private void updateEmployee() {

    Employee selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an employee first.", PURPLE);
        return;
    }

    if (!valid()) return;

    Employee emp = new Employee(
            Integer.parseInt(idField.getText().trim()),
            firstNameField.getText().trim(),
            lastNameField.getText().trim(),
            shiftField.getText().trim(),
            Double.parseDouble(salaryField.getText().trim()),
            addressField.getText().trim(),
            getManagerId(),
            Date.valueOf(dateField.getText().trim()),
            phoneField.getText().trim()
    );

    EmployeeDAO.updateEmployee(emp);
    status("Employee updated.", PURPLE);
    clearForm();
    loadEmployees();
}

private void deleteEmployee() {

    Employee selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an employee first.", PURPLE);
        return;
    }

    if (!Theme.confirm("Delete the selected employee? This cannot be undone.")) return;

    EmployeeDAO.deleteEmployee(selected.getEmployeeId());
    status("Employee deleted.", PURPLE);
    clearForm();
    loadEmployees();
}

private boolean valid() {

    if (idField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            shiftField.getText().trim().isEmpty() ||
            salaryField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            dateField.getText().trim().isEmpty()) {

        status("All fields are required except Manager ID and Phone.", PURPLE);
        return false;
    }

    try {
        Integer.parseInt(idField.getText().trim());
        Double.parseDouble(salaryField.getText().trim());
        Date.valueOf(dateField.getText().trim());

        if (!managerField.getText().trim().isEmpty()) {
            Integer.parseInt(managerField.getText().trim());
        }

    } catch (Exception ex) {
        status("Check ID, Salary, Manager ID, and Date format.", ORANGE);
        return false;
    }

    return true;
}

private Integer getManagerId() {

    if (managerField.getText().trim().isEmpty()) {
        return null;
    }

    return Integer.parseInt(managerField.getText().trim());
}

private void clearForm() {
    idField.clear();
    firstNameField.clear();
    lastNameField.clear();
    shiftField.clear();
    salaryField.clear();
    addressField.clear();
    managerField.clear();
    dateField.clear();
    phoneField.clear();

    table.getSelectionModel().clearSelection();
}

private void status(String msg, String color) {
    Theme.status(statusLabel, msg);
}

private void showBestSellingEmployee() {

    List<BestSellingEmployee> bestList = OrdersDAO.getBestSellingEmployee();

    if (bestList.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Best Selling Employee");
        alert.setHeaderText(null);
        alert.setContentText("No orders found.");
        alert.showAndWait();
        return;
    }

    BestSellingEmployee emp = bestList.get(0);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Best Selling Employee");
    alert.setHeaderText("Best Selling Employee");
    alert.setContentText(
            "Employee ID: " + emp.getEmployeeId() + "\n" +
                    "Name: " + emp.getEmployeeName() + "\n" +
                    "Number of Orders: " + emp.getNumberOfOrders() + "\n" +
                    "Total Sales: " + emp.getTotalSales()
    );

    alert.showAndWait();
}

private void showStringTable(String titleText, List<String[]> data, String[] headers) {

    TableView<String[]> resultTable = new TableView<>();

    for (int i = 0; i < headers.length; i++) {
        final int index = i;

        TableColumn<String[], String> column = new TableColumn<>(headers[i]);

        column.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue()[index])
        );

        resultTable.getColumns().add(column);
    }

    resultTable.setItems(FXCollections.observableArrayList(data));

    VBox box = new VBox(10);
    box.setPadding(new Insets(15));

    Label title = new Label(titleText);
    title.getStyleClass().add("popup-title");

    box.getChildren().addAll(title, resultTable);

    Stage resultStage = new Stage();
    resultStage.setTitle(titleText);
    resultStage.setScene(Theme.scene(box, 900, 400));
    resultStage.show();
}

private Label label(String text) {
    return Theme.label(text);
}

private TextField field(String prompt) {
    return Theme.field(prompt);
}

private Button actionButton(String text, String bg, String fg) {
    return Theme.actionButton(text, bg);
}

private <T> TableColumn<Employee, T> col(String title, String prop, double width) {

    TableColumn<Employee, T> column = new TableColumn<>(title);
    column.setCellValueFactory(new PropertyValueFactory<>(prop));
    column.setPrefWidth(width);
    return column;
}


}