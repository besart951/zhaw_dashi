package pm3.hs23.it22a_win.team1.dashboard.gradecalculator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GradeCalculatorJsonSerializer extends StdSerializer<GradeCalculatorData> {

    protected GradeCalculatorJsonSerializer() {
        this(null);
    }

    protected GradeCalculatorJsonSerializer(Class<GradeCalculatorData> t) {
        super(t);
    }

    @Override
    public void serialize(GradeCalculatorData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("@class", GradeCalculatorData.class.getCanonicalName());
        gen.writeObjectField("listOfSemesters", value.getListOfSemesters());
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(GradeCalculatorData value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }
}
