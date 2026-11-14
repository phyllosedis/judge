package ru.phyllosedis.judgeapi.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.phyllosedis.judgeapi.security.ApiKeyProtected;
import ru.phyllosedis.judgeapi.service.ScriptService;
import ru.phyllosedis.judgedomain.dto.script.CreateScriptRequest;

@ApplicationScoped
@Path("/script")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApiKeyProtected
public class ScriptRestController {
    @Inject
    private ScriptService scriptService;

    @POST
    public Response create(CreateScriptRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(scriptService.create(request))
                .build();
    }

    @GET
    public Response list() {
        return Response.ok(scriptService.listMyScripts()).build();
    }
}
