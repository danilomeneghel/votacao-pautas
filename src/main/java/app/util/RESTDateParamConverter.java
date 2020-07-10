package app.util;

import io.quarkus.launcher.shaded.com.google.inject.Inject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.util.Objects;

@Converter
public class RESTDateParamConverter implements AttributeConverter<RESTDateParam, String> {

    @Inject
    RESTDateParam restDateParam;

    @Override
    public String convertToDatabaseColumn(RESTDateParam restDateParam) {
        return Objects.isNull(restDateParam) ? null : restDateParam.getDateStr();
    }

    @Override
    public RESTDateParam convertToEntityAttribute(String data) {
        return Objects.isNull(data) ? null : restDateParam.getDateStr(data);
    }
}
