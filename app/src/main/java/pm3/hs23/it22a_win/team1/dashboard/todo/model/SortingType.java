package pm3.hs23.it22a_win.team1.dashboard.todo.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum defines different sorting types. It also links a caption
 * to the type as well as an active formulated expression to be
 * displayed for example as an column heading. The sorting type is also linked
 * to a comparator, which is defined in {@link Task}.
 * 
 * @author elmiglor
 * @version 2023-10-27
 */
public enum SortingType {
    //carefull this order is also order for sorting menu
    CREATION_DATE("Erstellungsdatum", "erstellt am", new CreationDateComparatorTask()),
    DUE_DATE("Fälligkeitsdatum", "fällig bis", new DueDateComparatorTask()),
    EXECUTION_DATE("Erledigungsdatum", "zu erledigen am", new ExecutionDateComparatorTask()),
    ALPHABETICAL("Alphabetisch", "erstellt am", new AlphaComparatorTask()),
    CUSTOM("Benutzerdefiniert", "erstellt am", new CustomComparatorTask());

    private final String caption;
    private final String inUseLabel;
    private final Comparator<Task> comparator;

    private static final Map<String, SortingType> lookUp = new HashMap<>();

    static {
        for (SortingType type : SortingType.values()) {
            lookUp.put(type.getCaption(), type);
        }
    }

    private SortingType(String caption, String inUseLabel, Comparator<Task> comparator) {
        this.caption = caption;
        this.inUseLabel = inUseLabel;
        this.comparator = comparator;
    }

    /**
     * Returns the caption which corresponds to the sorting type.
     * 
     * @return the caption of the sorting type
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Returns the active formulated expression which corresponds to the sorting
     * type.
     * 
     * @return the label to use as heading
     */
    public String getInUseLabel() {
        return inUseLabel;
    }

    /**
     * Returns a {@link Comparator} linked to the sorting type.
     * 
     * @return the comparator
     */
    public Comparator<Task> getComparator() {
        return comparator;
    }

    /**
     * Returns the {@link SortingType} for the given caption.
     * 
     * @param caption the caption of the wanted sorting type
     * @return the corresponding sorting type
     */
    public static SortingType getSortingType(String caption) {
        return lookUp.get(caption);
    }
}
