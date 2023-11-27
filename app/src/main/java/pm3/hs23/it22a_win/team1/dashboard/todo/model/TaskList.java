package pm3.hs23.it22a_win.team1.dashboard.todo.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class handles all manipulations of a list of {@link Task}.
 * 
 * @author elmiglor
 * @version 2023-11-12
 */
public class TaskList {
    private String nameList;
    List<Task> allTasks;
    SortingType currentSorting = SortingType.CREATION_DATE;
    DailyTaskList dailyList;

    /**
     * This class constructor sets the given name and links a {@link DailyTaskList}
     * to this.
     * 
     * @param nameList
     * @param dailyList
     */
    public TaskList(String nameList, DailyTaskList dailyList) {
        this.nameList = nameList;
        this.dailyList = dailyList;
        allTasks = new LinkedList<>(); // TODO load list and customList, sorting
    }

    /**
     * Sets the given name for this.
     * <p>
     * Checking for valid name has to happen in {@link ToDoData} as this has no
     * knowledge if already another task list with this name exists.
     * 
     * @param nameList
     */
    void setNameList(String nameList) {
        this.nameList = nameList;
    }

    /**
     * Returns the name of this task list.
     * 
     * @return
     */
    public String getNameList() {
        return nameList;
    }

    /**
     * Generates a new {@link Task} with the given parameters and adds it to the
     * task list. Returns true if task was generated successfully.
     * 
     * @param title
     * @param description
     * @param dueDate
     * @param dueInCalendar
     * @param executionDate
     * @param executionInCalendar
     * @param repetitionInterval
     * @param priority
     * @param inDailyList
     * @return true if task was generated successfully
     */
    public Task addTask(String title, String description, Optional<LocalDate> dueDate, boolean dueInCalendar,
            Optional<LocalDate> executionDate, boolean executionInCalendar, Optional<Period> repetitionInterval,
            boolean priority, boolean inDailyList) {
        try {
            Task task = new Task(title);
            if (this.equals(this.dailyList)) {
                task.setGeneratedInDailyList(true); // TODO carefull if moving to other lists is possible this has to
                task.setInDailyList(true);                   // change
            }
            if (!modifyTask(task, title, description, dueDate, dueInCalendar, executionDate, executionInCalendar,
                    repetitionInterval, priority, inDailyList)) {
                return null;
            }
            if (!allTasks.add(task)) {
                return null;
            }
            // TODO set custom nr correctly
            SortingType sortingType = getCurrentSorting();
            moveTaskToPosition(task, 0);
            setCurrentSorting(sortingType);
            return task;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Adds a {@link Task} to this {@link TaskList}.
     * 
     * @param task the task to be added
     * @return true if successfully added to task list
     */
    public boolean addTask(Task task) {
        if (!allTasks.contains(task)) {
            return allTasks.add(task);
        }
        return false;
    }

    /**
     * Modifies the given {@link Task} with the given parameters. Most parameters
     * are validated by the method
     * {@link Task#modifyTask(String, String, Optional, boolean, Optional, boolean, Optional, boolean)}.
     * 
     * @param task
     * @param title
     * @param description
     * @param dueDate
     * @param dueInCalendar
     * @param executionDate
     * @param executionInCalendar
     * @param repetitionInterval
     * @param priority
     * @param inDailyList
     * @return
     */
    public boolean modifyTask(Task task, String title, String description, Optional<LocalDate> dueDate,
            boolean dueInCalendar,
            Optional<LocalDate> executionDate, boolean executionInCalendar, Optional<Period> repetitionInterval,
            boolean priority, boolean inDailyList) {
        boolean success = task.modifyTask(title, description, dueDate, dueInCalendar, executionDate,
                executionInCalendar, repetitionInterval, priority);
        if (success) {
            if (task.isInDailyList() != inDailyList) {
                toggleTaskInDailyList(task);
            }
        }
        return success;
    }

    /**
     * Removes the given {@link Task} from this task list.
     * 
     * @param task the task which should be removed
     * @return true if successfully removed
     */
    public boolean removeTask(Task task) {
        return allTasks.remove(task); // TODO
    }

    /**
     * Sets the current sorting to the given {@link SortingType}.
     * <p>
     * Also sorts this task list according to the given parameter.
     * 
     * @param sortingType
     */
    public void setCurrentSorting(SortingType sortingType) {
        currentSorting = sortingType;
        getSortedList(currentSorting);
    }

    /**
     * Returns the current {@link SortingType} of this task list.
     * 
     * @return the current {@link SortingType}
     */
    public SortingType getCurrentSorting() {
        return currentSorting;
    }

    /**
     * Moves the given {@link Task} to the given position.
     * <p>
     * If current sorting is not {@link SortingType#CUSTOM} the sorting is changed
     * to custom.
     * 
     * @param draggedTask
     * @param position
     */
    public void moveTaskToPosition(Task draggedTask, int position) {
        // TODO WArning if already existing custom list
        if (currentSorting != SortingType.CUSTOM) {
            for (int i = 0; i < allTasks.size(); i++) {
                allTasks.get(i).setCustomNr(i);
            }
            setCurrentSorting(SortingType.CUSTOM);
        }
        removeTask(draggedTask);
        getSortedList(currentSorting).add(position, draggedTask);
        for (int i = 0; i < allTasks.size(); i++) {
            allTasks.get(i).setCustomNr(i);
        }
    }

    List<Task> filter(int criteria) {
        return allTasks; // TODO
    }

    /**
     * Returns a list of {@link Task} of this task list, sorted by the given
     * {@link SortingType}.
     * <p>
     * For internall porpuses only, as it returns the list with the given sorting
     * and not a unmodifiable list. Otherwise use {@link #getAllTasks()}.
     * 
     * @param sortingType the sorting type for the list
     * @return the sorted list
     */
    List<Task> getSortedList(SortingType sortingType) {
        if (allTasks.size() >= 2) {
            if (this.equals(this.dailyList)) {
                Collections.sort(allTasks, new CustomDailyComparatorTask());
            } else {
                Collections.sort(allTasks, sortingType.getComparator());
            }
        }
        return allTasks; // TODO
    }

    /**
     * Toggles the state of a {@link Task} whether it is included in the
     * {@link DailyTaskList}.
     * Adds/removes the given {@link Task} to the {@link DailyTaskList}.
     * <p>
     * If a task was generated in the daily list, it's always true, as it would be
     * lost otherwise.
     * 
     * @param task the task which should be toggled
     * @return the state for the given task after manipulation
     */
    public boolean toggleTaskInDailyList(Task task) {
        if (task.isGeneratedInDailyList()) {
            task.setInDailyList(true);
        } else if (dailyList.containsTask(task)) {
            if (dailyList.removeTask(task)) {
                task.setInDailyList(false);
            }
        } else {
            if (dailyList.addTask(task)) {
                task.setInDailyList(true);
                SortingType sortingType = dailyList.getCurrentSorting();
                dailyList.moveTaskToPosition(task, 0);
                dailyList.setCurrentSorting(sortingType);
            }
        }
        return task.isInDailyList();
    }

    /**
     * Returns all {@link Task} of this task list sorted with the current
     * {@link SortingType}.
     * 
     * @return unmodifiable list of all tasks
     */
    public List<Task> getAllTasks() {
        return List.copyOf(getSortedList(currentSorting));
    }

}
