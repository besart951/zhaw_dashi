package pm3.hs23.it22a_win.team1.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import pm3.hs23.it22a_win.team1.dashboard.calendar.CalendarController;
import pm3.hs23.it22a_win.team1.dashboard.calendar.CalendarData;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialPlannerController;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialPlannerData;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorController;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData;
import pm3.hs23.it22a_win.team1.dashboard.todo.ToDoTestLoad;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoFullScreenController;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
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
    private CalendarController calendarController;
    private ToDoFullScreenController todoController;
    private DashboardController dashboardController;
    private ColorSettingsController colorSettingsController;
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


    public void setModels(FinancialPlannerData financialPlannerData, GradeCalculatorData gradeCalculatorData, CalendarData calendarData, ToDoDecorator todoData,
                          Navigation navigation, ColorSettingsManager colorSettingsManager) {
        Objects.requireNonNull(financialPlannerData);
        Objects.requireNonNull(gradeCalculatorData);
        Objects.requireNonNull(calendarData);
        Objects.requireNonNull(todoData);
        Objects.requireNonNull(navigation);

        //this.dashboardModel = dashboardModel;
        this.navigation = navigation;

        registerModelListeners();

        // set child controller models
        financialPlannerController.setModel(financialPlannerData);
        gradeCalculatorController.setModel(gradeCalculatorData);
        calendarController.setModel(calendarData);
        todoController.setModel(todoData);
        colorSettingsController.setModel(colorSettingsManager);


        Map<Class, WidgetData> widgetModels = new HashMap<>();
        widgetModels.put(ToDoDecorator.class, todoData);
        widgetModels.put(GradeCalculatorData.class, gradeCalculatorData);
        dashboardController.setWidgetModels(widgetModels);

        // display startup page // TODO: 10.10.2023 default mit DASHBOARD starten
        navigation.switchToPage(GRADECALCULATOR);
        //navigation.switchToPage(FINANCE);
        showDashboard();
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
    void initialize() {
        try {
            // Setup controllers
            loadAndSetController(FINANCE, "/pm3/hs23/it22a_win/team1/dashboard/financialplanner/FinancialPlanner.fxml");
            loadAndSetController(GRADECALCULATOR, "/pm3/hs23/it22a_win/team1/dashboard/gradecalculator/GradeCalculator.fxml");
            loadAndSetController(CALENDAR, "/pm3/hs23/it22a_win/team1/dashboard/calendar/Calendar.fxml");
            loadAndSetController(TODO, "/pm3/hs23/it22a_win/team1/dashboard/todo/gui/ToDoFullScreen.fxml");
            loadAndSetController(DASHBOARD, "/pm3/hs23/it22a_win/team1/dashboard/DashboardView.fxml");
            loadAndSetController(COLORSETTINGS, "/pm3/hs23/it22a_win/team1/dashboard/ColorSettings.fxml");

            // Setup event listeners


        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error starting up UI. {0}", ex.getMessage());
        }
    }

    @FXML
    void showDashboard() {
        navigation.switchToPage(DASHBOARD);
    }

    @FXML
    void showCalender() {
        navigation.switchToPage(CALENDAR);
    }

    @FXML
    void showTodo() {
        navigation.switchToPage(TODO);
    }

    @FXML
    void showSettings() {
        navigation.switchToPage(SETTINGS);
    }

    @FXML
    void showGradeCalculator() {
        navigation.switchToPage(GRADECALCULATOR);
    }

    @FXML
    void showFinancialPlanner() {
        navigation.switchToPage(FINANCE);
    }


    private void registerModelListeners() {
        navigation.getCurrentPageProperty().addListener((change, oldPage, newPage) -> {
            pageContainer.getChildren().clear();
            Pane newPagePane = pages.computeIfAbsent(newPage, this::loadPageWithFallback);
            pageContainer.getChildren().add(newPagePane);
        });
    }

    private Pane loadPageWithFallback(Navigation.Page page) {
        try {
            return loadPage(page, getViewSourceForPage(page));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error loading page " + page, ex);
            return new Pane(); // Return an empty pane as a fallback
        }
    }

    private String getViewSourceForPage(Navigation.Page page) {
        return switch (page) {
            case FINANCE -> "/pm3/hs23/it22a_win/team1/dashboard/financialplanner/FinancialPlanner.fxml";
            case GRADECALCULATOR -> "/pm3/hs23/it22a_win/team1/dashboard/gradecalculator/GradeCalculator.fxml";
            case CALENDAR -> "/pm3/hs23/it22a_win/team1/dashboard/calendar/Calendar.fxml";
            case TODO -> "/pm3/hs23/it22a_win/team1/dashboard/todo/gui/ToDoFullScreen.fxml";
            case DASHBOARD -> "/pm3/hs23/it22a_win/team1/dashboard/DashboardView.fxml";
            case COLORSETTINGS -> "/pm3/hs23/it22a_win/team1/dashboard/ColorSettings.fxml";
            // Add cases for other pages...
            default -> throw new IllegalArgumentException("Unknown page: " + page);
        };
    }

    private void loadAndSetController(Navigation.Page page, String fxmlPath) throws IOException {
        switch (page) {
            case DASHBOARD -> dashboardController = loadPage(page, fxmlPath);
            case CALENDAR -> calendarController = loadPage(page, fxmlPath);
            case FINANCE -> financialPlannerController = loadPage(page, fxmlPath);
            case GRADECALCULATOR -> gradeCalculatorController = loadPage(page, fxmlPath);
            case TODO -> todoController = loadPage(page, fxmlPath);
            case COLORSETTINGS -> colorSettingsController = loadPage(page, fxmlPath);
            default -> throw new IllegalArgumentException("Unknown page: " + page);
        }
    }

    @FXML
    void showThemeSettings() {
        navigation.switchToPage(COLORSETTINGS);
    }
}
