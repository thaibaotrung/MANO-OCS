package org.acme.Controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.Model.VNF_Instance;
import org.acme.Service.VNF_Instance_Services;

@Path("/vnflcm/v1/vnf_instances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VNF_Instance_Controller {
    @Inject VNF_Instance_Services vnf_instance_services;

    @POST
    public Uni<Void> add_VNF_Instance(VNF_Instance vnf_instance){
        return vnf_instance_services.add_VNF_Instance(vnf_instance);
    }
}
