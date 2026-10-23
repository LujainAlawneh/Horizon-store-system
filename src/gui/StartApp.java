package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class StartApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainDashboard dashboard = new MainDashboard();
        dashboard.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}