package app.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.WebApplicationException;

public class RESTDateParam {

	private final static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private Date data;	
	private String dateStr;

	public RESTDateParam(Date data, String dateStr) {
		this.data = data;
		this.dateStr = dateStr;
	}

	public RESTDateParam getDateStr(String dateStr) throws WebApplicationException {
		try {
			data = new Date( df.parse( dateStr ).getTime() );
			return (RESTDateParam) Stream.of(data).collect(Collectors.toList());
        } catch ( final ParseException ex ) {
            // Wrap up any expection as javax.ws.rs.WebApplicationException
            throw new WebApplicationException( ex );
        }
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

}
