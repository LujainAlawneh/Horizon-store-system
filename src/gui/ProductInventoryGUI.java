package gui;

import dao.ProductDAO;
import entity.*;

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

public class ProductInventoryGUI {

private static final String PURPLE = "#2D1B69";
private static final String ORANGE = "#F5A623";
private static final String BG = "#F0EBF8";

private TableView<Product> table;
private ObservableList<Product> productList = FXCollections.observableArrayList();

private TextField nameField, brandField, priceField, warrantyField, categoryField;
private Label statusLabel;

public void show(Stage stage) {
    stage.setTitle("Horizon – Product Inventory");

    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: " + BG + ";");

    root.setTop(buildTopBar(stage));
    root.setLeft(buildFormPanel());
    root.setCenter(buildTablePanel());

    Theme.show(stage, root, 1050, 700);

    loadProducts();
}

private HBox buildTopBar(Stage stage) {
    return Theme.topBar(stage, "Product Inventory");
}

private ScrollPane buildFormPanel() {
    VBox panel = new VBox(12);
    panel.setPrefWidth(285);
    panel.setPadding(new Insets(18));
    panel.getStyleClass().add("form-panel");

    Label formTitle = new Label("Product Details");
    formTitle.getStyleClass().add("form-title");

    nameField = field("Product Name");
    brandField = field("Brand");
    priceField = field("Price");
    warrantyField = field("Warranty");
    categoryField = field("Category ID");

    Button addBtn = actionButton("Add", PURPLE, "white");
    Button updateBtn = actionButton("Update", PURPLE, "white");
    Button deleteBtn = actionButton("Delete", PURPLE, "white");
    Button clearBtn = actionButton("Clear", PURPLE, "white");

    Button topProductsBtn = actionButton("Top 5 Products", ORANGE, "white");
    Button categoryBtn = actionButton("Products By Category", PURPLE, "white");
    Button warehouseBtn = actionButton("Warehouses Below 20%", PURPLE, "white");
    Button lowStockBtn = actionButton("Low Stock Products", PURPLE, "white");

    addBtn.setMaxWidth(Double.MAX_VALUE);
    updateBtn.setMaxWidth(Double.MAX_VALUE);
    deleteBtn.setMaxWidth(Double.MAX_VALUE);
    clearBtn.setMaxWidth(Double.MAX_VALUE);
    topProductsBtn.setMaxWidth(Double.MAX_VALUE);
    categoryBtn.setMaxWidth(Double.MAX_VALUE);
    warehouseBtn.setMaxWidth(Double.MAX_VALUE);
    lowStockBtn.setMaxWidth(Double.MAX_VALUE);

    addBtn.setOnAction(e -> addProduct());
    updateBtn.setOnAction(e -> updateProduct());
    deleteBtn.setOnAction(e -> deleteProduct());
    clearBtn.setOnAction(e -> clearForm());

    topProductsBtn.setOnAction(e -> showTop5BestSellingProducts());
    categoryBtn.setOnAction(e -> showCategoryProductCount());
    warehouseBtn.setOnAction(e -> showWarehouseCapacityReport());
    lowStockBtn.setOnAction(e -> showLowStockProducts());

    statusLabel = new Label("");
    statusLabel.setFont(Font.font("Arial", 12));
    statusLabel.setWrapText(true);

    panel.getChildren().addAll(
            formTitle,
            label("Name"), nameField,
            label("Brand"), brandField,
            label("Price"), priceField,
            label("Warranty"), warrantyField,
            label("Category ID"), categoryField,
            addBtn, updateBtn, deleteBtn, clearBtn,
            topProductsBtn, categoryBtn, warehouseBtn, lowStockBtn,
            statusLabel
    );

    ScrollPane scrollPane = new ScrollPane(panel);
    scrollPane.getStyleClass().add("form-scroll");
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefWidth(285);

    return scrollPane;
}

private VBox buildTablePanel() {
    VBox panel = new VBox(15);
    panel.setPadding(new Insets(25));

    TextField priceAboveField = field("Price above");
    priceAboveField.setPrefWidth(140);

    Button priceSearchBtn = actionButton("Search Price", ORANGE, "white");
    Button refreshBtn = actionButton("Refresh", PURPLE, "white");

    priceSearchBtn.setOnAction(e -> searchProductsAbovePrice(priceAboveField));
    refreshBtn.setOnAction(e -> loadProducts());

    HBox topRow = new HBox(10, priceAboveField, priceSearchBtn, refreshBtn);
    topRow.setAlignment(Pos.CENTER_LEFT);

    table = new TableView<>();
    Theme.styleTable(table);

    table.getColumns().addAll(
            col("ID", "productId", 70),
            col("Name", "productName", 180),
            col("Brand", "brand", 130),
            col("Price", "price", 110),
            col("Warranty", "warrantyPeriod", 100),
            col("Category ID", "categoryId", 110),
            col("Quantity", "quantity", 100)
    );

    TextField searchField = Theme.searchBox(table, productList);
    topRow.getChildren().add(0, searchField);

    table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
        if (selected != null) {
            nameField.setText(selected.getProductName());
            brandField.setText(selected.getBrand());
            priceField.setText(String.valueOf(selected.getPrice()));
            warrantyField.setText(String.valueOf(selected.getWarrantyPeriod()));
            categoryField.setText(String.valueOf(selected.getCategoryId()));
        }
    });

    panel.getChildren().addAll(topRow, table);
    return panel;
}

