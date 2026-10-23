package gui;

import dao.OrdersDAO;
import dao.PaymentDAO;
import entity.Orders;

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
import java.util.List;

public class OrdersGUI {

private static final String PURPLE = "#2D1B69";
private static final String ORANGE = "#F5A623";
private static final String BG = "#F0EBF8";

private TableView<Orders> table;
private ObservableList<Orders> orderList = FXCollections.observableArrayList();

private TextField dateField, customerIdField, employeeIdField;
private Label statusLabel;

public void show(Stage stage) {

    stage.setTitle("Horizon – Orders");

    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: " + BG + ";");

    root.setTop(buildTopBar(stage));
    root.setLeft(buildFormPanel());
    root.setCenter(buildTablePanel());

    Theme.show(stage, root, 1000, 700);

    loadOrders();
}

private HBox buildTopBar(Stage stage) {
    return Theme.topBar(stage, "Orders");
}

private ScrollPane buildFormPanel() {

    VBox panel = new VBox(12);
    panel.setPrefWidth(270);
    panel.setPadding(new Insets(18));
    panel.getStyleClass().add("form-panel");

    Label formTitle = new Label("Order Details");
    formTitle.getStyleClass().add("form-title");

    dateField = field("Date YYYY-MM-DD");
    customerIdField = field("Customer ID");
    employeeIdField = field("Employee ID");

    Button addBtn = actionButton("Add", PURPLE, "white");
    Button updateBtn = actionButton("Update", PURPLE, "white");
    Button deleteBtn = actionButton("Delete", PURPLE, "white");
    Button addDetailsBtn = actionButton("Add Order Details", PURPLE, "white");
    Button detailsBtn = actionButton("Show Order Details", PURPLE, "white");
    Button paymentBtn = actionButton("Add Payment", PURPLE, "white");
    Button clearBtn = actionButton("Clear", PURPLE, "white");

    addBtn.setMaxWidth(Double.MAX_VALUE);
    updateBtn.setMaxWidth(Double.MAX_VALUE);
    deleteBtn.setMaxWidth(Double.MAX_VALUE);
    addDetailsBtn.setMaxWidth(Double.MAX_VALUE);
    detailsBtn.setMaxWidth(Double.MAX_VALUE);
    paymentBtn.setMaxWidth(Double.MAX_VALUE);
    clearBtn.setMaxWidth(Double.MAX_VALUE);

    addBtn.setOnAction(e -> addOrder());
    updateBtn.setOnAction(e -> updateOrder());
    deleteBtn.setOnAction(e -> deleteOrder());
    addDetailsBtn.setOnAction(e -> showAddOrderDetailsWindow());
    detailsBtn.setOnAction(e -> showOrderDetails());
    paymentBtn.setOnAction(e -> showAddPaymentWindow());
    clearBtn.setOnAction(e -> clearForm());

    statusLabel = new Label("");
    statusLabel.setFont(Font.font("Arial", 12));
    statusLabel.setWrapText(true);

    panel.getChildren().addAll(
            formTitle,
            label("Order Date"), dateField,
            label("Customer ID"), customerIdField,
            label("Employee ID"), employeeIdField,
            addBtn, updateBtn, deleteBtn,
            addDetailsBtn, detailsBtn, paymentBtn, clearBtn,
            statusLabel
    );

    return Theme.scrollable(panel);
}

private VBox buildTablePanel() {

    VBox panel = new VBox(15);
    panel.setPadding(new Insets(25));

    Button refreshBtn = actionButton("Refresh", PURPLE, "white");
    refreshBtn.setOnAction(e -> loadOrders());

    HBox topRow = new HBox(10, refreshBtn);
    topRow.setAlignment(Pos.CENTER_LEFT);

    table = new TableView<>();
    Theme.styleTable(table);

    table.getColumns().addAll(
            col("Order ID", "orderId", 80),
            col("Date", "orderDate", 120),
            col("Total", "totalCost", 100),
            col("Customer ID", "customerId", 110),
            col("Employee ID", "employeeId", 110)
    );

    TextField searchField = Theme.searchBox(table, orderList);
    topRow.getChildren().add(0, searchField);

    table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
        if (selected != null) {
            dateField.setText(selected.getOrderDate().toString());
            customerIdField.setText(String.valueOf(selected.getCustomerId()));
            employeeIdField.setText(String.valueOf(selected.getEmployeeId()));
        }
    });

    panel.getChildren().addAll(topRow, table);
    return panel;
}

