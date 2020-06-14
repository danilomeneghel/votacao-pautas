package app.controller;

import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import app.model.Pauta;
import app.service.PautaService;

@Path("/pautas")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class PautaController {

    @Inject
    PautaService pautaService;

    @Inject
    Template error;
    
    @Inject
    Template pautaForm;
    
    @Inject
    Template pautaView;
    
    @Inject
    Template pautas;

    public List<String> status = Arrays.asList("Ativo", "Inativo");
    
    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listPautas(@QueryParam("filter") String filter) {
        return pautas.data("pautas", find(filter))
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    private List<Pauta> find(String filter) {
        Sort sort = Sort.ascending("titulo");

        if (filter != null && !filter.isEmpty()) {
            return Pauta.find("LOWER(titulo) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        }
        else {
            return Pauta.findAll(sort).list();
        }
    }

    @GET
    @Path("/{id}")
    public TemplateInstance getId(@PathParam("id") Long id) {
        Pauta pauta = pautaService.findPauta(id);
        if (pauta == null) {
            return error.data("error", "Pauta com id " + id + " não encontrado.");
        }
        
        return pautaView.data("pauta", pauta);
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
        return pautaForm.data("status", status);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    @Path("/new")
    public Response addPauta(@MultipartForm Pauta pauta) {
    	pautaService.insert(pauta);

    	return Response.seeOther(URI.create("/pautas")).build();
    }
    
    @GET
    @Path("/{id}/edit")
    public TemplateInstance updateForm(@PathParam("id") long id) {
        Pauta loaded = pautaService.findPauta(id);
        if (loaded == null) {
            return error.data("error", "Pauta com id " + id + " não encontrado.");
        }
		
        return pautaForm.data("pauta", loaded)
        	.data("status", status)
            .data("update", true);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    @Path("/{id}/edit")
    public Object updatePauta(@PathParam("id") long id, @MultipartForm Pauta pauta) {
    	Pauta loaded = pautaService.findPauta(id);
    	
        if (pauta == null) {
            return error.data("error", "Pauta com id " + id + " não encontrado.");
        }

        pautaService.update(loaded, pauta);

        return Response.seeOther(URI.create("/pautas")).build();
    }

    @POST
    @Transactional
    @Path("/{id}/delete")
    public Response deletePauta(@PathParam("id") long id) {
        Pauta.delete("id", id);

        return Response.seeOther(URI.create("/pautas")).build();
    }
}