private void loadProducts() {
    productList.clear();
    productList.addAll(ProductDAO.getAllProducts());
}

private void addProduct() {
    if (!valid()) return;

    Product product = new Product(
            0,
            nameField.getText().trim(),
            brandField.getText().trim(),
            Double.parseDouble(priceField.getText().trim()),
            Integer.parseInt(warrantyField.getText().trim()),
            Integer.parseInt(categoryField.getText().trim()),
            0
    );

    if (ProductDAO.addProduct(product)) {
        status("Product added.", PURPLE);
        clearForm();
        loadProducts();
    } else {
        status("Failed to add product.", PURPLE);
    }
}

private void updateProduct() {
    Product selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select a product first.", PURPLE);
        return;
    }

    if (!valid()) return;

    Product product = new Product(
            selected.getProductId(),
            nameField.getText().trim(),
            brandField.getText().trim(),
            Double.parseDouble(priceField.getText().trim()),
            Integer.parseInt(warrantyField.getText().trim()),
            Integer.parseInt(categoryField.getText().trim()),
            selected.getQuantity()
    );

    if (ProductDAO.updateProduct(product)) {
        status("Product updated.", PURPLE);
        clearForm();
        loadProducts();
    } else {
        status("Failed to update product.", PURPLE);
    }
}

private void deleteProduct() {
    Product selected = table.getSelectionModel().getSelectedItem();

    if (selected == null) {
        status("Select a product first.", PURPLE);
        return;
    }

    if (!Theme.confirm("Delete the selected product? This cannot be undone.")) return;

    if (ProductDAO.deleteProduct(selected.getProductId())) {
        status("Product deleted.", PURPLE);
        clearForm();
        loadProducts();
    } else {
        status("Failed to delete product.", PURPLE);
    }
}

private void searchProductsAbovePrice(TextField priceAboveField) {
    if (priceAboveField.getText().trim().isEmpty()) {
        status("Enter a price first.", PURPLE);
        return;
    }

    try {
        double price = Double.parseDouble(priceAboveField.getText().trim());

        productList.clear();
        productList.addAll(ProductDAO.getProductsAbovePrice(price));

        status("Showing products above " + price + ".", PURPLE);

    } catch (NumberFormatException ex) {
        status("Price must be a number.", ORANGE);
    }
}

private void showTop5BestSellingProducts() {
    java.util.List<BestSellingProduct> products = ProductDAO.getTop5BestSellingProducts();

    if (products.isEmpty()) {
        showMessage("Top 5 Best Selling Products", "No sales data found.");
        return;
    }

    StringBuilder result = new StringBuilder();

    int rank = 1;
    for (BestSellingProduct p : products) {
        result.append(rank++)
                .append(". ")
                .append(p.getProductName())
                .append(" (")
                .append(p.getTotalQuantitySold())
                .append(" sold)\n");
    }

    showMessage("Top 5 Best Selling Products", result.toString());
}

