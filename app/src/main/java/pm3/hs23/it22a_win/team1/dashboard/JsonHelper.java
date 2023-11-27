package pm3.hs23.it22a_win.team1.dashboard;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonHelper {

    public JsonHelper(){

    }

    public boolean saveObjectInJsonFile(Object object, File jsonFile) {
        try {
            jsonFile.createNewFile();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.writeValue(jsonFile, object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveObjectWithClassInformationInJsonFile(Object object, File jsonFile) {
        try {
            jsonFile.createNewFile();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.writeValue(jsonFile, object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> T loadObjectFromJsonFile(TypeReference<T> objectType, File jsonFile, boolean withClassInformation) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (withClassInformation) objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return objectMapper.readValue(jsonFile, objectType);
    }
}