private void loadOrders() {
    orderList.clear();
    orderList.addAll(OrdersDAO.getAllOrders());
}

private void addOrder() {

    if (!valid()) return;

    Orders order = new Orders(
            0,
            Date.valueOf(dateField.getText().trim()),
            0.0,
            Integer.parseInt(customerIdField.getText().trim()),
            Integer.parseInt(employeeIdField.getText().trim())
    );

    if (OrdersDAO.addOrder(order)) {
        status("Order added.", PURPLE);
        clearForm();
        loadOrders();
    } else {
        status("Failed to add order.", PURPLE);
    }
}

private void updateOrder() {

    Orders selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an order first.", PURPLE);
        return;
    }

    if (!valid()) return;

    Orders order = new Orders(
            selected.getOrderId(),
            Date.valueOf(dateField.getText().trim()),
            selected.getTotalCost(),
            Integer.parseInt(customerIdField.getText().trim()),
            Integer.parseInt(employeeIdField.getText().trim())
    );

    if (OrdersDAO.updateOrder(order)) {
        status("Order updated.", PURPLE);
        clearForm();
        loadOrders();
    } else {
        status("Failed to update order.", PURPLE);
    }
}

private void deleteOrder() {

    Orders selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an order first.", PURPLE);
        return;
    }

    if (!Theme.confirm("Delete the selected order? This cannot be undone.")) return;

    if (OrdersDAO.deleteOrder(selected.getOrderId())) {
        status("Order deleted.", PURPLE);
        clearForm();
        loadOrders();
    } else {
        status("Failed to delete order.", PURPLE);
    }
}

private void showAddOrderDetailsWindow() {

    Orders selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an order first.", PURPLE);
        return;
    }

    Stage detailsStage = new Stage();
    detailsStage.setTitle("Add Order Details");

    VBox box = new VBox(10);
    box.setPadding(new Insets(15));

    Label title = new Label("Add Details for Order #" + selected.getOrderId());
    title.getStyleClass().add("popup-title");

    TextField productIdField = field("Product ID");
    TextField quantityField = field("Quantity");

    Button saveBtn = actionButton("Save Order Details", PURPLE, "white");
    saveBtn.setMaxWidth(Double.MAX_VALUE);

    saveBtn.setOnAction(e -> {
        try {
            int productId = Integer.parseInt(productIdField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (OrdersDAO.addOrderDetails(selected.getOrderId(), productId, quantity)) {
                status("Order details added.", PURPLE);
                detailsStage.close();
                loadOrders();
            } else {
                status("Failed to add order details.", PURPLE);
            }

        } catch (Exception ex) {
            status("Check Product ID and Quantity.", ORANGE);
        }
    });

    box.getChildren().addAll(
            title,
            label("Product ID"), productIdField,
            label("Quantity"), quantityField,
            saveBtn
    );

    detailsStage.setScene(Theme.scene(box, 350, 250));
    detailsStage.show();
}

private void showOrderDetails() {

    Orders selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an order first.", PURPLE);
        return;
    }

    List<String[]> details = OrdersDAO.getOrderDetailsByOrderId(selected.getOrderId());

    TableView<String[]> detailsTable = new TableView<>();

    String[] headers = {
            "Order ID", "Product", "Brand", "Quantity", "Unit Price",
            "Line Total", "Payment Date", "Amount", "Status", "Method", "Payment Details"
    };

    for (int i = 0; i < headers.length; i++) {
        final int index = i;

        TableColumn<String[], String> column = new TableColumn<>(headers[i]);

        column.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue()[index])
        );

        column.setPrefWidth(130);
        detailsTable.getColumns().add(column);
    }

    detailsTable.setItems(FXCollections.observableArrayList(details));

    VBox box = new VBox(10);
    box.setPadding(new Insets(15));

    Label title = new Label("Order Details + Payment for Order #" + selected.getOrderId());
    title.getStyleClass().add("popup-title");

    box.getChildren().addAll(title, detailsTable);

    Stage detailsStage = new Stage();
    detailsStage.setTitle("Order Details With Payment");
    detailsStage.setScene(Theme.scene(box, 1150, 450));
    detailsStage.show();
}