private void showCategoryProductCount() {
    java.util.List<CategoryProductCount> data = ProductDAO.getProductsCountByCategory();

    TableView<CategoryProductCount> reportTable = new TableView<>();

    reportTable.getColumns().addAll(
            reportCol("Category", "categoryName", 180),
            reportCol("Product Count", "productCount", 140),
            reportCol("Average Price", "averagePrice", 140)
    );

    reportTable.setItems(FXCollections.observableArrayList(data));

    showTableStage("Products Count By Category", reportTable, 550, 400);
}

private void showWarehouseCapacityReport() {
    java.util.List<WarehouseCapacityReport> data = ProductDAO.getWarehousesBelow20PercentStorage();

    TableView<WarehouseCapacityReport> reportTable = new TableView<>();

    reportTable.getColumns().addAll(
            reportCol("Warehouse ID", "warehouseId", 120),
            reportCol("Location", "location", 180),
            reportCol("Maximum Storage", "maximumStorage", 150),
            reportCol("Available Storage", "availableStorage", 150)
    );

    reportTable.setItems(FXCollections.observableArrayList(data));

    showTableStage("Warehouses Below 20% Storage", reportTable, 650, 400);
}

private void showLowStockProducts() {
    java.util.List<LowStockProduct> data = ProductDAO.getLowStockProducts();

    TableView<LowStockProduct> reportTable = new TableView<>();

    reportTable.getColumns().addAll(
            reportCol("Product", "productName", 180),
            reportCol("Location", "location", 180),
            reportCol("Quantity", "quantityAvailable", 120),
            reportCol("Minimum Stock", "minimumStockLevel", 140)
    );

    reportTable.setItems(FXCollections.observableArrayList(data));

    showTableStage("Low Stock Products", reportTable, 650, 400);
}

private boolean valid() {
    if (nameField.getText().trim().isEmpty() ||
            brandField.getText().trim().isEmpty() ||
            priceField.getText().trim().isEmpty() ||
            warrantyField.getText().trim().isEmpty() ||
            categoryField.getText().trim().isEmpty()) {

        status("All fields are required.", PURPLE);
        return false;
    }

    try {
        Double.parseDouble(priceField.getText().trim());
        Integer.parseInt(warrantyField.getText().trim());
        Integer.parseInt(categoryField.getText().trim());
    } catch (NumberFormatException ex) {
        status("Price, Warranty, and Category must be numbers.", ORANGE);
        return false;
    }

    return true;
}

private void clearForm() {
    nameField.clear();
    brandField.clear();
    priceField.clear();
    warrantyField.clear();
    categoryField.clear();

    table.getSelectionModel().clearSelection();
}

private void showMessage(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(title);
    alert.setContentText(content);
    alert.showAndWait();
}

private <T> void showTableStage(String titleText, TableView<T> reportTable, int width, int height) {
    VBox box = new VBox(10);
    box.setPadding(new Insets(15));

    Label title = new Label(titleText);
    title.getStyleClass().add("popup-title");

    box.getChildren().addAll(title, reportTable);

    Stage stage = new Stage();
    stage.setTitle(titleText);
    stage.setScene(Theme.scene(box, width, height));
    stage.show();
}

private <S, T> TableColumn<S, T> reportCol(String title, String prop, double width) {
    TableColumn<S, T> column = new TableColumn<>(title);
    column.setCellValueFactory(new PropertyValueFactory<>(prop));
    column.setPrefWidth(width);
    return column;
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

private <T> TableColumn<Product, T> col(String title, String prop, double width) {
    TableColumn<Product, T> column = new TableColumn<>(title);
    column.setCellValueFactory(new PropertyValueFactory<>(prop));
    column.setPrefWidth(width);
    return column;
}

}