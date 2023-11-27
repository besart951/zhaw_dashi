package pm3.hs23.it22a_win.team1.dashboard;

import javafx.scene.Node;

public class WidgetWrapper {

    public final WidgetType widgetType;

    public final Node widget;

    public WidgetWrapper(WidgetType widgetType, Node widget) {
        this.widgetType = widgetType;
        this.widget = widget;
    }
}
