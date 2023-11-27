package pm3.hs23.it22a_win.team1.dashboard;

import javafx.scene.layout.Pane;

public interface WidgetController {
    void setModel(WidgetData model);

    WidgetModels getModelType();

    Pane loadModelNode(WidgetData widgetData);
}
