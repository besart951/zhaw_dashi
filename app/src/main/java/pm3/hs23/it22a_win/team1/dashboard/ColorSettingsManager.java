package pm3.hs23.it22a_win.team1.dashboard;

import javafx.application.Application;

import java.util.Properties;
import atlantafx.base.theme.*;

public class ColorSettingsManager {
    public static final String SETTINGS_FILE_NAME = "settings.properties";
    private final Properties settings = new Properties();

    /**
     * Loads the settings properties and creates the settings file if need be.
     */
    public ColorSettingsManager(Theme defaultTheme) {
        setAppTheme(defaultTheme);
    }

    public void setAppTheme(Theme theme) {
        switch (theme) {
            case CUPERTINO_DARK -> Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            case CUPERTINO_LIGHT -> Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
            case DRACULA -> Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
            case NORD_DARK -> Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            case NORD_LIGHT -> Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
            case PRIMER_LIGHT -> Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            case PRIMER_DARK -> Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            default -> throw new IllegalArgumentException("Unknown theme: " + theme);
        }
    }
}
