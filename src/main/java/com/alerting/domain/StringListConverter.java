package com.alerting.domain;


import java.util.*;
import javax.persistence.AttributeConverter;

public class StringListConverter implements AttributeConverter<List<String>,String> {
    private static final String SPLIT_CHAR = ",";
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return strings != null ? String.join(SPLIT_CHAR, strings) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return s != null ? Arrays.asList(s.split(SPLIT_CHAR)) : new ArrayList<>();
    }
}
