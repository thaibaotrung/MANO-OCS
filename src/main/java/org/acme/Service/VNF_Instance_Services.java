package org.acme.Service;


import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.Model.VNF_Instance;
import org.acme.Model.VNFc;
import org.bson.Document;
import io.fabric8.kubernetes.client.ConfigBuilder;

import java.util.*;

import io.fabric8.kubernetes.client.Config;
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

@ApplicationScoped
public class VNF_Instance_Services {

    private static final Logger logger = LoggerFactory.getLogger(VNF_Instance_Services.class);
    Config config = new ConfigBuilder()
            .withMasterUrl("https://127.0.0.1:53242")
            .withTrustCerts(true)
            .build();
    public Uni<Void> add_VNF_Instance(VNF_Instance vnf_instance){


            Document document1 = new Document()
                    .append("name", vnf_instance.getName())
                    .append("state", "NOT_IN_USE")
                    .append("description", vnf_instance.getDescription())
                    .append("vnfdId", vnf_instance.getVnfdId())
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

    @Inject
    ReactiveMongoClient mongoClient;
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnf");
    }
}
