package gui;

import dao.SupplierDAO;
import entity.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class SupplierGUI {

private static final String PURPLE = "#2D1B69";
private static final String ORANGE = "#F5A623";
private static final String BG = "#F0EBF8";

private TableView<Supplier> table;
private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

private TextField nameField, addressField, companyField, emailField, phoneField;
private Label statusLabel;

public void show(Stage stage) {

    stage.setTitle("Horizon – Suppliers");

    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: " + BG + ";");

    root.setTop(buildTopBar(stage));
    root.setLeft(buildFormPanel());
    root.setCenter(buildTablePanel());

    Theme.show(stage, root, 1000, 700);

    loadSuppliers();
}

private HBox buildTopBar(Stage stage) {
    return Theme.topBar(stage, "Suppliers");
}

private ScrollPane buildFormPanel() {

    VBox panel = new VBox(12);
    panel.setPrefWidth(270);
    panel.setPadding(new Insets(18));
    panel.getStyleClass().add("form-panel");

    Label formTitle = new Label("Supplier Details");
    formTitle.getStyleClass().add("form-title");

    nameField = field("Supplier Name");
    addressField = field("Address");
    companyField = field("Company Name");
    emailField = field("Email");
    phoneField = field("Phone Number");

    Button addBtn = actionButton("Add", PURPLE, "white");
    Button updateBtn = actionButton("Update", PURPLE, "white");
    Button deleteBtn = actionButton("Delete", PURPLE, "white");
    Button clearBtn = actionButton("Clear", PURPLE, "white");

    addBtn.setMaxWidth(Double.MAX_VALUE);
    updateBtn.setMaxWidth(Double.MAX_VALUE);
    deleteBtn.setMaxWidth(Double.MAX_VALUE);
    clearBtn.setMaxWidth(Double.MAX_VALUE);

    addBtn.setOnAction(e -> addSupplier());
    updateBtn.setOnAction(e -> updateSupplier());
    deleteBtn.setOnAction(e -> deleteSupplier());
    clearBtn.setOnAction(e -> clearForm());

    statusLabel = new Label("");
    statusLabel.setFont(Font.font("Arial", 12));
    statusLabel.setWrapText(true);

    panel.getChildren().addAll(
            formTitle,
            label("Name"), nameField,
            label("Address"), addressField,
            label("Company"), companyField,
            label("Email"), emailField,
            label("Phone"), phoneField,
            addBtn, updateBtn, deleteBtn, clearBtn,
            statusLabel
    );

    return Theme.scrollable(panel);
}

private VBox buildTablePanel() {

    VBox panel = new VBox(15);
    panel.setPadding(new Insets(25));

    Button refreshBtn = actionButton("Refresh", PURPLE, "white");
    refreshBtn.setOnAction(e -> loadSuppliers());

    HBox topRow = new HBox(10, refreshBtn);
    topRow.setAlignment(Pos.CENTER_LEFT);

    table = new TableView<>();
    Theme.styleTable(table);

    table.getColumns().addAll(
            col("ID", "supplierId", 70),
            col("Name", "supplierName", 150),
            col("Address", "supplierAddress", 170),
            col("Company", "companyName", 150),
            col("Email", "email", 180),
            col("Phone", "phoneNumber", 120)
    );

    TextField searchField = Theme.searchBox(table, supplierList);
    topRow.getChildren().add(0, searchField);

    table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
        if (selected != null) {
            nameField.setText(selected.getSupplierName());
            addressField.setText(selected.getSupplierAddress());
            companyField.setText(selected.getCompanyName());
            emailField.setText(selected.getEmail());
            phoneField.setText(selected.getPhoneNumber() == null ? "" : selected.getPhoneNumber());
        }
    });

    panel.getChildren().addAll(topRow, table);
    return panel;
}

private void loadSuppliers() {
    supplierList.clear();
    supplierList.addAll(SupplierDAO.getAllSuppliers());
}

private void addSupplier() {

    if (!valid()) return;

    Supplier supplier = new Supplier(
            0,
            nameField.getText().trim(),
            addressField.getText().trim(),
            companyField.getText().trim(),
            emailField.getText().trim(),
            phoneField.getText().trim()
    );

    if (SupplierDAO.addSupplier(supplier)) {
        status("Supplier added.", PURPLE);
        clearForm();
        loadSuppliers();
    } else {
        status("Failed to add supplier.", PURPLE);
    }
}

private void updateSupplier() {

    Supplier selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select a supplier first.", PURPLE);
        return;
    }

    if (!valid()) return;

    Supplier supplier = new Supplier(
            selected.getSupplierId(),
            nameField.getText().trim(),
            addressField.getText().trim(),
            companyField.getText().trim(),
            emailField.getText().trim(),
            phoneField.getText().trim()
    );

    if (SupplierDAO.updateSupplier(supplier)) {
        status("Supplier updated.", PURPLE);
        clearForm();
        loadSuppliers();
    } else {
        status("Failed to update supplier.", PURPLE);
    }
}

private void deleteSupplier() {

    Supplier selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select a supplier first.", PURPLE);
        return;
    }

    if (!Theme.confirm("Delete the selected supplier? This cannot be undone.")) return;

    if (SupplierDAO.deleteSupplier(selected.getSupplierId())) {
        status("Supplier deleted.", ORANGE);
        clearForm();
        loadSuppliers();
    } else {
    	status("Supplier has supplier orders and cannot be deleted.", ORANGE);
    }
}

private boolean valid() {

    if (nameField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty() ||
            companyField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {

        status("All fields are required except phone.", PURPLE);
        return false;
    }

    if (!emailField.getText().contains("@")) {
        status("Email must contain @.", ORANGE);
        return false;
    }

    return true;
}

private void clearForm() {
    nameField.clear();
    addressField.clear();
    companyField.clear();
    emailField.clear();
    phoneField.clear();

    table.getSelectionModel().clearSelection();
}

private void status(String msg, String color) {
    Theme.status(statusLabel, msg);
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

private <T> TableColumn<Supplier, T> col(String title, String prop, double width) {
    TableColumn<Supplier, T> column = new TableColumn<>(title);
    column.setCellValueFactory(new PropertyValueFactory<>(prop));
    column.setPrefWidth(width);
    return column;
}

}
