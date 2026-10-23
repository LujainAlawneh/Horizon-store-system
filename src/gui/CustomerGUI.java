 package gui;

import dao.CustomerDAO;
import entity.Customer;

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

import java.sql.Date;

public class CustomerGUI {

    private static final String PURPLE="#2D1B69";
    private static final String ORANGE ="#F5A623";
    private static final String BG="#F0EBF8";

    private TableView<Customer> table;
    private ObservableList<Customer> customerList =FXCollections.observableArrayList();

    private TextField firstNameField,lastNameField,birthDateField,emailField,phoneField,addressField;
    private Label statusLabel;

    public void show(Stage stage) {
        stage.setTitle("Horizon – Customers");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: "+BG+";");

        root.setTop(buildTopBar(stage));
        root.setLeft(buildFormPanel());
        root.setCenter(buildTablePanel());

        Theme.show(stage, root, 1000, 700);
        loadCustomers();
    }

    // ── Top bar ─────────────────────────────────────────────────────────────
    private HBox buildTopBar(Stage stage) {
    return Theme.topBar(stage, "Customers");
}

    // ── Left form panel ──────────────────────────────────────────────────────
    private ScrollPane buildFormPanel() {
        VBox panel = new VBox(12);
        panel.setPrefWidth(260);
        panel.setPadding(new Insets(18));
        panel.getStyleClass().add("form-panel");

        Label formTitle = new Label("Customer Details");
        formTitle.getStyleClass().add("form-title");

        firstNameField =field("First Name");
        lastNameField=field("Last Name");
        birthDateField =field("Birth Date (YYYY-MM-DD)");
        emailField=field("Email");
        phoneField =field("Phone");
        addressField=field("Address");

        Button addBtn=actionButton("Add",PURPLE,"white");
        Button updateBtn=actionButton("Update",PURPLE,"white");
        Button deleteBtn=actionButton("Delete",PURPLE,"white");
        Button clearBtn =actionButton("Clear",PURPLE,"white");

        addBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setMaxWidth(Double.MAX_VALUE);

        addBtn.setOnAction(e ->addCustomer());
        updateBtn.setOnAction(e->updateCustomer());
        deleteBtn.setOnAction(e->deleteCustomer());
        clearBtn.setOnAction(e->clearForm());
        
        statusLabel = new Label("");
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setWrapText(true);
        
        panel.getChildren().addAll(
            formTitle,
            label("First Name"),firstNameField,
            label("Last Name"),lastNameField,
            label("Birth Date"), birthDateField,
            label("Email"),emailField,
            label("Phone"),phoneField,
            label("Address"),addressField,
            addBtn,updateBtn, deleteBtn,clearBtn,
            statusLabel
        );

        return Theme.scrollable(panel);
    }

    // ── Center table panel ───────────────────────────────────────────────────
    private VBox buildTablePanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(25));



        Button refreshBtn=actionButton("Refresh", PURPLE, "white");
        refreshBtn.setOnAction(e->loadCustomers());


        HBox searchRow = new HBox(10,refreshBtn);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        // Table
        table = new TableView<>();
        Theme.styleTable(table);
        

        table.getColumns().addAll(
            col("ID", "customerId",60),
            col("First Name","firstName",110),
            col("Last Name", "lastName",110),
            col("Birth Date","customerBirthDate",110),
            col("Email","customerEmail",160),
            col("Phone","phoneNumber",120),
            col("Address","address",150)
        );

        TextField searchField = Theme.searchBox(table, customerList);
    searchRow.getChildren().add(0, searchField);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                firstNameField.setText(sel.getFirstName());
                lastNameField .setText(sel.getLastName());
                birthDateField.setText(String.valueOf(sel.getCustomerBirthDate()));
                emailField.setText(sel.getCustomerEmail());
                phoneField.setText(sel.getPhoneNumber());
                addressField .setText(sel.getAddress());
            }
        });

        panel.getChildren().addAll(searchRow,table);
        return panel;
    }

    // ── DB operations ────────────────────────────────────────────────────────
    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(CustomerDAO.getAllCustomers());
    }

    private void addCustomer() {
        if (!valid()) return;
        Customer c = new Customer(0,
            firstNameField.getText().trim(),
            lastNameField.getText().trim(),
            Date.valueOf(birthDateField.getText().trim()),
            emailField.getText().trim(),
            addressField.getText().trim(),
            phoneField.getText().trim());

        if (CustomerDAO.addCustomer(c)) 
        { status("Customer added.",PURPLE); clearForm(); loadCustomers(); }
        else status("Failed to add customer.",PURPLE);
    }

    private void updateCustomer() {
        Customer sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { status("Select a customer first.",PURPLE); return; }
        if (!valid()) return;

        Customer c = new Customer(sel.getCustomerId(),
            firstNameField.getText().trim(),
            lastNameField.getText().trim(),
            Date.valueOf(birthDateField.getText().trim()),
            emailField.getText().trim(),
            addressField.getText().trim(),
            phoneField.getText().trim());

        if (CustomerDAO.updateCustomer(c))
        { status("Customer updated.",PURPLE); clearForm(); loadCustomers(); }
        else status("Failed to update customer.",PURPLE);
    }

    private void deleteCustomer() {

        Customer sel = table.getSelectionModel().getSelectedItem();

        if (sel == null) {
            status("Select a customer first.",PURPLE);
            return;
        }

    if (!Theme.confirm("Delete the selected customer? This cannot be undone.")) return;

        if (CustomerDAO.deleteCustomer(sel.getCustomerId())) {
            status("Customer deleted.", ORANGE);
            clearForm();
            loadCustomers();
        } else {
            status("Failed to delete.",PURPLE);
        }
    }
    private boolean valid() {
        if (firstNameField.getText().trim().isEmpty()||
            lastNameField.getText().trim().isEmpty()||
            birthDateField.getText().trim().isEmpty()||
            emailField.getText().trim().isEmpty()||
            addressField.getText().trim().isEmpty()) {
            status("All fields are required except phone.",PURPLE);
            return false;
        }
        try { Date.valueOf(birthDateField.getText().trim()); }
        catch (IllegalArgumentException ex) { status("Birth date must be YYYY-MM-DD.", ORANGE); return false; }
        return true;
    }

    private void clearForm() {
        firstNameField.clear(); lastNameField.clear(); birthDateField.clear();
        emailField.clear();  phoneField.clear();  addressField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void status(String msg, String color) {
    Theme.status(statusLabel, msg);
}
    // ── Helpers ──────────────────────────────────────────────────────────────
    private Label label(String text) {
    return Theme.label(text);
}

    private TextField field(String prompt) {
    return Theme.field(prompt);
}

    private Button actionButton(String text, String bg, String fg) {
    return Theme.actionButton(text, bg);
}

    private <T> TableColumn<Customer, T> col(String title, String prop, double width) {
        TableColumn<Customer, T> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(width);
        return c;
    }
}