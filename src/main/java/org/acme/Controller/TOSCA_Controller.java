package org.acme.Controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.Model.ConfigMap;
import org.acme.Model.Deployment;
import org.acme.Model.Secret;
import org.acme.Model.Service;
import org.acme.Service.TOSCA_Services;
import java.io.IOException;

@Path("/vnfd")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TOSCA_Controller {
    @Inject
    TOSCA_Services services;

    @POST
    @Path("/Deployment")
    public Deployment addDeployment() throws IOException {
        return services.addDeployment();
    }

    @PUT
    @Path("/Service")
    public Service addService() throws  IOException{
        return services.addService();
    }

    @PUT
    @Path("/ConfigMap")
    public ConfigMap addConfigMap() throws IOException{
        return services.addConfigMap();
    }

    @PUT
    @Path("/Secret")
    public Secret addSecret() throws IOException{
        return services.addSecret();
    }

    @POST
    public Uni<Void> addVnfd() throws IOException{
        return services.addVnfd();
  }
}
