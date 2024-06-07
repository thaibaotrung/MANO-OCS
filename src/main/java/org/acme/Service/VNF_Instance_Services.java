package org.acme.Service;


import com.mongodb.client.model.Filters;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import org.acme.Model.VNF_Instance;
import org.acme.Model.VNFc;
import org.bson.Document;
import io.fabric8.kubernetes.client.ConfigBuilder;

import java.util.*;

import io.fabric8.kubernetes.client.Config;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.function.DoubleBinaryOperator;

@ApplicationScoped
public class VNF_Instance_Services {

    private static final Logger logger = LoggerFactory.getLogger(VNF_Instance_Services.class);
    Config config = new ConfigBuilder()
            .withMasterUrl("https://127.0.0.1:58655")
            .withTrustCerts(true)
            .build();
    public Uni<Void> add_VNF_Instance(VNF_Instance vnf_instance){


            Document document1 = new Document()
                    .append("name", vnf_instance.getName())
                    .append("status", "STOPPED")
                    .append("description", vnf_instance.getDescription())
                    .append("vnfdName", vnf_instance.getVnfdName())
                    .append("vnfc", null);

            try(KubernetesClient client = new DefaultKubernetesClient(config)){
                Namespace namespace = new NamespaceBuilder()
                        .withNewMetadata()
                        .withName(vnf_instance.getName())
                        .endMetadata()
                        .build();
                 client.namespaces().resource(namespace).create();
            }

        return getCollection().insertOne(document1)
                .onItem().ignore().andContinueWithNull();


    }

    public Uni<List<VNF_Instance>> findVnfByName(@PathParam("name") String name){
        String regexPattern = ".*" + name + ".*"; // Tạo mẫu regex gần đúng
        Bson regexFilter = Filters.regex("name", regexPattern, "i"); // "i" để không phân biệt chữ hoa chữ thường
        return getCollection().find(regexFilter)
                .map(document -> {
                    VNF_Instance vnf_instance = new VNF_Instance();
                    vnf_instance.set_id(document.getObjectId("_id"));
                    vnf_instance.setName(document.getString("name"));
                    vnf_instance.setVnfdName(document.getString("vnfdName"));
                    vnf_instance.setStatus(document.getString("status"));
                    vnf_instance.setDescription(document.getString("description"));
                    return vnf_instance;
                }).collect().asList();
    }

//    public Uni<List<VNF_Instance>> findVnf(@QueryParam("name") String name, @QueryParam("status") String status){
//        return getCollection().find(new Document(""))
//    }

    public Uni<List<VNF_Instance>> findVnfByStatus(@PathParam("status") String status){
        return getCollection().find(new Document("status", status))
                .map(document -> {
                    VNF_Instance vnf_instance = new VNF_Instance();
                    vnf_instance.set_id(document.getObjectId("_id"));
                    vnf_instance.setName(document.getString("name"));
                    vnf_instance.setVnfdName(document.getString("vnfdName"));
                    vnf_instance.setStatus(document.getString("status"));
                    vnf_instance.setDescription(document.getString("description"));
                    return vnf_instance;
                }).collect().asList();
    }

    @Inject
    ReactiveMongoClient mongoClient;
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnf");
    }
}
