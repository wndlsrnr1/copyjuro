package juro.copyjuro.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import juro.copyjuro.repository.user.model.UserRole;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class EnumListConverter implements AttributeConverter<List<UserRole>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<UserRole> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<UserRole> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(dbData.split(SEPARATOR))
                .map(UserRole::valueOf)
                .collect(Collectors.toList());
    }
}
