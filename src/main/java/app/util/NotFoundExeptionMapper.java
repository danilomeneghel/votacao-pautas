package app.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.common.io.Resources;

@Provider
public class NotFoundExeptionMapper implements ExceptionMapper<NotFoundException> {
    
    @Override
    public Response toResponse(NotFoundException exception) {
        URL url = Resources.getResource("templates/pageNotFound.html");
        String text = null;

		try {
			text = Resources.toString(url, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(text).build();
		}

        return Response.status(Status.NOT_FOUND).entity(text).build();
    }    
}