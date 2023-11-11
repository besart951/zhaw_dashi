package pm3.hs23.it22a_win.team1.dashboard.todo.gui;

/**
 * This intefaces defines what a observer has to impelment.
 * 
 * @author elmiglor
 * @version 2023-10-27
 */
public interface IsObserver {
    /**
     * This method is always called when an observed object changes.
     * 
     * @param updateEvent the type of update {@link UpdateEvent}
     */
    void update(UpdateEvent updateEvent);
}
