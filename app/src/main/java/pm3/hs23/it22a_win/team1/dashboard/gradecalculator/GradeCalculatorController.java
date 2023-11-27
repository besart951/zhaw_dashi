package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * The {@code GradeCalculatorController} class is responsible for managing the
 * user interface of the grade calculator feature.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class GradeCalculatorController {

    @FXML
    private StackPane viewWidgetContent;
    @FXML
    private ComboBox<Semester> comboSemester;
    @FXML
    private CheckComboBox<String> comboModulgruppe;
    @FXML
    private Label averageGradeLabel;
    @FXML
    private VBox vBoxContent;
    private TableView<Module> tableOverview;

    private Border defaultBorder;
    private GradeCalculatorData gradeCalculatorData;
    private final int MAX_SEMESTER = 12;
    private final int MAX_MODULES = 20;

    /**
     * Initializes the grade calculator controller with the given data.
     *
     * @param gradeCalculatorData the {@code GradeCalculatorData} object containing the data model
     */
    public void initialize(GradeCalculatorData gradeCalculatorData) {
        this.gradeCalculatorData = gradeCalculatorData;

        GradeCalcTableView gradeCalcTableView = new GradeCalcTableView(viewWidgetContent);
        gradeCalcTableView.showBigView();
        gradeCalcTableView.setOnEditCommitCallback(() -> {
            updateAverageGradeLabel();
            updateComboModuleGroup();
        });
        this.tableOverview = gradeCalcTableView.getTableOverview();
        vBoxContent.getChildren().add(tableOverview);
        VBox.setVgrow(tableOverview, Priority.ALWAYS);
        setupListeners();
        setupComponents();
    }

    /**
     * Sets the models for the grade calculator controller.
     *
     * @param gradeCalculatorData the {@code GradeCalculatorData} object containing the data model
     */
    public void setModel(GradeCalculatorData gradeCalculatorData) {
        initialize(gradeCalculatorData);
    }

    /**
     * Sets the models for the grade calculator controller.
     */
    private void setupComponents() {
        comboSemester.setConverter(new StringConverter<>() {
            @Override
            public String toString(Semester semester) {
                return semester == null ? null : semester.getDescription();
            }

            @Override
            public Semester fromString(String string) {
                return comboSemester.getItems().stream()
                    .filter(item -> item.getDescription().equals(string))
                    .findFirst()
                    .orElse(null);
            }
        });

        List<Semester> nameOfSemesters = new ArrayList<>();
        ObservableList<Module> allModules = FXCollections.observableArrayList();

        gradeCalculatorData.getListOfSemesters().forEach(semester -> {
            nameOfSemesters.add(semester);
            allModules.addAll(semester.getModules());
        });

        comboSemester.getItems().addAll(nameOfSemesters);


        tableOverview.setItems(allModules);
        defaultBorder = comboSemester.getBorder();
        tableOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        updateComboModuleGroup();
    }

    /**
     * Applies the filter to the table view.
     *
     * @throws ExecutionException If the module group is not a number.
     */
    @FXML
    public void filterApplyButton() throws ExecutionException {
        Semester selectedSemester = comboSemester.getValue();
        List<Integer> selectedModuleGroups = new ArrayList<>();

        // Retrieve the selected module group values
        for (String selected : comboModulgruppe.getCheckModel().getCheckedItems()) {
            try {
                selectedModuleGroups.add(Integer.parseInt(selected));
            } catch (NumberFormatException e) {
                throw new ExecutionException("Invalid module group number: " + selected, e);
            }
        }

        ObservableList<Module> filteredModules = FXCollections.observableArrayList();
        for (Semester semester : gradeCalculatorData.getListOfSemesters()) {
            if (selectedSemester == null || semester == selectedSemester) {
                for (Module module : semester.getModules()) {
                    // Check if the module group is in the selected groups or if no group is selected
                    if (selectedModuleGroups.isEmpty() || selectedModuleGroups.contains(module.getModuleGroup())) {
                        filteredModules.add(module);
                    }
                }
            }
        }

        tableOverview.setItems(filteredModules);
        updateAverageGradeLabel();
    }

    /**
     * Resets the filter for the table view.
     */
    @FXML
    public void filterResetButton() {
        comboSemester.setValue(null);
        comboModulgruppe.getCheckModel().clearChecks();

        ObservableList<Module> allModules = FXCollections.observableArrayList();
        for (Semester semester : gradeCalculatorData.getListOfSemesters()) {
            allModules.addAll(semester.getModules());
        }
        tableOverview.setItems(allModules);
        updateAverageGradeLabel();
    }

    /**
     * Adds a new module to the selected semester.
     */
    @FXML
    public void addModuleButton() {
        if (isSemesterSelected()) {
            if (gradeCalculatorData.getListOfModules(comboSemester.getValue()).size() < MAX_MODULES) {
                gradeCalculatorData.createNewModuleInSemester(comboSemester.getValue());
            }
        } else {
            showSemesterSelectionError();
        }
        updateAverageGradeLabel();
    }

    /**
     * Deletes the selected module from the selected semester.
     */
    @FXML
    public void deleteModuleButton() {
        List<Module> selectedModules = new ArrayList<>(tableOverview.getSelectionModel().getSelectedItems());
        selectedModules.forEach(gradeCalculatorData::removeModule);
        updateAverageGradeLabel();
    }

    /**
     * Adds a new semester to the grade calculator.
     */
    @FXML
    public void addNewSemester() {
        if (comboSemester.getItems().size() <= MAX_SEMESTER) {
            // Create a TextInputDialog
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Füge ein neues Semester hinzu");
            dialog.setHeaderText("Geben Sie ein Name für das Semester ein");
            dialog.setContentText("Name:");

            final int maxNameLength = 20;

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                if (name.length() <= maxNameLength) {
                    gradeCalculatorData.createSemester(name);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Der Name ist zu lang");
                    alert.setContentText("Der Name darf nicht länger als " + maxNameLength + " Zeichen sein.");
                    alert.showAndWait();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fehler- Maximum erreicht");
            alert.setHeaderText("Ein neues Semester kann nicht hinzugefügt werden");
            alert.setContentText("Sie haben die maximale Anzahl an Semestern erreicht.");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the selected semester from the grade calculator.
     */
    private void setupListeners() {
        ObservableList<Semester> semesters = gradeCalculatorData.getListOfSemesters();
        semesters.addListener(this::onSemesterListChanged);
        semesters.forEach(this::addModuleListener);
        tableOverview.getItems()
            .addListener((ListChangeListener.Change<? extends Module> change) -> updateAverageGradeLabel());
    }

    /**
     * Updates the combo box for the module group.
     */
    private void updateComboModuleGroup() {
        Set<String> moduleGroups = gradeCalculatorData.getAllModules();

        comboModulgruppe.getItems().setAll(FXCollections.observableArrayList(moduleGroups));
    }

    /**
     * Updates the combo box for the module group.
     *
     * @param change The change to the semester list.
     */
    private void onSemesterListChanged(ListChangeListener.Change<? extends Semester> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(this::addSemester);
            }
            if (change.wasRemoved()) {
                change.getRemoved().forEach(this::removeSemester);
            }
        }
    }

    /**
     * Adds a semester to the grade calculator.
     *
     * @param semester The semester to add.
     */
    private void addSemester(Semester semester) {
        addModuleListener(semester);
        comboSemester.getItems().add(semester);
        semester.getModules().forEach(this::addModuleInTableOverview);
        tableOverview.refresh();
    }

    /**
     * Removes a semester from the grade calculator.
     *
     * @param semester The semester to remove.
     */
    private void removeSemester(Semester semester) {
        Objects.requireNonNull(semester);
        comboSemester.setValue(null);
        comboSemester.getItems().remove(semester);
        tableOverview.refresh();
    }

    /**
     * Adds a module to the table view.
     *
     * @param moduleToAdd The module to add.
     */
    private void addModuleInTableOverview(Module moduleToAdd) {
        tableOverview.getItems().add(moduleToAdd);
        tableOverview.refresh();
    }

    /**
     * Adds a listener to the module list of a semester.
     *
     * @param semester The semester to add the listener to.
     */
    private void addModuleListener(Semester semester) {
        semester.getModules().addListener((ListChangeListener.Change<? extends Module> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::addModuleInTableOverview);
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(this::removeModuleInTableOverview);
                }
            }
        });
    }

    /**
     * Removes a module from the table view.
     *
     * @param moduleToRemove The module to remove.
     */
    private void removeModuleInTableOverview(Module moduleToRemove) {
        tableOverview.getItems().remove(moduleToRemove);
        tableOverview.refresh();
    }

    /**
     * Checks if a semester is selected.
     *
     * @return True if a semester is selected, false otherwise.
     */
    private boolean isSemesterSelected() {
        return comboSemester.getValue() != null;
    }

    /**
     * Shows an error if no semester is selected.
     */
    private void showSemesterSelectionError() {
        comboSemester.setBorder(new Border(new BorderStroke(Color.RED,
            BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY,
            BorderWidths.DEFAULT)));
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> comboSemester.setBorder(defaultBorder));
        pause.play();
    }

    /**
     * Updates the average grade label.
     */
    private void updateAverageGradeLabel() {
        double average = calculateAverageGrade();
        averageGradeLabel.setText(String.format("%.2f", average));
    }

    /**
     * Calculates the average grade.
     *
     * @return The average grade.
     */
    public double calculateAverageGrade() {
        ObservableList<Module> modules = tableOverview.getItems();
        return gradeCalculatorData.calculateAverage(modules);
    }

    /**
     * Removes the selected semester from the combo box.
     */
    @FXML
    private void removeSemesterButton() {
        if (isSemesterSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Bestätigung erforderlich");

            Image icon = new Image(getClass().getResourceAsStream("/pm3/hs23/it22a_win/team1/dashboard/icons/estimates.png"));
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(icon);

            alert.setHeaderText("Löschen des Semesters");
            alert.setContentText("Sind Sie sicher, dass Sie das Semester \nmit allen Modulen unwiderruflich löschen möchten?");

            ButtonType buttonTypeYes = new ButtonType("Ja, löschen");
            ButtonType buttonTypeNo = new ButtonType("Nein, abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                gradeCalculatorData.removeSemester(comboSemester.getValue());
            }
        } else {
            showSemesterSelectionError();
        }
    }

}
