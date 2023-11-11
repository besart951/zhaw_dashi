package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

/**
 * The {@code FactoryGradeHandler} class serves as a factory for creating dummy
 * data for the grade calculator feature.
 *
 * @author Besart Morina
 * @version 09.11.2023
 */
public class FactoryGradeHandler {

    private String nameDummy = "Modulname";
    private String shortNameDummy = "mdl";
    private int creditsDummy = 4;
    private double preliminaryGradeDummy = 0.0;
    private double examGradeDummy = 0.0;
    private double weightPremDummy = 20.0;

    public FactoryGradeHandler() {
    }

    /*
     * Creates a new module with dummy data.
     */
    public Module createModule() {
        return new Module(nameDummy + "1", shortNameDummy, creditsDummy, preliminaryGradeDummy, examGradeDummy,
                weightPremDummy);
    }

    /*
     * Creates a new semester with dummy data.
     */
    public Semester createSemester(String description) {
        return new Semester(description);
    }
}
