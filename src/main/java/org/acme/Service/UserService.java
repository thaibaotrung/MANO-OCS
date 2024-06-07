package org.acme.Service;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.Model.AuthenticationResponse;
import org.acme.Model.User;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@ApplicationScoped
public class UserService {

    public Uni<Void> CreateUser(User user){
        Document document = new Document()
                .append("username", user.getUsername())
                .append("password", user.getPassword());
        return getUserCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }

    public User findUserByName(@PathParam("name") String name) {
        return getUserCollection().find(new Document("username", name))
                .map(document -> {
                    User user = new User();
                    user.setUsername(document.getString("username"));
                    user.setPassword(document.getString("password"));
                    return user;
                })
                .collect()
                .first()
                .await().indefinitely();
    }
//    public Response findUserByName(@PathParam("name") String name) {
//        Uni<Document> uniDocument = getUserCollection().find(new Document("username", name)).first();
//
//        return uniDocument
//                .onItem().ifNotNull().transformToUni(document -> {
//                    User user = new User();
//                    user.setUsername(document.getString("username"));
//                    user.setPassword(document.getString("password"));
//                    return Uni.createFrom().item(Response.ok(user).build());
//                })
//                .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).build())
//                .await().indefinitely();
//    }
    @Inject JwtToken jwtToken;
    @ConfigProperty(name = "ACCESS_TOKEN_LIFE")
    long expiredIn;
//    public Uni<Response> login(@RequestBody String username){
//        User foundUser = (User) this.findUserByName(username);
//        String accessToken = jwtToken.generateToken(username);
//        System.out.println(accessToken);
//        Jsonb jsonb = JsonbBuilder.create();
//        String jsonResponse = jsonb.toJson(new AuthenticationResponse(expiredIn, accessToken));
//
//        return Uni.createFrom().item(Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build());
//    }

    public String generateJWT(String username) {
        User foundUser = this.findUserByName(username);
        return jwtToken.generateToken(username);
    }
//    public Uni<User> findUserByName(String name) {
//        return getUserCollection().find(new Document("name", name))
//                .map(document -> {
//                    User user = new User();
//                    user.setUsername(document.getString("username"));
//                    user.setPassword(document.getString("password"));
//                    return user;
//                }).collect().first();
//    }
    @Inject
    ReactiveMongoClient mongoClient;
    private ReactiveMongoCollection<Document> getUserCollection(){
        return mongoClient.getDatabase("test").getCollection("user");
    }
}
