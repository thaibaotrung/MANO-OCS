package org.acme.Controller;


import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.Model.Instance;
import org.acme.Model.VNF_Instance;
import org.acme.Model.VNFc;
import org.acme.Model.Vnfd;
import org.acme.Service.LCM_Services;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@Path("/vnflcm/v1/vnf_instances")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LCM_Controller {

    @Inject
    LCM_Services lcm_services;

    @GET
    @Path("/instantiate/{name}")
    public CompletableFuture<Void> Instantiate(@PathParam("name") String name) throws InterruptedException {
        return lcm_services.Instantiate(name);
    }

    @GET
    @Path("/terminate/{name}")
    public CompletableFuture<Void> Terminate(@PathParam("name") String name){
        return lcm_services.Terminate(name);
    }

    @GET
    @Path("/healing/{name}")
    public CompletableFuture<Void> Healing(@PathParam("name") String name){
        return lcm_services.HealingVnf(name);
    }

    @GET
    @Path("/list")
    public Uni<List<VNF_Instance>> listVNF(@QueryParam("name") String name){
        return lcm_services.listVNF(name);
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

    @GET
    @Path("/listVnfd")
    public Uni<List<Vnfd>> listVnfd() throws InterruptedException{
        return lcm_services.listVNFD();
    }

    @GET
    @Path("/listLcmOpocc/{name}")
    public Uni<List<VNF_Instance>> listLCMOPOCC(@PathParam("name") String name){
        return lcm_services.listLCMOPOCC(name);
    }

    @DELETE
    @Path("/delete/{name}")
    public CompletableFuture<Void> deleteVNF(String name){
        return lcm_services.deleteVNF(name);
    }
}
