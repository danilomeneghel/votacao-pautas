package app.enumerator.converter;

import app.enumerator.VotoEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class VotoEnumConverter implements AttributeConverter<VotoEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(VotoEnum simNaoEnum) {
        return Objects.isNull(simNaoEnum) ? null : simNaoEnum.getValor();
    }

    @Override
    public VotoEnum convertToEntityAttribute(Integer valor) {
        return Objects.isNull(valor) ? null : VotoEnum.getValor(valor);
    }
}
