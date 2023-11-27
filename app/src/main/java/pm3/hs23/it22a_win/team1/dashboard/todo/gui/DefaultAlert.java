package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pm3.hs23.it22a_win.team1.dashboard.todo.ToDoTestLoad;

/**
 * 
 * @author elmiglor
 * @version 2023-11-12
 */
public class DefaultAlert {

    public static Alert getStyledAlert(String header, String content, Window owner) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(DefaultAlert.class.getResource("ToDoStyle.css").toExternalForm());
        alert.getDialogPane().setStyle(ToDoTestLoad.getColorScheme());
        alert.getDialogPane().getScene().setFill(Color.TRANSPARENT);
        alert.setHeaderText(header);
        alert.setContentText(content + "\n\n");
        ButtonType btnYes = new ButtonType("Ja");
        ButtonType btnCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnYes, btnCancel);
        ((Button)alert.getDialogPane().lookupButton(btnCancel)).setDefaultButton(true);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(owner);
        return alert;
    }
    
}
