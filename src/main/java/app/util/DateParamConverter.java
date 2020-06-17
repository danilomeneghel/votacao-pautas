package app.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParamConverter {

	public LocalDate dateToDb(LocalDate data) {
		return LocalDate.parse(data.toString(), DateTimeFormatter.ISO_DATE);
	}

	public String dateToString(LocalDate data) {
		return new SimpleDateFormat("dd-MM-yyyyy").format(data);
	}

}