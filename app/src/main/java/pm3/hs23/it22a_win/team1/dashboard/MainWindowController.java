package pm3.hs23.it22a_win.team1.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialPlannerController;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialPlannerData;
import pm3.hs23.it22a_win.team1.dashboard.flashcard.FlashcardController;
import pm3.hs23.it22a_win.team1.dashboard.flashcard.FlashcardModel;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorController;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorSmallController;
import pm3.hs23.it22a_win.team1.dashboard.todo.TodoController;
import pm3.hs23.it22a_win.team1.dashboard.todo.TodoModel;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pm3.hs23.it22a_win.team1.dashboard.Navigation.Page.*;

/**
 * Controller for the main view which contains all the subpages.
 *
 * @author Besart Morina
 * @version 10.10.2023
 */
public class MainWindowController {

    @FXML
    private StackPane pageContainer;

    /**
     * sub-page controllers
     */
    private FinancialPlannerController financialPlannerController;
    private GradeCalculatorController gradeCalculatorController;
    private GradeCalculatorSmallController gradeCalculatorSmallController;
    private FlashcardController flashcardController;
    private TodoController todoController;
    private Navigation navigation;


    /**
     * sub-page panes
     */
    private final Map<Navigation.Page, Pane> pages = new EnumMap<>(Navigation.Page.class);

    private static final Logger logger = Logger.getLogger(MainWindowController.class.getName());

    /**
     * Constructs a new instance of the {@link MainWindowController}.
     *
     * @throws NullPointerException if one of the icon files are missing in the resources' folder.
     */
    public MainWindowController() {
    }


    public void setModels(FinancialPlannerData financialPlannerData, GradeCalculatorData gradeCalculatorData, FlashcardModel flashcardModel, TodoModel todoModel,
                          Navigation navigation) {
        Objects.requireNonNull(financialPlannerData);
        Objects.requireNonNull(gradeCalculatorData);
        Objects.requireNonNull(flashcardModel);
        Objects.requireNonNull(todoModel);
        Objects.requireNonNull(navigation);

        //this.dashboardModel = dashboardModel;
        this.navigation = navigation;

        registerModelListeners();

        // set child controller models
        financialPlannerController.setModels(financialPlannerData);
        gradeCalculatorController.setModels(gradeCalculatorData);
        gradeCalculatorSmallController.setModels(gradeCalculatorData);
        flashcardController.setModels(flashcardModel);
        todoController.setModels(todoModel);

        // display startup page // TODO: 10.10.2023 default mit DASHBOARD starten
        navigation.switchToPage(GRADECALCULATOR);
    }

    private <T> T loadPage(Navigation.Page page, String viewSource) throws IOException {
        Objects.requireNonNull(page);
        Objects.requireNonNull(viewSource);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewSource));
        Pane view = loader.load();
        pages.put(page, view);
        pageContainer.getChildren().add(view);
        return loader.getController();
    }

    @FXML
    private void initialize() {
        try {
            // Setup controllers
            financialPlannerController = loadPage(FINANCE, "/pm3/hs23/it22a_win/team1/dashboard/financialplanner/FinancialPlanner.fxml");
            gradeCalculatorController = loadPage(GRADECALCULATOR, "/pm3/hs23/it22a_win/team1/dashboard/gradecalculator/GradeCalculator.fxml");
            gradeCalculatorSmallController = loadPage(CALENDAR, "/pm3/hs23/it22a_win/team1/dashboard/gradecalculator/GradeCalculatorSmall.fxml");
            flashcardController = loadPage(FLASHCARD, "/pm3/hs23/it22a_win/team1/dashboard/flashcard/Flashcard.fxml");
            todoController = loadPage(TODO, "/pm3/hs23/it22a_win/team1/dashboard/todo/Todo.fxml");

            // Setup event listeners


        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error starting up UI. {0}", ex.getMessage());
        }
    }

    @FXML
    private void showDashboard() {
        navigation.switchToPage(DASHBOARD);
    }

    @FXML
    private void showCalender() {
        navigation.switchToPage(CALENDAR);
    }

    @FXML
    private void showTodo() {
        navigation.switchToPage(TODO);
    }

    @FXML
    private void showSettings() {
        navigation.switchToPage(SETTINGS);
    }

    @FXML
    private void showFlashcard() {
        navigation.switchToPage(FLASHCARD);
    }

    @FXML
    private void showGradeCalculator() {
        navigation.switchToPage(GRADECALCULATOR);
    }

    @FXML
    private void showFinancialPlanner() {
        navigation.switchToPage(FINANCE);
    }


    private void registerModelListeners() {
        navigation.getCurrentPageProperty().addListener((change, oldPage, newPage) ->
            pages.get(newPage).toFront());
    }
}
