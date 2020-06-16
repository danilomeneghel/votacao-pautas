package app.enumerator.converter;

import app.enumerator.StatusUserEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class StatusUserEnumConverter implements AttributeConverter<StatusUserEnum, String> {

    @Override
    public String convertToDatabaseColumn(StatusUserEnum statusUserEnum) {
        return Objects.isNull(statusUserEnum) ? null : statusUserEnum.getValor();
    }

    @Override
    public StatusUserEnum convertToEntityAttribute(String valor) {
        return Objects.isNull(valor) ? null : StatusUserEnum.getValor(valor);
    }
}
