package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.IsObservable;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.IsObserver;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.UpdateEvent;

import java.util.*;

/**
 * Represents a semester within the grade calculator application. It contains a
 * list of modules,
 * the semester's average grade, and a description. The class provides methods
 * to manage modules
 * and calculate the semester's average grade based on the grades and credits of
 * its modules.
 *
 * @author Besart Morina
 * @version 02.11.2023
 */
public class Semester implements IsObserver, IsObservable, ListChangeListener<Module> {
    private final String description;
    private final ListProperty<Module> modules;
    private final List<IsObserver> observers = new ArrayList<>();

    /**
     * Constructs a new Semester with a specific semester number and initializes the
     * description
     * based on that number.
     *
     * @param description the number of the semester
     */
    public Semester(String description) {
        this.description = description;
        this.modules = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        modules.addListener(this);
    }

    /**
     * Adds a module to the semester.
     *
     * @param module the module to be added
     */
    public void addModule(Module module) {
        Objects.requireNonNull(module);
        modules.add(module);
        module.addListener(this);
    }

    /**
     * Gets the description of the semester with counted number.
     *
     * @return the description of the semester
     */
    public String getDescription() {
        return description;
    }

    public ObservableList<Module> getModules() {
        return modules;
    }

    /**
     * Removes a module from the semester.
     *
     * @param module the module to be removed
     */
    public void removeModule(Module module) {
        Objects.requireNonNull(module);
        modules.remove(module);
        informListener(UpdateEvent.UPDATE_LIST);
    }

    // Semester -> GradeCalcData.update
    @Override
    public void onChanged(Change<? extends Module> c) {
        System.out.println("Module onChanged");
        informListener(UpdateEvent.UPDATE_LIST);
    }

    //kommt von Modules -> Semster.onChanged
    @Override
    public void update(UpdateEvent updateEvent) {
        informListener(UpdateEvent.UPDATE_LIST);
        System.out.println("updateSemester");
    }

    @Override
    public void informListener(UpdateEvent updateEvent) {
        for (IsObserver observer : observers) {
            observer.update(updateEvent);
        }
    }

    @Override
    public void addListener(IsObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeListener(IsObserver observer) {
        this.observers.remove(observer);
    }

}
