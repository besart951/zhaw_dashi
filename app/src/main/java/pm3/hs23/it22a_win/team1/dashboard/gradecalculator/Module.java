package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;


/**
 * Represents a module within the grade calculator application. A module is a unit of study
 * that has a name, short name, credit value, preliminary grade, exam grade, and a weight
 * for the preliminary grade. The class provides a method to calculate the final grade of
 * the module based on its preliminary and exam grades and their respective weights.
 *
 * @author Besart Morina
 * @version 02.11.2023
 */
public class Module {
    private final SimpleStringProperty name;
    private final SimpleStringProperty shortName;
    private final SimpleIntegerProperty credits;
    private final SimpleDoubleProperty preGrade;
    private final SimpleDoubleProperty examGrade;
    private final SimpleDoubleProperty weightPreliminaryGrade;
    private final SimpleIntegerProperty moduleGroup;
    private final DoubleProperty calculatedGrade;

    /**
     * Constructs a new Module with the specified details.
     *
     * @param name                   the name of the module
     * @param shortName              the short name of the module
     * @param credits                the number of credits the module is worth
     * @param preliminaryGrade       the preliminary grade of the module
     * @param examGrade              the exam grade of the module
     * @param weightPreliminaryGrade the weight of the preliminary grade in the final grade calculation
     */
    public Module(String name, String shortName, int credits, double preliminaryGrade, double examGrade, double weightPreliminaryGrade) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(shortName);
        validateNonNegative(credits, "Credits");
        validateNonNegative(preliminaryGrade, "PreliminaryGrade");
        validateNonNegative(examGrade, "ExamGrade");
        validateNonNegative(weightPreliminaryGrade, "WeightPreliminaryGrade");

        this.name = new SimpleStringProperty(name);
        this.shortName = new SimpleStringProperty(shortName);
        this.credits = new SimpleIntegerProperty(credits);
        this.preGrade = new SimpleDoubleProperty(preliminaryGrade);
        this.examGrade = new SimpleDoubleProperty(examGrade);
        this.weightPreliminaryGrade = new SimpleDoubleProperty(weightPreliminaryGrade);
        this.moduleGroup = new SimpleIntegerProperty(0);
        this.calculatedGrade = new SimpleDoubleProperty(this.calculateModuleGrade());
    }

    /**
     * Constructs a new Module with the specified details.
     *
     * @param name                   the name of the module
     * @param shortName              the short name of the module
     * @param credits                the number of credits the module is worth
     * @param preliminaryGrade       the preliminary grade of the module
     * @param examGrade              the exam grade of the module
     * @param weightPreliminaryGrade the weight of the preliminary grade in the final grade calculation
     */
    public Module(String name, String shortName, int credits, double preliminaryGrade, double examGrade, double weightPreliminaryGrade, int moduleGroup) {
        Objects.requireNonNull(name, "Name");
        Objects.requireNonNull(shortName, "ShortName");
        validateNonNegative(credits, "Credits");
        validateNonNegative(preliminaryGrade, "PreliminaryGrade");
        validateNonNegative(examGrade, "ExamGrade");
        validateNonNegative(weightPreliminaryGrade, "WeightPreliminaryGrade");
        validateNonNegative(moduleGroup, "ModuleGroup");

        this.name = new SimpleStringProperty(name);
        this.shortName = new SimpleStringProperty(shortName);
        this.credits = new SimpleIntegerProperty(credits);
        this.preGrade = new SimpleDoubleProperty(preliminaryGrade);
        this.examGrade = new SimpleDoubleProperty(examGrade);
        this.weightPreliminaryGrade = new SimpleDoubleProperty(weightPreliminaryGrade);
        this.moduleGroup = new SimpleIntegerProperty(moduleGroup);
        this.calculatedGrade = new SimpleDoubleProperty(this.calculateModuleGrade());
    }

    public String getName() {
        return name.get();
    }

    public String getShortName() {
        return shortName.get();
    }

    public int getCredits() {
        return credits.get();
    }

    public double getPreGrade() {
        return preGrade.get();
    }

    public double getExamGrade() {
        return examGrade.get();
    }

    public double getWeightPreliminaryGrade() {
        return weightPreliminaryGrade.get();
    }
    public int getModuleGroup() {
        return moduleGroup.get();
    }
    public double getCalculatedGrade() {
        return calculatedGrade.get();
    }


    // Regular setters
    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name.set(name);
    }

    public void setShortName(String shortName) {
        Objects.requireNonNull(shortName);
        this.shortName.set(shortName);
    }

    public void setCredits(int credits) {
        validateNonNegative(credits, "Credits");
        this.credits.set(credits);
    }

    public void setPreGrade(double preliminaryGrade) {
        validateNonNegative(preliminaryGrade, "PreliminaryGrade");
        this.preGrade.set(preliminaryGrade);
    }

    public void setExamGrade(double examGrade) {
        this.examGrade.set(examGrade);
    }

    public void setWeightPreliminaryGrade(double weightPreliminaryGrade) {
        validateNonNegative(weightPreliminaryGrade, "Weight Preliminary Grade");
        this.weightPreliminaryGrade.set(weightPreliminaryGrade);
    }
    public void setModuleGroup(int moduleGroup){
        validateNonNegative(moduleGroup, "Module Group");
        this.moduleGroup.set(moduleGroup);
    }

    public void setCalculatedGrade(double value) {
        validateNonNegative(value, "Value");
        calculatedGrade.set(value);
    }

    /**
     * Calculates and returns the final grade for the module. The final grade is a weighted
     * average of the preliminary and exam grades based on the specified weight.
     *
     * @return the final calculated grade for the module
     */
    public double calculateModuleGrade() {
        double grade = (getPreGrade() * getWeightPreliminaryGrade() / 100) + (getExamGrade() * (1 - getWeightPreliminaryGrade() / 100));
        return Math.round(grade * 10) / 10.0;
    }

    private void validateNonNegative(Number value, String fieldName) {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
    }

}

