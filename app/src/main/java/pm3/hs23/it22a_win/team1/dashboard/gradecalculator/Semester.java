package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

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
public class Semester {
    private final String description;
    private final ListProperty<Module> modules;
    private double averageGrade;

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
    }

    /**
     * Adds a module to the semester.
     *
     * @param module the module to be added
     */
    public void addModule(Module module) {
        modules.add(module);
    }

    /**
     * Calculates and returns the average grade for the semester. The average is
     * weighted based on
     * the credits and grades of the modules in the semester.
     *
     * @return the weighted average grade for the semester
     */
    public double calculateSemesterGrade() {
        double totalCredits = 0;
        double weightedSum = 0;

        for (Module module : modules) {
            totalCredits += module.calculateModuleGrade() * module.getCredits();
            weightedSum += module.calculateModuleGrade() * module.getCredits() * module.calculateModuleGrade();
        }

        if (totalCredits == 0) {
            return 0.0;
        }
        averageGrade = weightedSum / totalCredits;
        return weightedSum / totalCredits;
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

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    /**
     * Removes a module from the semester.
     *
     * @param module the module to be removed
     */
    public void removeModule(Module module) {
        modules.remove(module);
    }

    /**
     * Validates that the specified value is not negative.
     *
     * @param value     the value to be validated
     * @param fieldName the name of the field to be included in the exception
     *                  message
     *
     * @throws IllegalArgumentException if the specified value is negative
     */
    private void validateNonNegative(Number value, String fieldName) {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
    }
}
