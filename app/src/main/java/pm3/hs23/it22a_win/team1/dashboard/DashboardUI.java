package pm3.hs23.it22a_win.team1.dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pm3.hs23.it22a_win.team1.dashboard.calendar.CalendarModel;
import pm3.hs23.it22a_win.team1.dashboard.financialplanner.FinancialPlannerData;
import pm3.hs23.it22a_win.team1.dashboard.flashcard.FlashcardModel;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData;
import pm3.hs23.it22a_win.team1.dashboard.todo.TodoModel;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * GUI entry point for JavaFX.
 *
 * @author Besart Morina
 * @version 10.10.2023
 */
public class DashboardUI extends Application {
    // TODO: 10.10.2023 implement DataStorage
    // private final PersistentDataStorage storage;
    private final FinancialPlannerData financialPlannerData;
    private final GradeCalculatorData gradeCalculatorData;

    private final CalendarModel calendarModel;
    private final FlashcardModel flashcardModel;
    private final TodoModel todoModel;

    private final Navigation navigation;
    private static final Logger logger = Logger.getLogger(DashboardUI.class.getName());

    /**
     * Initialized the GUI entry class and initializes models.
     */
    public DashboardUI() {
        //storage = new PersistentDataStorage(songModel, playlistModel, moodboardModel, statisticModel);
        financialPlannerData = new FinancialPlannerData();
        gradeCalculatorData = new GradeCalculatorData();
        calendarModel = new CalendarModel();
        flashcardModel = new FlashcardModel();
        todoModel = new TodoModel();


        navigation = new Navigation();
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

        Scene scene = new Scene(mainLoader.load());
        URL stylesheet = Objects.requireNonNull(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/style.css"));

        scene.getStylesheets().add(stylesheet.toExternalForm());

        // configure and show stage
        stage.setScene(scene);
        stage.setTitle("PASL");
        stage.show();
        stage.setMinWidth(1300);
        stage.setMinHeight(700);

        String dashboardIcon = Objects.requireNonNull(getClass().getResource("/pm3/hs23/it22a_win/team1/dashboard/icons/pasl-logo.png")).toExternalForm();
        stage.getIcons().add(new Image(dashboardIcon));


        // sets the Model of the mainController
        MainWindowController mainController = mainLoader.getController();
        mainController.setModels(financialPlannerData, gradeCalculatorData, flashcardModel, todoModel, navigation);
    }
}
