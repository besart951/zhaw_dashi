package pm3.hs23.it22a_win.team1.dashboard.todo.model;

/**
 * This class inherits its methods from {@link TaskList}. It has
 * proprietary sorting possiblities.
 * 
 * @author elmiglor
 * @version 2023-10-27
 */
public class DailyTaskList extends TaskList {
    private static final String DAILY_LIST = "| Tages-Liste |";

    /**
     * Instanciates a {@link DailyTaskList} and sets a reference to itself.
     */
    public DailyTaskList() {
        super(DAILY_LIST, null);
        this.dailyList = this;
    }

    /**
     * Adds a {@link Task} to this {@link DailyTaskList}.
     * 
     * @param task the task to be added
     * @return true if successfully added to task list
     */
    boolean addTask(Task task) {
        return allTasks.add(task);
    }

    /**
     * Returns whether this {@link DailyTaskList} contains the given {@link Task}.
     * 
     * @param task the task which should be tested
     * @return true if the task is in this daily task list
     */
    boolean containsTask(Task task) {
        return allTasks.contains(task);
    }

    /**
     * {@inheritDoc}
     * 
     * The method is similar to the inherited method, but uses a custom numeration
     * only valid for the daily list.
     */
    @Override
    public void moveTaskToPosition(Task draggedTask, int position) {
        // TODO WArning if already existing custom list
        if (currentSorting != SortingType.CUSTOM) {
            for (int i = 0; i < allTasks.size(); i++) {
                allTasks.get(i).setCustomDailyNr(i);
            }
            setCurrentSorting(SortingType.CUSTOM);
        }
        removeTask(draggedTask);
        getSortedList(currentSorting).add(position, draggedTask);
        for (int i = 0; i < allTasks.size(); i++) {
            allTasks.get(i).setCustomDailyNr(i);
        }
    }

}
