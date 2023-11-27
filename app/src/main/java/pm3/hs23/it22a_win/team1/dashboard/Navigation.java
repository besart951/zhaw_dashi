package pm3.hs23.it22a_win.team1.dashboard;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Model responsible for tracking the page navigation.
 *
 * @author Besart Morina
 * @version 10.10.2023
 */
public class Navigation {
    private final ObjectProperty<Page> currentPage = new SimpleObjectProperty<>(Page.FINANCE);
    private static final Logger logger = Logger.getLogger(Navigation.class.getName());

    public ObjectProperty<Page> getCurrentPageProperty() {
        return currentPage;
    }

    /**
     * Switches to the passed {@link Page}.
     *
     * @param nextPage the {@link Page} to switch to, cannot be null.
     * @throws NullPointerException if the parameter is null.
     */
    public void switchToPage(Page nextPage) {
        Objects.requireNonNull(nextPage);
        logger.log(Level.FINE, "switching to page {0}", nextPage.getName());
        currentPage.set(nextPage);
    }

    public enum Page {
        DASHBOARD("Dashboard"),
        CALENDAR("Kalender"),
        FINANCE("Finanzplaner"),
        GRADECALCULATOR("Notenrechner"),
        TODO("ToDo"),
        SETTINGS("Einstellungen"),
        COLORSETTINGS("Farbeinstellung");

        private final String pageName;

        Page(String pageName) {
            Objects.requireNonNull(pageName);
            this.pageName = pageName;
        }

        public String getName() {
            return this.pageName;
        }
    }
}

