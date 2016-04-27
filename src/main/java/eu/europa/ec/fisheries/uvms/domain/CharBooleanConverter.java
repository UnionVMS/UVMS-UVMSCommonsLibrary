package eu.europa.ec.fisheries.uvms.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CharBooleanConverter implements AttributeConverter<Boolean, String> {

    private static final String Y = "Y";
    private static final String N = "N";

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (Boolean.TRUE.equals(attribute)) {
            return Y;
        } else {
            return N;
        }
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return Y.equals(dbData);
    }

}

