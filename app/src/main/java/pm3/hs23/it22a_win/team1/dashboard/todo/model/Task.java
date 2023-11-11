package pm3.hs23.it22a_win.team1.dashboard.todo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a task collected in a {@link TaskList}. It holds all
 * necessary information about the task.
 * 
 * @author elmiglor
 * @version 0.1, 2023.10.03
 */
public class Task {
    private String title;
    private String description;
    private LocalDate creationDate;
    private LocalTime creationTime; // TODO changes if modified?
    private Optional<LocalDate> dueDate = Optional.empty();
    private boolean dueInCalendar;
    private Optional<LocalDate> executionDate = Optional.empty();
    private boolean executionInCalendar;
    private Optional<Period> repetitionInterval;
    private boolean priority; // TODO change to int if more than 1 priority
    private boolean inDailyList;
    private boolean done;
    private int customNr;
    private int customDailyNr;
    private boolean generatedInDailyList;

    /**
     * Generates a new instance with the given title. The title can not be null or
     * blank. Sets the creation date and time. Leading and trailing spaces get
     * removed.
     * 
     * @param title
     */
    public Task(String title) {
        if (title == null || title.isBlank()) {
            throw new NullPointerException("Title was null or empty.");
        }
        this.title = title.trim();
        creationDate = LocalDate.now();
        creationTime = LocalTime.now();
    }

    /**
     * Modifies the task with the given parameters. Returns false if title is null
     * or blank.
     * 
     * @param title
     * @param description
     * @param dueDate
     * @param dueInCalendar
     * @param executionDate
     * @param executionInCalendar
     * @param repetitionInterval
     * @param priority
     * @return true if task was successfully modified
     */
    boolean modifyTask(String title, String description, Optional<LocalDate> dueDate, boolean dueInCalendar,
            Optional<LocalDate> executionDate, boolean executionInCalendar, Optional<Period> repetitionInterval,
            boolean priority) {

        if (title != null && !title.isBlank()) {
            this.title = title.trim();
            this.description = Objects.requireNonNull(description);
            Objects.requireNonNull(dueDate);
            if (dueDate.isEmpty()) {
                dueInCalendar = false;
            }
            Objects.requireNonNull(executionDate);
            if (executionDate.isEmpty()) {
                executionInCalendar = false;
            }
            this.dueDate = Objects.requireNonNull(dueDate);
            this.dueInCalendar = dueInCalendar;
            this.executionDate = executionDate;
            this.executionInCalendar = executionInCalendar;
            this.repetitionInterval = repetitionInterval;
            this.priority = priority;
            return true;
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    LocalDate getCreationDate() {
        return creationDate;
    }

    LocalTime getCreationTime() {
        return creationTime;
    }

    /**
     * Returns the creation date as an optional.
     * 
     * @return optional of creation date
     */
    public Optional<LocalDate> getOptionalCreationDate() {
        return Optional.of(creationDate);
    }

    /**
     * Returns the due date as an optional.
     * 
     * @return optional of due date, or <code>Optional.empty()</code> if due date is
     *         not set
     */
    public Optional<LocalDate> getDueDate() {
        return dueDate;
    }

    /**
     * Returns the execution date as an optional.
     * 
     * @return optional of execution date, or <code>Optional.empty()</code> if
     *         execution date is not set
     */
    public Optional<LocalDate> getExecutionDate() {
        return executionDate;
    }

    /**
     * Returns the repetition interval as an optional.
     * 
     * @return optional of due date, or <code>Optional.empty()</code> if due date is
     *         not set
     */
    public Optional<Period> getRepetitionInterval() {
        return repetitionInterval;
    }

    /**
     * Returns whether this priority is set.
     * 
     * @return true if this task has priority, otherwise false
     */
    public boolean isPriority() {
        return priority;
    }

    /**
     * Toggles the state of priority.
     */
    public void togglePriority() {
        priority = !priority;
    }

    /**
     * Returns whether the due date of this task is entered in the calendar.
     * 
     * @return true if due date is in calendar, otherwise false
     */
    public boolean isDueInCalendar() {
        return dueInCalendar;
    }

    /**
     * Returns whether the execution date of this task is entered in the calendar.
     * 
     * @return truef if execution date is in calendar, otherwise false
     */
    public boolean isExecutionInCalendar() {
        return executionInCalendar;
    }

    /**
     * Returns whether this task is marked as done.
     * 
     * @return true if task is done, otherwise false
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Toggles the state of isDone.
     * <p>
     * If a repetition interval is set, a new due date is set and the task is not
     * marked as done.
     */
    public void toggleDone() {
        if (!done) {
            if (repetitionInterval.isPresent()) {
                System.out.println(dueDate.get());
                dueDate = Optional.of(dueDate.get().plus(repetitionInterval.get()));
                System.out.println(dueDate.get());
            } else {
                done = true; // TODO remove or put down the list, ask user?
            }
        } else {
            done = false;
        }
    }

    /**
     * Sets the state whether this task is in the {@link DailyTaskList}. The
     * adding/removing to the {@link DailyTaskList} is executed by the
     * {@link TaskList} as it has knowledge of the {@link DailyTaskList}.
     * 
     * @param inDailyList the state whether this task has to be in daily list or not
     */
    void setInDailyList(boolean inDailyList) {
        this.inDailyList = inDailyList;
    }

    /**
     * Returns whether this task is in {@link DailyTaskList} or not.
     * 
     * @return true if is in daily list, otherwise false
     */
    public boolean isInDailyList() {
        return inDailyList;
    }

    void setCustomNr(int customNr) {
        this.customNr = customNr;
    }

    int getCustomNr() {
        return customNr;
    }

    void setCustomDailyNr(int customDailyNr) {
        this.customDailyNr = customDailyNr;
    }

    int getCustomDailyNr() {
        return customDailyNr;
    }

    void setGeneratedInDailyList(boolean generatedInDailyList) {
        this.generatedInDailyList = generatedInDailyList;
    }

    boolean isGeneratedInDailyList() {
        return generatedInDailyList;
    }
}

/**
 * Comparator for {@link Task} based on their state is done and is priority.
 */
class DoneUrgentComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = ((Boolean) t1.isDone()).compareTo(t2.isDone());
        if (c == 0) {
            c = -((Boolean) t1.isPriority()).compareTo(t2.isPriority());
        }
        return c;
    }
}

