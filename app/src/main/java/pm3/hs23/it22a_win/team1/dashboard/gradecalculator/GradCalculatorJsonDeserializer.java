package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GradCalculatorJsonDeserializer extends StdDeserializer<GradeCalculatorData> {

    protected GradCalculatorJsonDeserializer() {
        this(null);
    }

    protected GradCalculatorJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GradeCalculatorData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode semestersNode = (JsonNode) p.getCodec().readTree(p).get("listOfSemesters");
        List<Semester> semesters = new ArrayList<>();
        semestersNode.forEach(semesterNode -> semesters.add(generateSemester(semesterNode)));
        return new GradeCalculatorData(semesters);
    }

    private Semester generateSemester(JsonNode semesterNode) {
        Semester semester = new Semester(semesterNode.get("description").asText());
        semesterNode.get("modules").forEach(moduleNode -> semester.addModule(generateModule(moduleNode)));

        return semester;
    }

    private Module generateModule(JsonNode moduleNode) {
        Module module = new Module(
            moduleNode.get("name").asText(),
            moduleNode.get("shortName").asText(),
            moduleNode.get("credits").asInt(),
            moduleNode.get("preGrade").asDouble(),
            moduleNode.get("examGrade").asDouble(),
            moduleNode.get("weightPreliminaryGrade").asDouble(),
            moduleNode.get("moduleGroup").asInt()
        );
        module.setCalculatedGrade(moduleNode.get("calculatedGrade").asDouble());

        return module;
    }
}
