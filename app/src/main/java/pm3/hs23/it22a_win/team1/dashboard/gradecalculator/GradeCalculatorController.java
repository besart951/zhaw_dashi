package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.*;

/**
 * The {@code GradeCalculatorController} class is responsible for managing the
 * user interface
 * for the grade calculator feature within the dashboard application. It handles
 * the creation,
 * modification, and deletion of semesters and modules, as well as updating the
 * UI components
 * accordingly.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class GradeCalculatorController {

    @FXML private VBox vBoxContent;
    @FXML private ComboBox<String> comboSemester;
    @FXML private ComboBox<String> comboModulgruppe;

    @FXML private TableView<Module> tableOverview;
    @FXML private TableColumn<Module, String> nameColumn;
    @FXML private TableColumn<Module, String> shortNameColumn;
    @FXML private TableColumn<Module, Integer> creditsColumn;
    @FXML private TableColumn<Module, Integer> moduleGroupColumn;
    @FXML private TableColumn<Module, Double> preGradeColumn;
    @FXML private TableColumn<Module, Double> weightPreliminaryGradeColumn;
    @FXML private TableColumn<Module, Double> examGradeColumn;
    @FXML private TableColumn<Module, Double> calculatedGradeColumn;
    @FXML private Label averageGradeLabel;

    private Border defaultBorder;
    private GradeCalculatorData gradeCalculatorData;
    private final int MAX_SEMESTER = 12;
    private final int MAX_MODULES = 20;

    /**
     * Initializes the grade calculator controller with the given data.
     *
     * @param gradeCalculatorData
     */
    public void initialize(GradeCalculatorData gradeCalculatorData) {
        this.gradeCalculatorData = gradeCalculatorData;
        setupComponents();
        setupListeners();
    }

    /**
     * Sets the models for the grade calculator controller.
     *
     * @param gradeCalculatorData
     */
    public void setModels(GradeCalculatorData gradeCalculatorData) {
        initialize(gradeCalculatorData);
    }

    /**
     * Sets the models for the grade calculator controller.
     */
    private void setupComponents() {
        setupTable();
        List<String> nameOfSemesters = new ArrayList<>();
        ObservableList<Module> allModules = FXCollections.observableArrayList();

        gradeCalculatorData.getListOfSemesters().forEach(semester -> {
            nameOfSemesters.add(semester.getDescription());
            allModules.addAll(semester.getModules());
        });

        comboSemester.getItems().addAll(nameOfSemesters);
        tableOverview.setItems(allModules);
        defaultBorder = comboSemester.getBorder();
        tableOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        updateComboModuleGroup();
    }

    /**
     * Sets up the table view for the grade calculator.
     */
    private void setupTable() {
        setupCellFactories();
        setupCellValueFactories();
    }

    /**
     * Sets up the cell factories for the table view.
     */
    private void setupCellFactories() {
        setupStringColumn(nameColumn, 30);
        setupStringColumn(shortNameColumn, 5);
        setupIntegerColumn(creditsColumn, 0, 12);
        setupIntegerColumn(moduleGroupColumn, 0, 12);
        setupDoubleColumn(preGradeColumn, 0.00, 6.00);
        setupDoubleColumn(weightPreliminaryGradeColumn, 1, 100);
        setupDoubleColumn(examGradeColumn, 0.00, 6.00);
    }

    /**
     * Sets up the cell value factories for the table view.
     *
     * @param column    The column to set up the cell value factory for.
     * @param maxLength The maximum length of the string.
     */
    private void setupStringColumn(TableColumn<Module, String> column, int maxLength) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            String value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
            value = value != null && value.length() > maxLength ? value.substring(0, maxLength) : value;
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
            Integer value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
            value = value != null && value >= min && value <= max ? value : null;
            Module module = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (column == creditsColumn) {
                module.setCredits(value);
            } else if (column == moduleGroupColumn) {
                module.setModuleGroup(value);
            }
            tableOverview.refresh();
            updateAverageGradeLabel();
            updateComboModuleGroup();
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
            Double value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
            value = value != null && value >= min && value <= max ? value : null;
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
            updateAverageGradeLabel();
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

    /**
     * Applies the filter to the table view.
     *
     * @throws Exception If the module group is not a number.
     */
    @FXML
    public void filterApplyButton() throws Exception {
        String selectedSemesterDescription = comboSemester.getValue();
        Integer selectedModuleGroup = null;
        try {
            if (comboModulgruppe.getValue() != null) {
                selectedModuleGroup = Integer.parseInt(comboModulgruppe.getValue());
            }
        } catch (NumberFormatException e) {
            throw new Exception(e);
        }

        ObservableList<Module> filteredModules = FXCollections.observableArrayList();
        for (Semester semester : gradeCalculatorData.getListOfSemesters()) {
            if (selectedSemesterDescription == null || semester.getDescription().equals(selectedSemesterDescription)) {
                for (Module module : semester.getModules()) {
                    if (module.getModuleGroup() == selectedModuleGroup) {
                        filteredModules.add(module);
                    }
                }
            }
        }

        tableOverview.setItems(filteredModules);
    }

    /**
     * Resets the filter for the table view.
     */
    @FXML
    public void filterResetButton() {
        comboSemester.setValue(null);
        comboModulgruppe.setValue(null);

        ObservableList<Module> allModules = FXCollections.observableArrayList();
        for (Semester semester : gradeCalculatorData.getListOfSemesters()) {
            allModules.addAll(semester.getModules());
        }
        tableOverview.setItems(allModules);
    }

    /**
     * Adds a new module to the selected semester.
     */
    @FXML
    public void addModuleButton() {
        if (isSemesterSelected()) {
            if (gradeCalculatorData.getModulesInSemester(comboSemester.getValue()).size() < MAX_MODULES) {
                gradeCalculatorData.createNewModuleInSemester(comboSemester.getValue());
            }
        } else {
            showSemesterSelectionError();
        }
    }

    /**
     * Deletes the selected module from the selected semester.
     */
    @FXML
    public void deleteModuleButton() {
        List<Module> selectedModules = new ArrayList<>(tableOverview.getSelectionModel().getSelectedItems());
        selectedModules.forEach(gradeCalculatorData::removeModule);
        tableOverview.getItems().removeAll(selectedModules);
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
        comboSemester.getItems().add(semester.getDescription());
        semester.getModules().forEach(this::addModuleInTableOverview);
    }

    /**
     * Removes a semester from the grade calculator.
     *
     * @param semester The semester to remove.
     */
    private void removeSemester(Semester semester) {
        comboSemester.getItems().remove(semester.getDescription());
        tableOverview.getItems().removeAll(semester.getModules());
    }

    /**
     * Adds a module to the table view.
     *
     * @param moduleToAdd The module to add.
     */
    private void addModuleInTableOverview(Module moduleToAdd) {
        tableOverview.getItems().add(moduleToAdd);
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
                    for (Module addedModule : change.getAddedSubList()) {
                        addModuleInTableOverview(addedModule);
                    }
                }
                if (change.wasRemoved()) {
                    for (Module removedModule : change.getRemoved()) {
                        removeModuleInTableOverview(removedModule);
                    }
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
    }

    /**
     * Checks if a semester is selected.
     *
     * @return True if a semester is selected, false otherwise.
     */
    private boolean isSemesterSelected() {
        return comboSemester.getValue() != null && !comboSemester.getValue().trim().isEmpty();
    }

    /**
     * Shows an error if no semester is selected.
     *
     * @throws Exception If no semester is selected.
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
        if (modules.isEmpty()) {
            return 0.00;
        }

        double totalCredits = 0;
        double weightedSum = 0;

        for (Module module : modules) {
            double grade = module.getCalculatedGrade();
            int credits = module.getCredits(); // Assuming getCredits() returns an Integer
            if (credits > 0 && grade > 0) {
                weightedSum += grade * credits;
                totalCredits += credits;
            }
        }

        return totalCredits > 0 ? weightedSum / totalCredits : 0.0;
    }

    /**
     * Removes the selected semester from the combo box.
     */
    public void removeSemester() {
        Object selectedItem = comboSemester.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            comboSemester.getItems().remove(selectedItem);
        }
    }
}
