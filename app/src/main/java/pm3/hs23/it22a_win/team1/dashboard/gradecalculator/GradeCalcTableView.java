package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.IntegerStringConverter;
import atlantafx.base.util.DoubleStringConverter;
import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;

import java.util.regex.Pattern;

/**
 * The {@code GradeCalcTableView} class represents the table view for the grade
 * calculator feature.
 * It contains the table view and the columns.
 *
 * @version 09.11.2023
 * @autor Besart Morina
 */
public class GradeCalcTableView {

    private final TableView<Module> tableOverview;
    private final StackPane parentNode;
    private final TableColumn<Module, String> nameColumn;
    private final TableColumn<Module, String> shortNameColumn;
    private final TableColumn<Module, Integer> creditsColumn;
    private final TableColumn<Module, Integer> moduleGroupColumn;
    private final TableColumn<Module, Double> preGradeColumn;
    private final TableColumn<Module, Double> weightPreliminaryGradeColumn;
    private final TableColumn<Module, Double> examGradeColumn;
    private final TableColumn<Module, Double> calculatedGradeColumn;
    // This Regex pattern matches a number with up to two decimal places
    private static final Pattern AMOUNT_PATTERN_DOUBLE = Pattern.compile("^[0-9]{1}(\\.[0-9]{1,3})?$");
    private static final Pattern AMOUNT_PATTERN_INTEGER = Pattern.compile("^[0-9]+$");
    // This Regex pattern matches a string containing only letters, numbers, and
    // spaces
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\s]+");
    private Runnable onEditCommitCallback;

    /**
     * Constructs a new {@code GradeCalcTableView} object.
     */
    public GradeCalcTableView(StackPane parentNode) {
        this.parentNode = parentNode;
        // Initialize the TableView
        tableOverview = new TableView<>();
        tableOverview.setEditable(true);

        this.nameColumn = new TableColumn<>("Module Name");
        this.shortNameColumn = new TableColumn<>("Kürzel");
        this.creditsColumn = new TableColumn<>("Credits");
        this.moduleGroupColumn = new TableColumn<>("Module Gruppe");
        this.preGradeColumn = new TableColumn<>("Vornote");
        this.weightPreliminaryGradeColumn = new TableColumn<>("Gewichtung Vornote");
        this.examGradeColumn = new TableColumn<>("Prüfungsnote");
        this.calculatedGradeColumn = new TableColumn<>("Abschluss Note");

        nameColumn.minWidthProperty().set(300);
        shortNameColumn.minWidthProperty().set(70);
        creditsColumn.minWidthProperty().set(100);
        moduleGroupColumn.minWidthProperty().set(150);
        preGradeColumn.minWidthProperty().set(100);
        nameColumn.minWidthProperty().set(100);
        weightPreliminaryGradeColumn.minWidthProperty().set(170);
        examGradeColumn.minWidthProperty().set(150);
        calculatedGradeColumn.minWidthProperty().set(123);
        nameColumn.setStyle("back");

        setupStringColumn(nameColumn, 30);
        setupStringColumn(shortNameColumn, 5);
        setupIntegerColumn(creditsColumn, 0, 12);
        setupIntegerColumn(moduleGroupColumn, 0, 12);
        setupDoubleColumn(preGradeColumn, 1.00, 6.00);
        setupDoubleColumn(weightPreliminaryGradeColumn, 0.00, 100.00);
        setupDoubleColumn(examGradeColumn, 1.00, 6.00);

        setupCellValueFactories();
    }

    /**
     * Sets the callback for the onEditCommit event.
     *
     * @param onEditCommitCallback the callback to be set
     */
    public void setOnEditCommitCallback(Runnable onEditCommitCallback) {
        this.onEditCommitCallback = onEditCommitCallback;
    }

    /**
     * Returns the table view.
     *
     * @return the table view
     */
    public TableView<Module> getTableOverview() {
        return tableOverview;
    }

    /**
     * Shows the big view of the table view.
     */
    public void showBigView() {
        tableOverview.getColumns().clear();
        tableOverview.getColumns().addAll(
            nameColumn,
            shortNameColumn,
            creditsColumn,
            moduleGroupColumn,
            preGradeColumn,
            weightPreliminaryGradeColumn,
            examGradeColumn,
            calculatedGradeColumn);

        tableOverview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Shows the small view of the table view.
     * This view is used for the overview of the modules.
     */
    public void showSmallView() {
        tableOverview.getColumns().clear();
        tableOverview.getColumns().addAll(
            shortNameColumn,
            calculatedGradeColumn);
        tableOverview.setStyle("-fx-border-radius:0 0 10 10; -fx-background-radius: 0 0 10 10;");
        tableOverview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Sets up the cell value factories for the table view.
     *
     * @param column The column to set up the cell value factory for.
     * @param max    The maximum length of the string.
     */
    private void setupStringColumn(TableColumn<Module, String> column, int max) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            String value;
            if (event.getNewValue() != null && NAME_PATTERN.matcher(event.getNewValue()).find() && event.getNewValue().length() <= max) {
                value = event.getNewValue();
            } else {
                value = event.getOldValue();
                showNotification("Bitte geben Sie ein Text der maximalien länge von " + max + " an.");
            }

            Module module = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (column == nameColumn) {
                module.setName(value);
            } else if (column == shortNameColumn) {
                module.setShortName(value);
            }
            tableOverview.refresh();
        });
    }

    /**
     * Sets up the cell value factories for the table view.
     *
     * @param column The column to set up the cell value factory for.
     * @param min    The minimum value of the integer Input.
     * @param max    The maximum value of the integer Input.
     */
    private void setupIntegerColumn(TableColumn<Module, Integer> column, int min, int max) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        column.setOnEditCommit(event -> {
            Integer value;
            if (AMOUNT_PATTERN_INTEGER.matcher(event.getNewValue().toString()).find()
                && event.getNewValue() >= min && event.getNewValue() <= max) {
                value = event.getNewValue();
            } else {
                value = event.getOldValue();
                showNotification("Bitte geben Sie eine Zahl zwischen " + min + " und " + max + " an.");
            }
            Module module = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (column == creditsColumn) {
                module.setCredits(value);
            } else if (column == moduleGroupColumn) {
                module.setModuleGroup(value);
            }
            tableOverview.refresh();
            if (onEditCommitCallback != null) {
                onEditCommitCallback.run();
            }
        });
    }

    /**
     * Sets up the cell value factories for the table view.
     *
     * @param column The column to set up the cell value factory for.
     * @param min    The minimum value of the double Input.
     * @param max    The maximum value of the double Input.
     */
    private void setupDoubleColumn(TableColumn<Module, Double> column, double min, double max) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column.setOnEditCommit(event -> {
            Double value;
            if (event.getNewValue() != null && AMOUNT_PATTERN_DOUBLE.matcher(event.getNewValue().toString()).find() && event.getNewValue() >= min && event.getNewValue() <= max) {
                value = event.getNewValue();
            } else {
                value = event.getOldValue();
                showNotification("Bitte geben Sie eine Zahl zwischen " + min + " und " + max + " an.");
            }
            Module module = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (column == preGradeColumn) {
                module.setPreGrade(value);
            } else if (column == weightPreliminaryGradeColumn) {
                module.setWeightPreliminaryGrade(value);
            } else if (column == examGradeColumn) {
                module.setExamGrade(value);
            }
            module.setCalculatedGrade(module.calculateModuleGrade());
            tableOverview.refresh();
            if (onEditCommitCallback != null) {
                onEditCommitCallback.run();
            }
        });
    }

    /**
     * Sets up the cell value factories for the table view.
     */
    private void setupCellValueFactories() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        shortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        moduleGroupColumn.setCellValueFactory(new PropertyValueFactory<>("moduleGroup"));
        preGradeColumn.setCellValueFactory(new PropertyValueFactory<>("preGrade"));
        weightPreliminaryGradeColumn.setCellValueFactory(new PropertyValueFactory<>("weightPreliminaryGrade"));
        examGradeColumn.setCellValueFactory(new PropertyValueFactory<>("examGrade"));
        calculatedGradeColumn
            .setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCalculatedGrade()));
    }

    private void showNotification(String message) {
        Notification msg = createNotification(message);
        setupNotificationAnimation(msg);
        scheduleAutoClose(msg);
        addNotificationToParent(msg);
    }

    private Notification createNotification(String message) {
        final var msg = new Notification(
            message,
            new FontIcon(MaterialDesignI.INFORMATION_OUTLINE)
        );
        msg.getStyleClass().addAll(
            Styles.ACCENT, Styles.ELEVATED_1
        );
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(msg, Pos.TOP_RIGHT);
        StackPane.setMargin(msg, new Insets(10, 10, 0, 0));
        return msg;
    }

    private void setupNotificationAnimation(Notification msg) {
        msg.setOnClose(e -> closeAnimation(msg));
        var in = Animations.slideInDown(msg, Duration.millis(250));
        in.playFromStart();
    }

    private void scheduleAutoClose(Notification msg) {
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> closeAnimation(msg));
        delay.play();
    }

    private void closeAnimation(Notification msg) {
        var out = Animations.slideOutUp(msg, Duration.millis(350));
        out.setOnFinished(f -> parentNode.getChildren().remove(msg));
        out.playFromStart();
    }

    private void addNotificationToParent(Notification msg) {
        if (!parentNode.getChildren().contains(msg)) {
            parentNode.getChildren().add(msg);
        }
    }

}
