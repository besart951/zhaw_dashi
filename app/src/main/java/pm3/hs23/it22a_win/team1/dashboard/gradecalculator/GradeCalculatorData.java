package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.*;

/**
 * The {@code GradeCalculatorData} class serves as the data model for the grade
 * calculator feature.
 * It maintains a list of semesters and provides functionality to manipulate
 * this list, such as
 * adding and removing semesters or modules within them.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class GradeCalculatorData {
    private final ListProperty<Semester> listOfSemesters;
    FactoryGradeHandler factoryGradeHandler;

    /**
     * Constructs a {@code GradeCalculatorData} object with a default semester and
     * two modules.
     */
    public GradeCalculatorData() {
        listOfSemesters = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        this.factoryGradeHandler = new FactoryGradeHandler();
        // TODO: 09.11.2023 dummy to delete
        Semester semester = factoryGradeHandler.createSemester("Semester 1");
        semester.addModule(factoryGradeHandler.createModule());
        semester.addModule(factoryGradeHandler.createModule());
        listOfSemesters.add(semester);
    }

    /**
     * Creates a new module with dummy data in the specified semester.
     *
     * @param semesterDescription the semester in which the new module will be
     *                            created
     */
    public void createNewModuleInSemester(String semesterDescription) {
        for (Semester semester : listOfSemesters) {
            if (semester.getDescription().equals(semesterDescription)) {
                semester.addModule(factoryGradeHandler.createModule());
            }
        }
    }

    /**
     * Creates a new semester and adds it to the list of semesters.
     */
    public void createSemester(String description) {
        listOfSemesters.add(factoryGradeHandler.createSemester(description));
    }

    public ListProperty<Semester> getListOfSemesters() {
        return listOfSemesters;
    }

    /**
     * Removes the specified module from its semester.
     *
     * @param module the module to be removed
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
     * Removes the specified semester from the list of semesters.
     * If the semester contains modules, they will be removed as well.
     *
     * @return Set of all module
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
     * Removes the specified semester from the list of semesters.
     *
     * @param semesterDescription the semester to be removed
     *
     * @return List of all modules in the semester
     */
    public List<Module> getModulesInSemester(String semesterDescription) {
        for (Semester semester : listOfSemesters) {
            if (Objects.equals(semester.getDescription(), semesterDescription)) {
                return semester.getModules();
            }
        }
        return new ArrayList<>();
    }

}
