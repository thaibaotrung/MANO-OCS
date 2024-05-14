package org.acme.Controller;


import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.Model.Instance;
import org.acme.Model.VNF_Instance;
import org.acme.Model.VNFc;
import org.acme.Service.LCM_Services;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@Path("/vnflcm/v1/vnf_instances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LCM_Controller {

    @Inject
    LCM_Services lcm_services;

    @POST
    @Path("/instantiate/{name}")
    public CompletableFuture<Void> Instantiate(@PathParam("name") String name) throws InterruptedException {
        return lcm_services.Instantiate(name);
    }

    @POST
    @Path("/terminate/{name}")
    public Uni<Void> Terminate(@PathParam("name") String name){
        return lcm_services.Terminate(name);
    }

    @POST
    @Path("/healing/{name}")
    public void Healing(@PathParam("name") String name){
        lcm_services.HealingVnf(name);
    }

    @GET
    @Path("/list")
    public Uni<List<VNF_Instance>> listVNF(){
        return lcm_services.listVNF();
    }

    @GET
    @Path("/listVnfc/{name}")
    public Uni<List<VNF_Instance>> listVNFc(@PathParam("name") String name){
        return lcm_services.listVNFc(name);
    }

    @POST
    @Path("/scale/{name}")
    public CompletableFuture<Void> scaleVNF(@PathParam("name") String name, VNFc vnFc, @QueryParam("vnfcName") String vnfcName) throws InterruptedException {
        return lcm_services.scaleVNF(name, vnFc, vnfcName);
    }

    @GET
    @Path("/listDeployment/{name}")
    public Uni<List<Instance>> listDeployment(@PathParam("name") String name) throws InterruptedException {
        return lcm_services.listDeployment(name);
    }

    @DELETE
    @Path("/delete/{name}")
    public Uni<Void> deleteVNF(String name){
        return lcm_services.deleteVNF(name);
    }
}
