package org.cientopolis.samplers.persistence.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;


/**
 * Created by Xavier on 26/06/2021.
 * Serializer/Deserializer class for StepFragmentClass (contained in Step)
 * Solution adapted from {@link InterfaceAdapter}
 */

public class ClassAdapter implements JsonSerializer<Class>, JsonDeserializer<Class> {

    @Override
    public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Class returnClass;

        // Look up for the class with the name serialized
        try {
            String className = jsonElement.getAsString();
            returnClass =  Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // for debug
            returnClass = null; // no class found, null returned
        }

        return  returnClass;
    }

    @Override
    public JsonElement serialize(Class aClass, Type type, JsonSerializationContext jsonSerializationContext) {

        // Serialize only de class name as String
        return jsonSerializationContext.serialize(aClass.getName());
    }

}
