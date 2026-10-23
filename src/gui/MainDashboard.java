package gui;

import java.sql.Connection;

import database.DBConnection;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainDashboard {

    public void show(Stage stage) {
        stage.setTitle("Horizon Store System");

        BorderPane root = new BorderPane();
        root.setTop(buildTopBar());
        root.setCenter(buildContent(stage));
        root.setBottom(buildFooter());

        Theme.show(stage, root, 1000, 700);
        stage.show();
    }

    private HBox buildTopBar() {
        Circle circle = new Circle(26);
        circle.setFill(Color.web(Theme.PURPLE));
        circle.setStroke(Color.web(Theme.ORANGE));
        circle.setStrokeWidth(4);

        Label logoText = new Label("HS");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        logoText.setTextFill(Color.web(Theme.ORANGE));

        StackPane logo = new StackPane(circle, logoText);

        Label title = new Label("Horizon Store System");
        title.getStyleClass().add("top-title");

        HBox bar = new HBox(18, logo, title);
        bar.getStyleClass().add("top-bar");
        return bar;
    }

    private VBox buildContent(Stage stage) {
        Label welcome = new Label("Welcome to Horizon Store System");
        welcome.getStyleClass().add("welcome-title");

        Label subtitle = new Label("Choose a module to get started");
        subtitle.getStyleClass().add("welcome-subtitle");

        Button products  = card("Products",  "Catalog, stock and inventory");
        Button customers = card("Customers", "Customer records and contacts");
        Button employees = card("Employees", "Staff, shifts and performance");
        Button orders    = card("Orders",    "Sales, order items and payments");
        Button suppliers = card("Suppliers", "Supplier directory");
        Button reports   = card("Reports",   "Charts and business insights");
        Button exit      = card("Exit",      "Close the application");

        products.setOnAction(e -> new ProductInventoryGUI().show(stage));
        customers.setOnAction(e -> new CustomerGUI().show(stage));
        employees.setOnAction(e -> new EmployeeGUI().show(stage));
        orders.setOnAction(e -> new OrdersGUI().show(stage));
        suppliers.setOnAction(e -> new SupplierGUI().show(stage));
        reports.setOnAction(e -> new ReportsGUI().show(stage));
        exit.setOnAction(e -> stage.close());

        GridPane grid = new GridPane();
        grid.setHgap(26);
        grid.setVgap(26);
        grid.setAlignment(Pos.CENTER);

        grid.add(products, 0, 0);
        grid.add(customers, 1, 0);
        grid.add(employees, 2, 0);
        grid.add(orders, 0, 1);
        grid.add(suppliers, 1, 1);
        grid.add(reports, 2, 1);
        grid.add(exit, 1, 2);

        VBox content = new VBox(8, welcome, subtitle, grid);
        content.setAlignment(Pos.TOP_CENTER);
        content.setSpacing(8);
        VBox.setMargin(grid, new javafx.geometry.Insets(32, 0, 0, 0));
        content.setPadding(new javafx.geometry.Insets(45, 40, 20, 40));
        return content;
    }

    private HBox buildFooter() {
        Label dbStatus = new Label("● Checking database…");
        dbStatus.getStyleClass().add("footer-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label app = new Label("Horizon Store System");
        app.getStyleClass().add("footer-text");

        HBox footer = new HBox(dbStatus, spacer, app);
        footer.getStyleClass().add("footer");

        // Check the connection off the UI thread so the window never freezes.
        Thread checker = new Thread(() -> {
            boolean ok;
            try (Connection con = DBConnection.getConnection()) {
                ok = con != null && con.isValid(2);
            } catch (Exception e) {
                ok = false;
            }

            final boolean connected = ok;
            Platform.runLater(() -> {
                dbStatus.getStyleClass().removeAll("footer-text", "db-ok", "db-error");
                if (connected) {
                    dbStatus.setText("● Database connected");
                    dbStatus.getStyleClass().add("db-ok");
                } else {
                    dbStatus.setText("● Database offline - check db.properties");
                    dbStatus.getStyleClass().add("db-error");
                }
            });
        });
        checker.setDaemon(true);
        checker.start();

        return footer;
    }

    private Button card(String title, String subtitle) {
        Circle badge = new Circle(22, Color.web(Theme.ORANGE));
        Label initial = new Label(title.substring(0, 1));
        initial.getStyleClass().add("card-initial");

        Label name = new Label(title);
        name.getStyleClass().add("card-title");

        Label description = new Label(subtitle);
        description.getStyleClass().add("card-subtitle");
        description.setWrapText(true);
        description.setMaxWidth(170);

        HBox content = new HBox(14, new StackPane(badge, initial), new VBox(3, name, description));
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setPrefSize(275, 96);
        button.getStyleClass().add("dash-card");
        return button;
    }
}
