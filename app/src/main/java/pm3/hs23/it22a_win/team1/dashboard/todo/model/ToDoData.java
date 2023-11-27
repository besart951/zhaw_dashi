package pm3.hs23.it22a_win.team1.dashboard.todo.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class handles {@link TaskList} on a top level. It is meant as an entry
 * point for a new to-do list data object.
 *
 * @author elmiglor
 * @version 2023-11-12
 */
public class ToDoData {
    protected String DAILY_LIST; // TODO const?
    protected Map<String, TaskList> taskLists;
    // private List<TaskList> taskLists; //TODO list or map?

    /**
     * Generates a new instance with a list of {@link TaskList}, initially
     * populated with the {@link DailyTaskList}.
     */
    public ToDoData() {
        taskLists = new TreeMap<>();
        DailyTaskList dailyTaskList = new DailyTaskList();
        DAILY_LIST = dailyTaskList.getNameList();
        taskLists.put(DAILY_LIST, dailyTaskList);
    }

    /**
     * Returns the unique names of all managed {@link TaskList}.
     *
     * @return
     */
    public List<String> getNamesAllLists() {
        List<String> allTaskLists = List.copyOf(taskLists.keySet());
        // List<String> allTaskLists = taskLists.values().stream().map(tl->
        // tl.getNameList()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        return allTaskLists;
    }

    protected Map<String, TaskList> getCopyOfTaskListsMap() {
        return Map.copyOf(taskLists);
    }

    /**
     * Returns the {@link TaskList} corresponding to the given list name.
     *
     * @param nameList the name of the {@link TaskList}
     * @return the corresponding {@link TaskList}
     */
    protected TaskList getListByName(String nameList) { // TODO remove if unused
        return taskLists.get(nameList);
    }

    /**
     * Adds a new {@link TaskList} to the list managed by the calling instance of
     * {@link ToDoData}. The name of the new list must be unique (not contained in
     * the managed list), max length of 30 characters and not blank nor null.
     * Leading and trailing spaces get removed.
     *
     * @param nameList the name of the new {@link TaskList}
     * @return true if list was successfuly created
     */
    protected boolean addNewList(String nameList) {
        if (nameList != null && !nameList.isBlank() && nameList.trim().length() < 30
                && !containsList(nameList.trim())) { // TODO
            // limit
            // necessary?
            // or limit
            // in
            // controller?
            taskLists.put(nameList.trim(), new TaskList(nameList.trim(), (DailyTaskList) taskLists.get(DAILY_LIST)));
            return true;
        }
        return false;
    }

    /**
     * Removes the corresponding {@link TaskList} for the given name.
     * The {@link DailyTaskList} can not be removed.
     *
     * @param nameList the name of the list which should be removed
     */
    protected void removeList(String nameList) { // TODO what happens to tasks, esp if on daily list?
        if (taskLists.containsKey(nameList) && !DAILY_LIST.equals(nameList)) {
            taskLists.remove(nameList);
        }
    }

    /**
     * Checks wheter the {@link TaskList} for the given name is managed by the
     * calling instance of {@link ToDoData}.
     *
     * @param nameList the name of the {@link TaskList} which should be checked
     * @return true if name is in list
     */
    public boolean containsList(String nameList) {
        return taskLists.containsKey(nameList);
    }

    /**
     * Adds a {@link Task} to the given {@link TaskList} and if successful removes
     * it from the {@link TaskList} initially containing the task.
     *
     * @param task     the task which should be relocated
     * @param fromList the task list where the task is currently located
     * @param toList   the task list where the task should be moved to
     * @return true if successfully relocated
     */
    protected boolean relocateTaskToOtherList(Task task, TaskList fromList, TaskList toList) {
        if (toList.addTask(task)) {
            return fromList.removeTask(task);
        }
        return false;
    }

}
