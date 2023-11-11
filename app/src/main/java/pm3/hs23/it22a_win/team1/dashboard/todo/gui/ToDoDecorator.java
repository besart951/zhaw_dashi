package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pm3.hs23.it22a_win.team1.dashboard.todo.model.DailyTaskList;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.SortingType;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.Task;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.TaskList;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.ToDoData;

/**
 * This class provides the GUI with access to the implementation of the
 * to-do model. It holds a map containing all existing instances of
 * {@link TaskList} created by an instance of this class.
 * <p>
 * As the {@link IsObservable} interface is implemented,
 * {@link IsObserver} objects can be registered.
 * 
 * @author elmiglor
 * @version 2023-10-27
 */
public class ToDoDecorator extends ToDoData implements IsObservable {
    private final List<IsObserver> listener = new ArrayList<>();
    private String nameSelectedList = DAILY_LIST;

    /**
     * Calls the constructor of {@link ToDoData}. //TODO prob remove if no loading
     * here
     */
    public ToDoDecorator() {
        super();
        // TODO load?
    }

    /**
     * Generates a new instace with the given list and the given selected list.
     * 
     * @param taskLists        a map of task lists with the names being the keys and
     *                         the
     *                         {@link TaskList} being the values.
     * @param nameSelectedList the name of the (previously) selected list. if not
     *                         included in the given map, the daily list will be
     *                         selected.
     */
    public ToDoDecorator(Map<String, TaskList> taskLists, String nameSelectedList) {
        super();
        boolean containsDailyList = false;
        for (TaskList taskList : taskLists.values()) {
            if (taskList instanceof DailyTaskList) {
                containsDailyList = true;
                DAILY_LIST = taskList.getNameList();
            }
        }
        if (containsDailyList) {
            this.taskLists = taskLists;
        } else {
            this.taskLists.putAll(taskLists);
        }
        this.nameSelectedList = this.taskLists.containsKey(nameSelectedList) ? nameSelectedList : DAILY_LIST;
    }

    private TaskList getSelectedList() {
        return nameSelectedList == null ? taskLists.get(getNamesAllLists().get(0)) : taskLists.get(nameSelectedList);
    }

    /**
     * Returns the name of the currently selected {@link TaskList}.
     * 
     * @return
     */
    String getNameSelectedList() {
        return nameSelectedList;
    }

    /**
     * Sets the given name as the currently selected {@link TaskList}.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with
     * {@link UpdateEvent#UPDATE_LIST}.
     * 
     * @param nameList the name of the list, which should be selected. Must be
     *                 contained in the map kept by this class.
     * @return true if list was contained
     */
    boolean setNameSelectedList(String nameList) {
        if (nameList != null && containsList(nameList)) {
            nameSelectedList = nameList;
            informListener(UpdateEvent.UPDATE_LIST);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If successful sets the name for the selected list to the given name and
     * triggers {@link #informListener(UpdateEvent)} with
     * {@link UpdateEvent#UPDATE_LIST}.
     */
    @Override
    protected boolean addNewList(String nameList) {
        boolean success = super.addNewList(nameList);
        if (success) {
            nameSelectedList = nameList.trim();
            informListener(UpdateEvent.UPDATE_LIST);
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeList(String nameList) {
        if (taskLists.containsKey(nameList) && !DAILY_LIST.equals(nameList)) {
            taskLists.remove(nameList);
            nameSelectedList = getNamesAllLists().get(0);
            informListener(UpdateEvent.UPDATE_LIST);
        }
    }

    // TODO who is responsable?,prob move to another class to reduce params
    void addTask(String title, String description, Optional<LocalDate> dueDate, boolean dueInCalendar,
            Optional<LocalDate> executionDate, boolean executionInCalendar, Optional<Period> repetitionInterval,
            boolean priority, boolean inDailyList) {
        getSelectedList().addTask(title, description, dueDate, dueInCalendar, executionDate, executionInCalendar,
                repetitionInterval, priority, inDailyList);
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    // TODO see above
    void modifyTask(Task task, String title, String description, Optional<LocalDate> dueDate, boolean dueInCalendar,
            Optional<LocalDate> executionDate, boolean executionInCalendar, Optional<Period> repetitionInterval,
            boolean priority, boolean inDailyList) {
        getSelectedList().modifyTask(task, title, description, dueDate, dueInCalendar, executionDate,
                executionInCalendar,
                repetitionInterval, priority, inDailyList);
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Removes a {@link Task} from the currently selected {@link TaskList}.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with
     * {@link UpdateEvent#UPDATE_TASKS}.
     * 
     * @param task the task which should be removed
     */
    void removeTask(Task task) {
        getSelectedList().removeTask(task);
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Returns all {@link Task} of the currently selected {@link TaskList}.
     * 
     * @return a list of tasks of the selected list
     */
    List<Task> getAllTasks() {
        return getSelectedList().getAllTasks();
    }

    /**
     * Returns the {@link SortingType} of the currently selected {@link TaskList}.
     * 
     * @return the sorting type of the selected list
     */
    SortingType getCurrentSorting() {
        return getSelectedList().getCurrentSorting();
    }

    /**
     * Sets the {@link SortingType} of the currently selected {@link TaskList} to
     * the given
     * type.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with an
     * {@link UpdateEvent#UPDATE_TASKS}.
     * 
     * @param sortingType the new sorting type for the selected list
     */
    void setCurrentSorting(SortingType sortingType) {
        getSelectedList().setCurrentSorting(sortingType);
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Moves the given {@link Task} to the given position of the currently selected
     * {@link TaskList}.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with an
     * {@link UpdateEvent#UPDATE_TASKS}.
     * 
     * @param task     the task which should be moved
     * @param position the new position of the task
     */
    void moveTaskToPosition(Task task, int position) {
        getSelectedList().moveTaskToPosition(task, position);
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Toggles the state of a {@link Task} whether it is included in the
     * {@link DailyTaskList}.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with an
     * {@link UpdateEvent#UPDATE_TASKS}.
     * 
     * @param task the task which should be toggled
     */
    void toggleTaskInDailyList(Task task) {
        getSelectedList().toggleTaskInDailyList(task);
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Toggles the state of a {@link Task} whether it is marked as done.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with an
     * {@link UpdateEvent#UPDATE_TASKS}.
     * 
     * @param task the task which should be toggled
     */
    void toggleDone(Task task) {
        task.toggleDone();
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Toggles the state of a {@link Task} whether it is set as priority.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with an
     * {@link UpdateEvent#UPDATE_TASKS}.
     * 
     * @param task the task which should be toggled
     */
    void togglePriority(Task task) {
        task.togglePriority();
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * Returns a optional containing a date depending on the given
     * {@link SortingType}.
     * 
     * @param task the task for which a date is needed
     * @param sortingType the sorting type for which a date is needed
     * @return optional of the asked date, can be <code>Optional.empty()</code>
     */
    public Optional<LocalDate> getNameForLblCurrentSorting(Task task, SortingType sortingType) {
        switch (sortingType) {
            case ALPHABETICAL:
            case CREATION_DATE:
            case CUSTOM:
                return task.getOptionalCreationDate();
            case DUE_DATE:
                return task.getDueDate();
            case EXECUTION_DATE:
                return task.getExecutionDate();
            default:
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(IsObserver observer) {
        listener.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(IsObserver observer) {
        listener.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void informListener(UpdateEvent updateEvent) {
        for (IsObserver observer : listener) {
            observer.update(updateEvent);
        }
    }

}
