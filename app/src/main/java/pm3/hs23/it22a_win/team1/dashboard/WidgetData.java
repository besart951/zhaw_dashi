package pm3.hs23.it22a_win.team1.dashboard;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = GradeCalculatorData.class, name = "pm3.hs23.it22a_win.team1.dashboard.gradecalculator.GradeCalculatorData"),
    @JsonSubTypes.Type(value = ToDoDecorator.class, name = "pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator")
})
public interface WidgetData {
}
