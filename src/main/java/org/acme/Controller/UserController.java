package org.acme.Controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.Model.User;
import org.acme.Service.UserService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/vnflcm/v1/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;
    @POST
    @Path("/register")
    public Uni<Void> createUser(User user){
        return userService.CreateUser(user);
    }

//    @GET
//    @Path("/{name}")
//
//    public Uni<User> findUserByName(@PathParam("name") String name){
//        return userService.findUserByName(name);
//    }

    @POST
    @Path("/login")
    public String login(@RequestBody User user){
//        return userService.login(securityContext.getUserPrincipal().getName());
        return userService.generateJWT(user.getUsername());
    }


}
