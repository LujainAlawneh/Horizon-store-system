package gui;

import java.util.ArrayList;
import java.util.List;

import dao.CustomerDAO;
import dao.OrdersDAO;
import dao.ProductDAO;
import entity.BestSellingProduct;
import entity.CategoryProductCount;
import entity.LowStockProduct;
import entity.ProductRevenueReport;
import entity.TopCustomer;
import entity.WarehouseCapacityReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReportsGUI {

    private VBox resultBox;

    public void show(Stage stage) {
        stage.setTitle("Horizon – Reports");

        BorderPane root = new BorderPane();
        root.setTop(Theme.topBar(stage, "Reports & Charts"));
        root.setCenter(buildContent());

        Theme.show(stage, root, 1000, 700);

        showProductsChart();
    }

    private VBox buildContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(25));

        Label pageTitle = new Label("Reports Dashboard");
        pageTitle.getStyleClass().add("form-title");

        Button productsChartBtn  = Theme.actionButton("Top Products Chart", Theme.PURPLE);
        Button customersChartBtn = Theme.actionButton("Top Customers Chart", Theme.PURPLE);
        Button categoryBtn       = Theme.actionButton("Category Summary", Theme.ORANGE);
        Button warehouseBtn      = Theme.actionButton("Low Storage Warehouses", Theme.ORANGE);
        Button lowStockBtn       = Theme.actionButton("Low Stock Products", Theme.ORANGE);
        Button dailySalesBtn     = Theme.actionButton("Daily Sales Report", Theme.ORANGE);
        Button revenueBtn        = Theme.actionButton("Revenue & Returns", Theme.ORANGE);

        productsChartBtn.setOnAction(e -> showProductsChart());
        customersChartBtn.setOnAction(e -> showCustomersChart());
        categoryBtn.setOnAction(e -> showCategorySummary());
        warehouseBtn.setOnAction(e -> showLowStorageWarehouses());
        lowStockBtn.setOnAction(e -> showLowStockProducts());
        dailySalesBtn.setOnAction(e -> showDailySales());
        revenueBtn.setOnAction(e -> showRevenue());

        // Buttons wrap onto a second line on narrow windows instead of overflowing.
        FlowPane buttons = new FlowPane(10, 10,
                productsChartBtn, customersChartBtn, categoryBtn,
                warehouseBtn, lowStockBtn, dailySalesBtn, revenueBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);

        resultBox = new VBox(15);
        resultBox.setAlignment(Pos.TOP_CENTER);
        resultBox.getStyleClass().add("result-card");
        VBox.setVgrow(resultBox, Priority.ALWAYS);

        content.getChildren().addAll(pageTitle, buttons, resultBox);
        return content;
    }

    // ── Charts ──────────────────────────────────────────────────────────────

    private void showProductsChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (BestSellingProduct p : ProductDAO.getTop5BestSellingProducts()) {
            data.add(new PieChart.Data(
                    p.getProductName() + " (" + p.getTotalQuantitySold() + ")",
                    p.getTotalQuantitySold()));
        }
        showChart("Top 5 Best Selling Products", "Quantity Sold", data);
    }

    private void showCustomersChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (TopCustomer c : CustomerDAO.getTop5CustomersByOrders()) {
            data.add(new PieChart.Data(
                    c.getCustomerName() + " (" + c.getNumberOfOrders() + ")",
                    c.getNumberOfOrders()));
        }
        showChart("Top 5 Customers By Orders", "Number Of Orders", data);
    }

    private void showChart(String heading, String chartTitle, ObservableList<PieChart.Data> data) {
        resultBox.getChildren().clear();

        Label title = new Label(heading);
        title.getStyleClass().add("section-title");

        PieChart chart = new PieChart(data);
        chart.setTitle(chartTitle);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(true);
        VBox.setVgrow(chart, Priority.ALWAYS);

        resultBox.getChildren().addAll(title, chart);
    }

    // ── Table reports ───────────────────────────────────────────────────────

    private void showCategorySummary() {
        List<String[]> rows = new ArrayList<>();
        for (CategoryProductCount c : ProductDAO.getProductsCountByCategory()) {
            rows.add(new String[] {
                    c.getCategoryName(),
                    String.valueOf(c.getProductCount()),
                    String.format("%.2f", c.getAveragePrice())
            });
        }
        showTable("Category Summary",
                new String[] { "Category", "Products", "Average Price" }, rows);
    }

    private void showLowStorageWarehouses() {
        List<String[]> rows = new ArrayList<>();
        for (WarehouseCapacityReport w : ProductDAO.getWarehousesBelow20PercentStorage()) {
            rows.add(new String[] {
                    String.valueOf(w.getWarehouseId()),
                    w.getLocation(),
                    String.valueOf(w.getMaximumStorage()),
                    String.valueOf(w.getAvailableStorage())
            });
        }
        showTable("Warehouses Below 20% Available Storage",
                new String[] { "ID", "Location", "Max Storage", "Available" }, rows);
    }

    private void showLowStockProducts() {
        List<String[]> rows = new ArrayList<>();
        for (LowStockProduct p : ProductDAO.getLowStockProducts()) {
            rows.add(new String[] {
                    p.getProductName(),
                    p.getWarehouseLocation(),
                    String.valueOf(p.getQuantityAvailable()),
                    String.valueOf(p.getMinimumStockLevel())
            });
        }
        showTable("Products Below Minimum Stock Level",
                new String[] { "Product", "Warehouse", "Available", "Minimum" }, rows);
    }

    private void showDailySales() {
        showTable("Daily Sales Report",
                new String[] { "Order Date", "Orders", "Total Sales" },
                OrdersDAO.getDailySalesReport());
    }

    private void showRevenue() {
        List<String[]> rows = new ArrayList<>();
        for (ProductRevenueReport p : ProductDAO.getProductRevenueReport()) {
            rows.add(new String[] {
                    p.getProductName(),
                    String.format("%.2f", p.getSalesRevenue()),
                    String.format("%.2f", p.getReturnLoss()),
                    String.format("%.2f", p.getNetRevenue())
            });
        }
        showTable("Products Revenue And Return Loss",
                new String[] { "Product", "Sales", "Returns", "Net Revenue" }, rows);
    }

    private void showTable(String heading, String[] headers, List<String[]> rows) {
        resultBox.getChildren().clear();

        Label title = new Label(heading);
        title.getStyleClass().add("section-title");

        resultBox.getChildren().addAll(title, Theme.stringTable(headers, rows));
    }
}