private void showAddPaymentWindow() {

    Orders selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select an order first.", PURPLE);
        return;
    }

    Stage paymentStage = new Stage();
    paymentStage.setTitle("Add Payment");

    VBox box = new VBox(10);
    box.setPadding(new Insets(15));

    Label title = new Label("Payment for Order #" + selected.getOrderId());
    title.getStyleClass().add("popup-title");

    TextField paymentDateField = field("Payment Date YYYY-MM-DD");
    TextField amountField = field("Amount");
    TextField statusField = field("Paid / Pending");

    ComboBox<String> methodBox = new ComboBox<>();
    methodBox.getItems().addAll("Cash", "Installment");
    methodBox.setValue("Cash");
    methodBox.setMaxWidth(Double.MAX_VALUE);

    TextField cashReceivedField = field("Cash Received");
    TextField changeField = field("Change Amount");

    TextField downPaymentField = field("Down Payment");
    TextField installmentsField = field("Number Of Installments");
    TextField monthlyField = field("Monthly Amount");
    TextField remainingField = field("Remaining Balance");

    VBox cashBox = new VBox(8, cashReceivedField, changeField);
    VBox installmentBox = new VBox(8, downPaymentField, installmentsField, monthlyField, remainingField);

    installmentBox.setVisible(false);
    installmentBox.setManaged(false);

    methodBox.setOnAction(e -> {
        boolean isCash = methodBox.getValue().equals("Cash");

        cashBox.setVisible(isCash);
        cashBox.setManaged(isCash);

        installmentBox.setVisible(!isCash);
        installmentBox.setManaged(!isCash);
    });

    Button saveBtn = actionButton("Save Payment", PURPLE, "white");
    saveBtn.setMaxWidth(Double.MAX_VALUE);

    saveBtn.setOnAction(e -> {

        try {
            boolean ok;

            if (methodBox.getValue().equals("Cash")) {

                ok = PaymentDAO.addCashPayment(
                        Date.valueOf(paymentDateField.getText().trim()),
                        Double.parseDouble(amountField.getText().trim()),
                        statusField.getText().trim(),
                        selected.getOrderId(),
                        Double.parseDouble(cashReceivedField.getText().trim()),
                        Double.parseDouble(changeField.getText().trim())
                );

            } else {

                ok = PaymentDAO.addInstallmentPayment(
                        Date.valueOf(paymentDateField.getText().trim()),
                        Double.parseDouble(amountField.getText().trim()),
                        statusField.getText().trim(),
                        selected.getOrderId(),
                        Double.parseDouble(downPaymentField.getText().trim()),
                        Integer.parseInt(installmentsField.getText().trim()),
                        Double.parseDouble(monthlyField.getText().trim()),
                        Double.parseDouble(remainingField.getText().trim())
                );
            }

            if (ok) {
                status("Payment added.", PURPLE);
                paymentStage.close();
            } else {
                status("Failed to add payment.", PURPLE);
            }

        } catch (Exception ex) {
            status("Check payment fields.", ORANGE);
        }
    });

    box.getChildren().addAll(
            title,
            label("Payment Date"), paymentDateField,
            label("Amount"), amountField,
            label("Payment Status"), statusField,
            label("Payment Method"), methodBox,
            cashBox,
            installmentBox,
            saveBtn
    );

    paymentStage.setScene(Theme.scene(box, 350, 500));
    paymentStage.show();
}

private boolean valid() {

    if (dateField.getText().trim().isEmpty() ||
            customerIdField.getText().trim().isEmpty() ||
            employeeIdField.getText().trim().isEmpty()) {

        status("All fields are required.", PURPLE);
        return false;
    }

    try {
        Date.valueOf(dateField.getText().trim());
        Integer.parseInt(customerIdField.getText().trim());
        Integer.parseInt(employeeIdField.getText().trim());

    } catch (Exception ex) {
        status("Check date and number fields.", ORANGE);
        return false;
    }

    return true;
}

private void clearForm() {
    dateField.clear();
    customerIdField.clear();
    employeeIdField.clear();
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

private <T> TableColumn<Orders, T> col(String title, String prop, double width) {
    TableColumn<Orders, T> column = new TableColumn<>(title);
    column.setCellValueFactory(new PropertyValueFactory<>(prop));
    column.setPrefWidth(width);
    return column;
}

}
