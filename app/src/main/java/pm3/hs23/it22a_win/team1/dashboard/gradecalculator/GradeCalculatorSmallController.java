package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

/**
 * The {@code GradeCalculatorSmallController} class is responsible for managing the compact user interface
 * for the grade calculator feature within the dashboard application. It handles the display of an overview
 * of semesters and their associated modules in a list view.
 *
 * @author Besart Morina
 * @version 02.11.2023
 */
public class GradeCalculatorSmallController {

    @FXML
    private ListView<String> overviewList;

    private GradeCalculatorData gradeCalculatorData;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * It sets up the formatting for the list view that displays the overview of semesters and modules.
     */
    public void initialize() {
        listTextFormatter();
    }

    /**
     * Sets the model objects used by the grade calculator. This method binds the UI components to the underlying
     * data model and initializes the view by populating the overview list.
     *
     * @param gradeCalculatorData the {@code GradeCalculatorData} object containing the data model
     */
    public void setModels(GradeCalculatorData gradeCalculatorData) {
        this.gradeCalculatorData = gradeCalculatorData;
        setupListeners();
        populateOverview();
    }

    /**
     * Sets up listeners to observe changes in the list of semesters and their modules. It ensures that the
     * overview list is updated whenever the underlying data model changes.
     */
    private void setupListeners() {
        gradeCalculatorData.getListOfSemesters().addListener((ListChangeListener.Change<? extends Semester> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::addModuleListener);
                }
                populateOverview();
            }
        });

        gradeCalculatorData.getListOfSemesters().forEach(this::addModuleListener);
    }

    /**
     * Adds a listener to a specific semester's modules list to handle changes. This ensures that the overview
     * list is updated when modules are added or removed from the semester.
     *
     * @param semester the {@code Semester} to which the listener will be added
     */
    private void addModuleListener(Semester semester) {
        semester.getModules().addListener((ListChangeListener.Change<? extends Module> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    populateOverview();
                }
            }
        });
    }

    /**
     * Populates the overview list with a summary of each semester and its modules. The summary includes the
     * semester description and a list of module short names with their calculated grades.
     */
    private void populateOverview() {
        overviewList.getItems().setAll(
            gradeCalculatorData.getListOfSemesters().stream().map(semester -> {
                String semesterInfo = semester.getDescription() + ": ";
                String modulesInfo = semester.getModules().stream()
                    .map(module -> module.getShortName() + " - " + String.format("%.2f", module.calculateModuleGrade()))
                    .collect(Collectors.joining(", "));
                return semesterInfo + modulesInfo;
            }).collect(Collectors.toList())
        );
    }

    /**
     * Configures the list view's cell factory to format the text of each list item. This ensures that the text
     * in the list view is wrapped and displayed correctly.
     */
    private void listTextFormatter(){
        overviewList.setCellFactory(listView -> new ListCell<>() {
            private final Text text = new Text();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    text.wrappingWidthProperty().bind(listView.widthProperty().subtract(15)); // Subtract some padding
                    setGraphic(text);
                }
            }
        });
    }
}
