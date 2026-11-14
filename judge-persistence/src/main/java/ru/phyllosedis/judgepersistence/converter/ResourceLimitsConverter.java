package ru.phyllosedis.judgepersistence.converter;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.phyllosedis.judgedomain.limit.ResourceLimits;

@Converter
public class ResourceLimitsConverter implements AttributeConverter<ResourceLimits, Object> {
    private static final Jsonb JSONB = JsonbBuilder.create();

    @Override
    public String convertToDatabaseColumn(ResourceLimits attribute) {
        return attribute == null ? null : JSONB.toJson(attribute);
    }

    @Override
    public ResourceLimits convertToEntityAttribute(Object dbData) {
        return dbData == null ? null : JSONB.fromJson(dbData.toString(), ResourceLimits.class);
    }

}
