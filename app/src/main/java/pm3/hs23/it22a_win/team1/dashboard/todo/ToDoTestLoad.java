package pm3.hs23.it22a_win.team1.dashboard.todo;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import pm3.hs23.it22a_win.team1.dashboard.todo.model.DailyTaskList;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.TaskList;

/**
 * This class is used to produce some data to display in the GUI.
 * TODO remove or use in tests
 * 
 * @author elmiglor
 * @version 2023-11-12
 */
public class ToDoTestLoad {
    private TaskList activeTaskList;
    private DailyTaskList dailyList;
    private Map<String, TaskList> taskLists;


    public ToDoTestLoad() {
        taskLists = new TreeMap<>();
        dailyList = new DailyTaskList();
        activeTaskList = new TaskList("test1", dailyList);
        taskLists.put("| Tages-Liste |", dailyList);
        taskLists.put("test1", activeTaskList);
        taskLists.put("sometest2", new TaskList("sometest2", dailyList));
        activeTaskList.addTask("some title", "this is something else", Optional.empty(), false, Optional.empty(), false, Optional.empty(), false, false);
        for(int i = 0;i <10000000;i++){}
        activeTaskList.addTask("some other title", "this is something else", Optional.empty(), false, Optional.empty(), false, Optional.empty(), false, false);
        for(int i = 0;i <1000000;i++){}
        activeTaskList.addTask("some very very long title", "this is something else", Optional.empty(), false, Optional.empty(), false, Optional.empty(), false, false);
        for(int i = 0;i <100000000;i++){}
        activeTaskList.addTask("some even longer but not so creative title to read", "this is something else", Optional.empty(), false, Optional.empty(), false, Optional.empty(), false, false);
    }

    public Map<String, TaskList> getTaskLists() {
        return taskLists;
    }

    public String getNameActiveTaskList() {
        return "test1";
    }

    public static String getColorScheme() {
        return "-my-base-color: aliceblue; -my-accent-color: lightblue; -my-focus-color: cornflowerblue;";
    }
    
}
