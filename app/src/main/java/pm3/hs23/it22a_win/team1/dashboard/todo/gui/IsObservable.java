package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

/**
 * This inteface defines the methods a ovservable class has to
 * implement.
 * 
 * @author elmiglor
 * @version 2023-10-27
 */
public interface IsObservable {

    /**
     * Adds an observer that listens for updates.
     * 
     * @param observer the observer which has implemented {@link IsObserver}
     */
    void addListener(IsObserver observer);

    /**
     * Removes an observer from the list.
     * 
     * @param observer the observer which should be removed
     */
    void removeListener(IsObserver observer);

    /**
     * Inform registered listener(s) about changes.
     * 
     * @param updateEvent the type of update {@link UpdateEvent}
     */
    void informListener(UpdateEvent updateEvent);
}
