package org.acme.Controller;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import io.vertx.core.file.FileSystem;
import jakarta.inject.Inject;
import jakarta.resource.spi.ConfigProperty;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Model.ConfigMap;
import org.acme.Model.Deployment;
import org.acme.Model.Secret;
import org.acme.Model.Service;
import org.acme.Service.TOSCA_Services;
import org.jboss.resteasy.reactive.server.core.multipart.FormData;

import java.io.*;

@Path("/vnflcm/v1/vnf_instances")
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
    @Path("/vnfd")
    public Uni<Void> addVnfd() throws IOException{
        return services.addVnfd();
  }

    }
