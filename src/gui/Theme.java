package gui;

import java.net.URL;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Shared look-and-feel and UI helpers for every Horizon screen.
 *
 * The visual styling itself lives in style.css (same package); this class only
 * attaches that stylesheet and builds the small widgets all screens have in
 * common (top bar, buttons, fields, search box, confirm dialog...).
 */
public final class Theme {

    public static final String PURPLE = "#2D1B69";
    public static final String ORANGE = "#F5A623";
    public static final String BG     = "#F0EBF8";

    private static final String STYLESHEET = loadStylesheet();

    private static final String[] ERROR_WORDS = {
        "fail", "cannot", "must", "check", "select", "required", "invalid", "error", "enter", "not "
    };

    private Theme() { }

    private static String loadStylesheet() {
        URL url = Theme.class.getResource("style.css");
        return url == null ? null : url.toExternalForm();
    }

    // ── Scenes & windows ────────────────────────────────────────────────────

    /** Creates a scene with the Horizon stylesheet attached. */
    public static Scene scene(Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        if (STYLESHEET != null) {
            scene.getStylesheets().add(STYLESHEET);
        }
        return scene;
    }

    /**
     * Shows a screen in the main window. If the window is already open it keeps
     * its current size, so moving between screens no longer resets the window.
     */
    public static void show(Stage stage, Parent root, double defaultWidth, double defaultHeight) {
        double width = defaultWidth;
        double height = defaultHeight;

        Scene old = stage.getScene();
        if (old != null && stage.isShowing()) {
            width = old.getWidth();
            height = old.getHeight();
        }

        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene(root, width, height));
    }

    // ── Reusable widgets ────────────────────────────────────────────────────

    /** Purple top bar with a "back to dashboard" button, a title and the brand name. */
    public static HBox topBar(Stage stage, String title) {
        Button back = new Button("← Dashboard");
        back.getStyleClass().add("back-btn");
        back.setOnAction(e -> new MainDashboard().show(stage));

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("top-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label brand = new Label("Horizon");
        brand.getStyleClass().add("top-brand");

        HBox bar = new HBox(18, back, titleLabel, spacer, brand);
        bar.getStyleClass().add("top-bar");
        return bar;
    }

    public static Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }

    public static TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    /**
     * Full-width button. Orange colour = accent button, "Delete..." = red
     * danger button, anything else = primary purple button.
     */
    public static Button actionButton(String text, String color) {
        Button button = new Button(text);

        String styleClass;
        if (text.startsWith("Delete")) {
            styleClass = "btn-danger";
        } else if (ORANGE.equalsIgnoreCase(color)) {
            styleClass = "btn-accent";
        } else {
            styleClass = "btn-primary";
        }

        button.getStyleClass().add(styleClass);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    /** Wraps a form panel in a scroll pane so buttons never get cut off on small windows. */
    public static ScrollPane scrollable(VBox panel) {
        ScrollPane scroll = new ScrollPane(panel);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefWidth(panel.getPrefWidth() + 18);
        scroll.getStyleClass().add("form-scroll");
        return scroll;
    }

    // ── Status messages & dialogs ───────────────────────────────────────────

    /** Shows a message under the form: green for success, red for problems. */
    public static void status(Label label, String message) {
        label.setText(message);
        label.getStyleClass().removeAll("status-ok", "status-error");

        String lower = message == null ? "" : message.toLowerCase();
        boolean error = false;
        for (String word : ERROR_WORDS) {
            if (lower.contains(word)) {
                error = true;
                break;
            }
        }
        label.getStyleClass().add(error ? "status-error" : "status-ok");
    }

    /** Asks the user to confirm a destructive action. */
    public static boolean confirm(String message) {
        ButtonType yes = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, yes, no);
        alert.setTitle("Please confirm");
        alert.setHeaderText(null);
        if (STYLESHEET != null) {
            alert.getDialogPane().getStylesheets().add(STYLESHEET);
        }

        return alert.showAndWait().filter(button -> button == yes).isPresent();
    }

    // ── Tables ──────────────────────────────────────────────────────────────

    /** Common table setup: columns fill the width, friendly empty message, grows with the window. */
    public static void styleTable(TableView<?> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No records to show"));
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    /**
     * Puts a live search filter in front of a table. Typing matches against
     * every visible column. Returns the search box so the caller can place it.
     * Column-header sorting keeps working.
     */
    public static <T> TextField searchBox(TableView<T> table, ObservableList<T> source) {
        FilteredList<T> filtered = new FilteredList<>(source, item -> true);
        SortedList<T> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        TextField box = new TextField();
        box.setPromptText("Search…");
        box.setPrefWidth(220);

        box.textProperty().addListener((obs, oldText, newText) -> {
            String query = newText == null ? "" : newText.trim().toLowerCase();

            filtered.setPredicate(item -> {
                if (query.isEmpty()) {
                    return true;
                }
                for (TableColumn<T, ?> column : table.getColumns()) {
                    Object value = column.getCellData(item);
                    if (value != null && String.valueOf(value).toLowerCase().contains(query)) {
                        return true;
                    }
                }
                return false;
            });
        });

        return box;
    }

    /** Read-only table built from rows of strings (used by the report screens). */
    public static TableView<String[]> stringTable(String[] headers, List<String[]> rows) {
        TableView<String[]> table = new TableView<>();

        for (int i = 0; i < headers.length; i++) {
            final int index = i;

            TableColumn<String[], String> column = new TableColumn<>(headers[i]);
            column.setCellValueFactory(cell -> {
                String[] row = cell.getValue();
                return new SimpleStringProperty(index < row.length ? row[index] : "");
            });
            table.getColumns().add(column);
        }

        table.setItems(FXCollections.observableArrayList(rows));
        styleTable(table);
        return table;
    }
}