/**
 * Comparator for {@link Task} based on the {@link DoneUrgentComparatorTask} and the alphabetical order of the title.
 */
class AlphaComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = new DoneUrgentComparatorTask().compare(t1, t2);
        if (c == 0) {
            c = t1.getTitle().compareTo(t2.getTitle());
        }
        return c;
    }
}

/**
 * Comparator for {@link Task} based on the {@link DoneUrgentComparatorTask} and the creation date.
 */
class CreationDateComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = new DoneUrgentComparatorTask().compare(t1, t2);
        if (c == 0) {
            c = -t1.getCreationDate().compareTo(t2.getCreationDate());
        }
        if (c == 0) {
            c = -t1.getCreationTime().compareTo(t2.getCreationTime());
        }
        return c;
    }
}

/**
 * Comparator for {@link Task} based on the {@link DoneUrgentComparatorTask}, the due date and the creation date.
 */
class DueDateComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = new DoneUrgentComparatorTask().compare(t1, t2);
        if (c == 0) {
            c = ((Boolean) t1.getDueDate().isEmpty()).compareTo(t2.getDueDate().isEmpty());
        }
        if (c == 0 && t1.getDueDate().isPresent() && t2.getDueDate().isPresent()) {
            c = t1.getDueDate().get().compareTo(t2.getDueDate().get());
        }
        if (c == 0) {
            c = -new CreationDateComparatorTask().compare(t1, t2);
        }
        return c;
    }
}

/**
 * Comparator for {@link Task} based on the {@link DoneUrgentComparatorTask}, the execution date and the creation date.
 */
class ExecutionDateComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = new DoneUrgentComparatorTask().compare(t1, t2);
        if (c == 0) {
            c = ((Boolean) t1.getExecutionDate().isEmpty()).compareTo(t2.getExecutionDate().isEmpty());
        }
        if (c == 0 && t1.getExecutionDate().isPresent() && t2.getExecutionDate().isPresent()) {
            c = t1.getExecutionDate().get().compareTo(t2.getExecutionDate().get());
        }
        if (c == 0) {
            c = -new CreationDateComparatorTask().compare(t1, t2);
        }
        return c;
    }
}

/**
 * Comparator for {@link Task} based on the {@link DoneUrgentComparatorTask} and the custom nr.
 */
class CustomComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = new DoneUrgentComparatorTask().compare(t1, t2);
        if (c == 0) {
            c = ((Integer) t1.getCustomNr()).compareTo(t2.getCustomNr());
        }
        return c; // TODO
    }
}

/**
 * Comparator for {@link Task} based on the {@link DoneUrgentComparatorTask} and the custom dail nr.
 */
class CustomDailyComparatorTask implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int c = new DoneUrgentComparatorTask().compare(t1, t2);
        if (c == 0) {
            c = ((Integer) t1.getCustomDailyNr()).compareTo(t2.getCustomDailyNr());
        }
        return c; // TODO
    }
}