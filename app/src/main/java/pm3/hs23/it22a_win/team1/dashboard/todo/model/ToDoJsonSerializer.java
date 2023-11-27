package pm3.hs23.it22a_win.team1.dashboard.todo.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator;

import java.io.IOException;
import java.util.Map;

public class ToDoJsonSerializer extends StdSerializer<ToDoDecorator> {

    protected ToDoJsonSerializer() {
        this(null);
    }

    protected ToDoJsonSerializer(Class<ToDoDecorator> t) {
        super(t);
    }

    @Override
    public void serialize(ToDoDecorator value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, TaskList> taskLists = value.getCopyOfTaskListsMap();
        gen.writeStartObject();
        gen.writeStringField("@class", ToDoDecorator.class.getCanonicalName());
        taskLists.forEach((listName, taskList) -> {
            try {
                gen.writeObjectField(listName, taskList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(ToDoDecorator value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }
}
