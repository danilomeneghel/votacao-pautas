package app.enumerator.converter;

import app.enumerator.StatusSessaoEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class StatusSessaoEnumConverter implements AttributeConverter<StatusSessaoEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusSessaoEnum statusSessaoEnum) {
        return Objects.isNull(statusSessaoEnum) ? null : statusSessaoEnum.getValor();
    }

    @Override
    public StatusSessaoEnum convertToEntityAttribute(Integer valor) {
        return Objects.isNull(valor) ? null : StatusSessaoEnum.getValor(valor);
    }
}
