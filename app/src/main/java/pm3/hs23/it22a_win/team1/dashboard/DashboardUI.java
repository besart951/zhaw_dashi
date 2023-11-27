package pm3.hs23.it22a_win.team1.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pm3.hs23.it22a_win.team1.dashboard.calendar.CalendarData;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialPlannerData;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.IsObserver;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.UpdateEvent;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * GUI entry point for JavaFX.
 *
 * @author Besart Morina
 * @version 10.10.2023
 */
public class DashboardUI extends Application {
    private final FinancialPlannerData financialPlannerData;
    private final GradeCalculatorData gradeCalculatorData;

    private final CalendarData calendarData;

    private final ToDoDecorator todoData;

    private final Navigation navigation;
    private final ColorSettingsManager colorSettingsManager;

    private Stage stage;
    private static final Logger logger = Logger.getLogger(DashboardUI.class.getName());

    /**
     * Initialized the GUI entry class and initializes models.
     */
    public DashboardUI() {
        //storage = new PersistentDataStorage(songModel, playlistModel, moodboardModel, statisticModel);
        this.financialPlannerData = new FinancialPlannerData();
        this.gradeCalculatorData = new GradeCalculatorData();
        this.calendarData = new CalendarData();
        this.todoData = new ToDoDecorator();


        this.navigation = new Navigation();
        this.colorSettingsManager = new ColorSettingsManager(Theme.CUPERTINO_LIGHT);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        openMainWindow(primaryStage);
    }

    /**
     * Creates the main window of the application.
     *
     * @param stage main stage.
     * @throws IOException if main fxml could not have been loaded.
     */
    private void openMainWindow(Stage stage) throws IOException {

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/MainWindow.fxml"));

        // TODO: 10.10.2023 implement DataStorage
        // private final PersistentDataStorage storage;
        Scene scene = new Scene(mainLoader.load());
        //URL stylesheet = Objects.requireNonNull(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/style.css")); //remove this with atlantafx

       // scene.getStylesheets().add(stylesheet.toExternalForm());

        // configure and show stage
        stage.setScene(scene);
        stage.setTitle("PASL");
        stage.show();
        //stage.setMinWidth(1300);      //TODO remove or is this necessary?
        //stage.setMinHeight(700);

        String dashboardIcon = Objects.requireNonNull(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/icons/pasl-logo.png")).toExternalForm();
        stage.getIcons().add(new Image(dashboardIcon));


        // sets the Model of the mainController
        MainWindowController mainController = mainLoader.getController();
        JsonHelper jsonHelper = new JsonHelper();
        Map<Class, WidgetData> widgetModels = jsonHelper.loadObjectFromJsonFile(new TypeReference<>() {}, new File("src/main/resources/pm3/hs23/it22a_win/team1/dashboard/Data/DataModels.json"), false);
        addListenersToModels(widgetModels);

        mainController.setModels(financialPlannerData, (GradeCalculatorData) widgetModels.get(GradeCalculatorData.class), calendarData, (ToDoDecorator) widgetModels.get(ToDoDecorator.class), navigation, colorSettingsManager);

    }

    private void addListenersToModels(Map<Class, WidgetData> widgetModels) {
        ToDoDecorator toDoDecorator = (ToDoDecorator) widgetModels.get(ToDoDecorator.class);
        GradeCalculatorData calculatorData = (GradeCalculatorData) widgetModels.get(GradeCalculatorData.class);

        calculatorData.addListener(updateEvent -> {
            System.out.println("Bessi save data executed");
            JsonHelper jsonHelper = new JsonHelper();
            jsonHelper.saveObjectInJsonFile(widgetModels, new File("src/main/resources/pm3/hs23/it22a_win/team1/dashboard/Data/DataModels.json"));
        });

        toDoDecorator.addListener(updateEvent -> {
            JsonHelper jsonHelper = new JsonHelper();
            jsonHelper.saveObjectInJsonFile(widgetModels, new File("src/main/resources/pm3/hs23/it22a_win/team1/dashboard/Data/DataModels.json"));
        });
    }

}
