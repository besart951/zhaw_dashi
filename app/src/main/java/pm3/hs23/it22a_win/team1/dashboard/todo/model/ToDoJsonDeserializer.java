package pm3.hs23.it22a_win.team1.dashboard.todo.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import pm3.hs23.it22a_win.team1.dashboard.todo.gui.ToDoDecorator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class ToDoJsonDeserializer extends StdDeserializer<ToDoDecorator> {

    protected ToDoJsonDeserializer() {
        this(null);
    }

    protected ToDoJsonDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public ToDoDecorator deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Map<String, TaskList> taskListMap = new TreeMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        DailyTaskList dailyTaskList = new DailyTaskList();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (!DailyTaskList.DAILY_LIST.equals(entry.getKey())) {
                taskListMap.put(entry.getKey(), generateTaskList(entry.getValue(), dailyTaskList));
            }
        }
        taskListMap.put(DailyTaskList.DAILY_LIST, dailyTaskList);
        return new ToDoDecorator(taskListMap, DailyTaskList.DAILY_LIST);
    }

    private TaskList generateTaskList(JsonNode taskListNode, DailyTaskList dailyTaskList) {
        TaskList taskList = new TaskList(taskListNode.get("nameList").asText(), dailyTaskList);
        JsonNode allTasksNode = taskListNode.get("allTasks");

        allTasksNode.forEach(jsonTaskNode -> {
            Task task = generateTask(jsonTaskNode);
            taskList.addTask(task);
            if (jsonTaskNode.get("inDailyList").asBoolean()) taskList.toggleTaskInDailyList(task);
        });
        return taskList;
    }

    private Task generateTask(JsonNode jsonTaskNode) {
        Task task = new Task(jsonTaskNode.get("title").asText());
        task.modifyTask(
            jsonTaskNode.get("title").asText(),
            jsonTaskNode.get("description").asText(),
            Optional.ofNullable(generateLocalDate(jsonTaskNode.get("dueDate"))),
            jsonTaskNode.get("dueInCalendar").asBoolean(),
            Optional.ofNullable(generateLocalDate(jsonTaskNode.get("executionDate"))),
            jsonTaskNode.get("executionInCalendar").asBoolean(),
            Optional.ofNullable((jsonTaskNode.get("repetitionInterval").isNull()) ? null : Period.parse(jsonTaskNode.get("repetitionInterval").asText())),
            jsonTaskNode.get("priority").asBoolean()
        );
        task.setCreationDate(generateLocalDate(jsonTaskNode.get("optionalCreationDate")));
        if (jsonTaskNode.get("done").asBoolean()) task.toggleDone();

        return task;
    }

    private LocalDate generateLocalDate(JsonNode jsonLocalDateNode) {
        if (jsonLocalDateNode.isEmpty()) return null;

        return LocalDate.of(jsonLocalDateNode.get(0).asInt(), jsonLocalDateNode.get(1).asInt(), jsonLocalDateNode.get(2).asInt());
    }
}
