package org.acme.Controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.Model.VNF_Instance;
import org.acme.Service.VNF_Instance_Services;

import java.util.List;

@Path("/vnflcm/v1/vnf_instances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VNF_Instance_Controller {
    @Inject VNF_Instance_Services vnf_instance_services;

    @POST
    public Uni<Void> add_VNF_Instance(VNF_Instance vnf_instance){
        return vnf_instance_services.add_VNF_Instance(vnf_instance);
    }

    @GET
    @Path("/name/{name}")
    public Uni<List<VNF_Instance>> findVnfByName(@PathParam("name") String name){
        return vnf_instance_services.findVnfByName(name);
    }

    @GET
    @Path("/status/{status}")
    public Uni<List<VNF_Instance>> findVnfByStatus(@PathParam("status") String status){
        return vnf_instance_services.findVnfByStatus(status);
    }
}
