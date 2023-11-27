package pm3.hs23.it22a_win.team1.dashboard;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorSmallController;

import java.io.IOException;

public class TestWidgetController implements WidgetController{

    @Override
    public void setModel(WidgetData model) {

    }

    @Override
    public WidgetModels getModelType() {
        return null;
    }

    @Override
    public Pane loadModelNode(WidgetData widgetData) {
        return null;
    }
}
