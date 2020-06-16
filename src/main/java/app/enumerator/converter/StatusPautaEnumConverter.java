package app.enumerator.converter;

import app.enumerator.StatusPautaEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class StatusPautaEnumConverter implements AttributeConverter<StatusPautaEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusPautaEnum statusPautaEnum) {
        return Objects.isNull(statusPautaEnum) ? null : statusPautaEnum.getValor();
    }

    @Override
    public StatusPautaEnum convertToEntityAttribute(Integer valor) {
        return Objects.isNull(valor) ? null : StatusPautaEnum.getValor(valor);
    }
}
