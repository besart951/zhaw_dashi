package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pm3.hs23.it22a_win.team1.dashboard.todo.ToDoTestLoad;

/**
 * Launcher for widgets without main application for test porpuse only.
 * TODO remove later
 * 
 * @author elmiglor
 * @version 2023-10-27
 */
public class WidgetLoader extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String widget = "ToDoBig.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(widget));
        Parent loadScreen = (Parent) loader.load();
        ToDoTestLoad testLoad = new ToDoTestLoad();
        ((ToDoBigController) loader.getController())
                .setModel(new ToDoDecorator(testLoad.getTaskLists(), testLoad.getNameActiveTaskList()));

        Scene scene = new Scene(loadScreen);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test - ToDo");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResource("images/overdue_big.png").toExternalForm()));
        primaryStage.show();
        System.out.println(getClass().getResource("ToDoStyle.css"));
    }
}
