package app;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.enterprise.context.ApplicationScoped;

@Path("/")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class Application {
        
        @Inject
        Template index;
        
        @GET
        public TemplateInstance index() {
                return index.instance();
        }

}