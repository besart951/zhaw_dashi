package pm3.hs23.it22a_win.team1.dashboard;

public enum WidgetType {

    TO_DO_BIG("/pm3/hs23/it22a_win/team1/dashboard/todo/gui/ToDoBig.fxml"),
    GRADE_CALCULATOR_SMALL("/pm3/hs23/it22a_win/team1/dashboard/gradecalculator/GradeCalculatorSmall.fxml");

    private final String fxmlPath;

    WidgetType(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    @Override
    public String toString() {
        return fxmlPath;
    }
}
