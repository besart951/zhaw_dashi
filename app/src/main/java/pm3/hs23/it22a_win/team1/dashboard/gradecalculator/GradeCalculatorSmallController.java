package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pm3.hs23.it22a_win.team1.dashboard.WidgetController;
import pm3.hs23.it22a_win.team1.dashboard.WidgetData;
import pm3.hs23.it22a_win.team1.dashboard.WidgetModels;

import java.io.IOException;

/**
 * The {@code GradeCalculatorSmallController} class is responsible for managing the compact user interface
 * for the grade calculator feature within the dashboard application. It handles the display of an overview
 * of semesters and their associated modules in a list view.
 *
 * @author Besart Morina
 * @version 02.11.2023
 */
public class GradeCalculatorSmallController implements WidgetController {

    @FXML
    private StackPane smallWidgetContent;
    @FXML
    private VBox vBoxContent;
    private TableView<Module> tableOverview;
    private GradeCalculatorData gradeCalculatorData;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * It sets up the formatting for the list view that displays the overview of semesters and modules.
     *
     * @param gradeCalculatorData the {@code GradeCalculatorData} object containing the data model
     */
    public void initialize(GradeCalculatorData gradeCalculatorData) {
        this.gradeCalculatorData = gradeCalculatorData;

        GradeCalcTableView gradeCalcTableView = new GradeCalcTableView(smallWidgetContent);
        gradeCalcTableView.showSmallView();
        this.tableOverview = gradeCalcTableView.getTableOverview();
        vBoxContent.getChildren().add(tableOverview);

        ObservableList<Module> allModules = FXCollections.observableArrayList();

        tableOverview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.gradeCalculatorData.getListOfSemesters().forEach(semester -> allModules.addAll(semester.getModules()));
        tableOverview.setItems(allModules);
        setupListeners();
    }

    /**
     * Sets the model objects used by the grade calculator. This method binds the UI components to the underlying
     * data model and initializes the view by populating the overview list.
     *
     * @param gradeCalculatorData the {@code GradeCalculatorData} object containing the data model
     */
    @Override
    public void setModel(WidgetData gradeCalculatorData) {
        initialize((GradeCalculatorData) gradeCalculatorData);
    }

    private void setupListeners() {
        ObservableList<Semester> semesters = gradeCalculatorData.getListOfSemesters();
        semesters.addListener(this::onSemesterListChanged);
        semesters.forEach(this::addModuleListener);
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
        semester.getModules().forEach(this::addModuleInTableOverview);
    }

    /**
     * Removes a semester from the grade calculator.
     *
     * @param semester The semester to remove.
     */
    private void removeSemester(Semester semester) {
        tableOverview.getItems().removeAll(semester.getModules());
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
                    for (Module addedModule : change.getAddedSubList()) {
                        addModuleInTableOverview(addedModule);
                    }
                }
            }
        });
    }

    private void addModuleInTableOverview(Module moduleToAdd) {
        tableOverview.getItems().add(moduleToAdd);
    }

    @Override
    public WidgetModels getModelType() {
        //TODO implement method
        return null;
    }

    /**
     * Loads the FXML file for the grade calculator feature.
     *
     * @param widgetData the {@code WidgetData} object containing the data model
     * @return the {@code Pane} object containing the grade calculator feature
     */
    @Override
    public Pane loadModelNode(WidgetData widgetData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/gradecalculator/GradeCalculatorSmall.fxml"));
            Pane root = fxmlLoader.load();

            GradeCalculatorSmallController controller = fxmlLoader.getController();
            controller.setModel(widgetData);
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
