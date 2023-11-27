package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import pm3.hs23.it22a_win.team1.dashboard.WidgetData;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.IsObservable;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.IsObserver;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.UpdateEvent;

import java.util.*;

/**
 * The {@code GradeCalculatorData} class serves as the data model for the grade
 * calculator feature.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
@JsonSerialize(using = GradeCalculatorJsonSerializer.class)
@JsonDeserialize(using = GradCalculatorJsonDeserializer.class)
public class GradeCalculatorData implements ListChangeListener<Semester>, IsObserver, IsObservable, WidgetData {
    private final ListProperty<Semester> listOfSemesters;
    FactoryGradeHandler factoryGradeHandler;
    private final List<IsObserver> observers = new ArrayList<>();

    /**
     * Constructor for GradeCalculatorData.
     * Initializes the list of semesters and sets up a factory for grade handling.
     */
    public GradeCalculatorData() {
        listOfSemesters = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        listOfSemesters.addListener(this);
        this.factoryGradeHandler = new FactoryGradeHandler();
    }

    public GradeCalculatorData(List<Semester> semesters) {
        listOfSemesters = new SimpleListProperty<>(FXCollections.observableList(semesters));
        listOfSemesters.addListener(this);
        listOfSemesters.forEach(semester -> semester.addListener(this));
        this.factoryGradeHandler = new FactoryGradeHandler();
    }

    /**
     * Creates a new module in a specific semester.
     *
     * @param inSemester The semester in which the new module will be added.
     */
    public void createNewModuleInSemester(Semester inSemester) {
        for (Semester semester : listOfSemesters) {
            if (semester == inSemester) {
                semester.addModule(factoryGradeHandler.createModule());
            }
        }
    }

    /**
     * Creates and adds a new semester to the list of semesters.
     *
     * @param description A description for the new semester.
     */
    public void createSemester(String description) {
        Semester semester = factoryGradeHandler.createSemester(description);
        semester.addListener(this);
        listOfSemesters.add(semester);

    }

    /**
     * Gets the list of semesters.
     *
     * @return A ListProperty of Semester objects.
     */
    public ListProperty<Semester> getListOfSemesters() {
        return listOfSemesters;
    }


    /**
     * Removes a specified module from the semester it belongs to.
     *
     * @param module The module to be removed.
     * @throws NullPointerException if the module is null.
     */
    public void removeModule(Module module) {
        Objects.requireNonNull(module);

        for (Semester semester : listOfSemesters) {
            if (semester.getModules().contains(module)) {
                semester.removeModule(module);
            }
        }
    }

    /**
     * Retrieves all module group names from all semesters.
     *
     * @return A set of strings representing module group names.
     */
    public Set<String> getAllModules() {
        Set<String> moduleGroupName = new HashSet<>();
        for (Semester semester : listOfSemesters) {
            for (Module module : semester.getModules()) {
                moduleGroupName.add(String.valueOf(module.getModuleGroup()));
            }
        }
        return moduleGroupName;
    }

    /**
     * Retrieves the list of modules from a specific semester.
     *
     * @param getSemester The semester for which to retrieve the module list.
     * @return A list of Module objects.
     */
    public List<Module> getListOfModules(Semester getSemester) {
        for (Semester semester : listOfSemesters) {
            if (semester == getSemester) {
                return semester.getModules();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Calculates the average grade across a list of modules.
     *
     * @param modules An ObservableList of Module objects.
     * @return The average grade as a double.
     */
    public double calculateAverage(ObservableList<Module> modules) {
        if (modules.isEmpty()) {
            return 0.00;
        }

        double totalCredits = 0;
        double weightedSum = 0;

        for (Module module : modules) {
            double grade = module.getCalculatedGrade();
            int credits = module.getCredits();
            if (credits > 0 && grade > 0) {
                weightedSum += grade * credits;
                totalCredits += credits;
            }
        }
        return totalCredits > 0 ? weightedSum / totalCredits : 0.0;
    }

    /**
     * Removes a specific semester from the list of semesters.
     *
     * @param semester The semester to be removed.
     */
    public void removeSemester(Semester semester) {
        Objects.requireNonNull(semester);
        listOfSemesters.remove(semester);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(IsObserver observer) {
        this.observers.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(IsObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void informListener(UpdateEvent updateEvent) {
        for (IsObserver observer : observers) {
            //update methode von ??
            observer.update(updateEvent);
        }
    }

    // Semester -> gradcalcData.update
    @Override
    public void onChanged(Change<? extends Semester> c) {
        System.out.println("Semster added");
        informListener(UpdateEvent.UPDATE_LIST);
    }

    @Override
    public void update(UpdateEvent updateEvent) {
        informListener(UpdateEvent.UPDATE_LIST);
    }
}
