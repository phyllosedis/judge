package ru.phyllosedis.judgepersistence.converter;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.phyllosedis.judgedomain.dto.script.ScriptParameter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Converter
public class ScriptParameterListConverter implements AttributeConverter<List<ScriptParameter>, Object> {

    private static final Jsonb JSONB = JsonbBuilder.create();
    private static final Type LIST_TYPE = new ArrayList<ScriptParameter>() {}.getClass().getGenericSuperclass();

    @Override
    public String convertToDatabaseColumn(List<ScriptParameter> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        return JSONB.toJson(attribute);
    }

    @Override
    public List<ScriptParameter> convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return Collections.emptyList();
        }
        String json = dbData.toString();
        return json.isBlank() ? Collections.emptyList() : JSONB.fromJson(json, LIST_TYPE);
    }
}
