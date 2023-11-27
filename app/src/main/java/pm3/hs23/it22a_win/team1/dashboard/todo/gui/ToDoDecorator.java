package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pm3.hs23.it22a_win.team1.dashboard.WidgetData;
import pm3.hs23.it22a_win.team1.dashboard.todo.model.*;

/**
 * This class provides the GUI with access to the implementation of the
 * to-do backend using the decorator pattern. It holds a map containing all
 * existing instances of {@link TaskList} created by an instance of this class.
 * <p>
 * As the {@link IsObservable} interface is implemented,
 * {@link IsObserver} objects can be registered.
 *
 * @author elmiglor
 * @version 2023-11-12
 */
@JsonSerialize(using = ToDoJsonSerializer.class)
@JsonDeserialize(using = ToDoJsonDeserializer.class)
public class ToDoDecorator extends ToDoData implements IsObservable, WidgetData {
    private final List<IsObserver> listener = new ArrayList<>();
    private String nameSelectedList = DAILY_LIST;
    private Optional<Task> selectedTask = Optional.empty();

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
     * If successfull, also resets the selected {@link Task} to
     * <code>Optional.empty()</code>.
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
            selectedTask = Optional.empty();
            informListener(UpdateEvent.UPDATE_LIST);
            return true;
        }
        return false;
    }

    /**
     * Returns the currently selected {@link Task}.
     *
     * @return the currently selected task
     */
    Optional<Task> getSelectedTask() {
        return selectedTask;
    }

    /**
     * Sets the given {@link Task} as the currently selected task.
     * <p>
     * Triggers {@link #informListener(UpdateEvent)} with
     * {@link UpdateEvent#UPDATE_TASKS}.
     *
     * @param task the task which should be selected
     */
    void setSelectedTask(Optional<Task> task) {
        selectedTask = task;
        informListener(UpdateEvent.UPDATE_TASKS);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If successful, calls {@link #setNameSelectedList(String)} to set the name of
     * the selected list to the given name.
     * <p>
     * The {@link #setNameSelectedList(String)} triggers
     * {@link #informListener(UpdateEvent)} with
     * {@link UpdateEvent#UPDATE_LIST}.
     */
    @Override
    protected boolean addNewList(String nameList) {
        boolean success = super.addNewList(nameList);
        if (success) {
            success = setNameSelectedList(nameList.trim());
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
        Task task = getSelectedList().addTask(title, description, dueDate, dueInCalendar, executionDate,
                executionInCalendar,
                repetitionInterval, priority, inDailyList);
        if (task != null) {
            selectedTask = Optional.of(task);
            informListener(UpdateEvent.UPDATE_TASKS);
        }
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
        if (getSelectedList().removeTask(task)) {
            selectedTask = Optional.empty();
            System.out.println("removed some task");
            informListener(UpdateEvent.UPDATE_TASKS);
        }
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
     * Moves the given {@link Task} form the currently selected {@link TaskList} to
     * the {@link TaskList} given by the string-paramter.
     * If successful, calls {@link #setNameSelectedList(String)} to set the name of
     * the selected list to the given name.
     * <p>
     * The {@link #setNameSelectedList(String)} triggers
     * {@link #informListener(UpdateEvent)} with
     * {@link UpdateEvent#UPDATE_LIST}.
     *
     * @param task   the task which should be moved
     * @param toList the task list where the task should be moved to
     * @return true if successfull
     */
    boolean moveTaskToList(Task task, String toList) {
        if (relocateTaskToOtherList(task, getSelectedList(), getListByName(toList))) {
            return setNameSelectedList(toList);
        }
        return false;
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
        if (nameSelectedList.equals(DAILY_LIST) && !task.isInDailyList()) {
            selectedTask = Optional.empty();
        }
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
     * @param task        the task for which a date is needed
     * @param sortingType the sorting type for which a date is needed
     * @return optional of the asked date, can be <code>Optional.empty()</code>
     */
    Optional<LocalDate> getNameForLblCurrentSorting(Task task, SortingType sortingType) {
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
