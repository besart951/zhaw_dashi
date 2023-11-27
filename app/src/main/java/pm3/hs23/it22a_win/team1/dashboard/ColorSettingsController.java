package pm3.hs23.it22a_win.team1.dashboard;

import javafx.fxml.FXML;

public class ColorSettingsController {

    private ColorSettingsManager colorSettingsManager;
    @FXML
    void applyCupertinoDark() {
        colorSettingsManager.setAppTheme(Theme.CUPERTINO_DARK);
    }
    @FXML
    void applyCupertinoLight() {
        colorSettingsManager.setAppTheme(Theme.CUPERTINO_LIGHT);
    }
    @FXML
    void applyDracula() {
        colorSettingsManager.setAppTheme(Theme.DRACULA);
    }
    @FXML
    void applyNordDark() {
        colorSettingsManager.setAppTheme(Theme.NORD_DARK);
    }
    @FXML
    void applyNordLight() {
        colorSettingsManager.setAppTheme(Theme.NORD_LIGHT);
    }
    @FXML
    void applyPrimerDark() {
        colorSettingsManager.setAppTheme(Theme.PRIMER_DARK);
    }

    @FXML
    void applyPrimerLight() {
        colorSettingsManager.setAppTheme(Theme.PRIMER_LIGHT);
    }

    public void setModel(ColorSettingsManager colorSettingsManager) {
        this.colorSettingsManager = colorSettingsManager;
    }



}
