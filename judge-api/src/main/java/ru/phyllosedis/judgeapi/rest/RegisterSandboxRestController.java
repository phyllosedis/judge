package ru.phyllosedis.judgeapi.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.phyllosedis.judgeapi.security.ApiKeyProtected;
import ru.phyllosedis.judgeapi.service.SandboxProfileService;
import ru.phyllosedis.judgedomain.dto.sandbox.RegisterSandboxRequest;

@ApplicationScoped
@Path("/sandbox")
@ApiKeyProtected
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegisterSandboxRestController {

    @Inject
    private SandboxProfileService sandboxService;

    @POST
    @Path("/register")
    public Response register(RegisterSandboxRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(sandboxService.register(request))
                .build();
    }

    @GET
    public Response listMySandboxes() {
        return Response.ok(sandboxService.getCompanySandboxes()).build();
    }
}
