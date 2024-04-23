package org.acme.Service;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import org.acme.Model.VNF_Instance;
import org.acme.Model.VNFc;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.concurrent.TimeUnit;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LCM_Services {
    Config config = new ConfigBuilder()
            .withMasterUrl("https://127.0.0.1:58209")
            .withTrustCerts(true)
            .build();


    public Uni<Void> Instantiate(@PathParam("name") String name) throws InterruptedException {

        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            String secretYamlPath = "D:\\20232\\TOSCA\\k8s_resources\\secret.yaml";
            byte[] secretYamlBytes = Files.readAllBytes(Paths.get(secretYamlPath));
            String secretYamlContent = new String(secretYamlBytes);

            Secret secret = Serialization.unmarshal(secretYamlContent, Secret.class);
            client.secrets().inNamespace(name).create(secret);

            String configMapYamlPath = "D:\\20232\\TOSCA\\k8s_resources\\configMap.yaml";
            byte[] configMapYamlBytes = Files.readAllBytes(Paths.get(configMapYamlPath));
            String configMapYamlContent = new String(configMapYamlBytes);

            ConfigMap configMap = Serialization.unmarshal(configMapYamlContent, ConfigMap.class);
            client.configMaps().inNamespace(name).create(configMap);

            String MongodeploymentYamlPath = "D:\\20232\\TOSCA\\k8s_resources\\mongo_deployment.yaml";
            byte[] MongodeploymentYamlBytes = Files.readAllBytes(Paths.get(MongodeploymentYamlPath));
            String MongodeploymentYamlContent = new String(MongodeploymentYamlBytes);

            Deployment Mongodeployment = Serialization.unmarshal(MongodeploymentYamlContent, Deployment.class);
            client.apps().deployments().inNamespace(name).create(Mongodeployment);

            String WebdeploymentYamlPath = "D:\\20232\\TOSCA\\k8s_resources\\web_deployment.yaml";
            byte[] WebdeploymentYamlBytes = Files.readAllBytes(Paths.get(WebdeploymentYamlPath));
            String WebdeploymentYamlContent = new String(WebdeploymentYamlBytes);

            Deployment Webdeployment = Serialization.unmarshal(WebdeploymentYamlContent, Deployment.class);
            client.apps().deployments().inNamespace(name).create(Webdeployment);


            String MongoServiceYamlPath = "D:\\20232\\TOSCA\\k8s_resources\\mongo_service.yaml";
            byte[] MongoServiceYamlBytes = Files.readAllBytes(Paths.get(MongoServiceYamlPath));
            String MongoServiceYamlContent = new String(MongoServiceYamlBytes);

            Service MongoService = Serialization.unmarshal(MongoServiceYamlContent, Service.class);
            client.services().inNamespace(name).create(MongoService);


            String WebServiceYamlPath = "D:\\20232\\TOSCA\\k8s_resources\\web_service.yaml";
            byte[] WebServiceYamlBytes = Files.readAllBytes(Paths.get(WebServiceYamlPath));
            String WebServiceYamlContent = new String(WebServiceYamlBytes);

            Service WebService = Serialization.unmarshal(WebServiceYamlContent, Service.class);
            client.services().inNamespace(name).create(WebService);


            System.out.println("Đã bật VNF thành công");

            TimeUnit.SECONDS.sleep(10);

            PodList podList = client.pods().inNamespace(name).list();

            List<VNFc> listVNFc = new ArrayList<>();
            // In ra tên của các Pod
            for (Pod pod : podList.getItems()) {
                String ip = pod.getStatus().getPodIP();
                String state = pod.getStatus().getPhase();
                String uid = pod.getMetadata().getUid();
                String podName = pod.getMetadata().getName();
                String nodeName = pod.getSpec().getNodeName();

                VNFc vnfc = new VNFc();
                vnfc.setId(uid);
                vnfc.setName(podName);
                vnfc.setIp(ip);
                vnfc.setNodeName(nodeName);
                vnfc.setState(state);

                listVNFc.add(vnfc);
            }


            Document query = new Document("name", name);
//            Document update = new Document("$set", new Document("vnfc", listVNFc));
//            Document update1 = new Document("$set", new Document("state", "IN_USE"));
            Document update = new Document("$set", new Document("vnfc", listVNFc).append("state", "IN_USE"));
            return getCollection().updateMany(query, update)
                    .onItem().ignore().andContinueWithNull();
//             getCollection().updateOne(query, update1)
//                    .onItem().ignore().andContinueWithNull();
//             return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        return null;
    }

    public Uni<Void> Terminate(@PathParam("name") String name){
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            client.apps().deployments().inNamespace(name).withName("mongo-deployment").delete();
            client.apps().deployments().inNamespace(name).withName("webapp-deployment").delete();
            client.services().inNamespace(name).withName("mongo-service").delete();
            client.services().inNamespace(name).withName("webapp-service").delete();
            client.secrets().inNamespace(name).withName("mongo-secret").delete();
            client.configMaps().inNamespace(name).withName("mongo-config").delete();
            System.out.println("Pod đã được tắt.");
        }
        Document query = new Document("name", name);
//        Document update = new Document("$set", new Document("vnfc", null));
//        Document update1 = new Document("$set", new Document("state", "NOT_IN_USE"));
        Document update = new Document("$set", new Document("vnfc", null).append("state", "NOT_IN_USE"));

        return getCollection().updateMany(query, update)
                .onItem().ignore().andContinueWithNull();

    }

    public Uni<Void> HeaingVnf(@PathParam("name") String name){
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            // Tắt các pod có nhãn app: "reddit" trong namespace "default"
            client.pods().inNamespace(name).withLabel("app", "mongo").delete();
            client.pods().inNamespace(name).withLabel("app", "webapp").delete();
            System.out.println("Các pod đã được tắt.");


            TimeUnit.SECONDS.sleep(15);
            PodList podList = client.pods().inNamespace(name).list();

            List<VNFc> listVNFc = new ArrayList<>();
            // In ra tên của các Pod
            for (Pod pod : podList.getItems()) {
                String ip = pod.getStatus().getPodIP();
                String state = pod.getStatus().getPhase();
                String uid = pod.getMetadata().getUid();
                String podName = pod.getMetadata().getName();
                String nodeName = pod.getSpec().getNodeName();

                VNFc vnfc = new VNFc();
                vnfc.setId(uid);
                vnfc.setName(podName);
                vnfc.setIp(ip);
                vnfc.setNodeName(nodeName);
                vnfc.setState(state);

                listVNFc.add(vnfc);
            }
            Document query = new Document("name", name);
//            Document update = new Document("$set", new Document("vnfc", listVNFc));
//            Document update1 = new Document("$set", new Document("state", "IN_USE"));
            Document update = new Document("$set", new Document("vnfc", listVNFc));
            return getCollection().updateOne(query, update)
                    .onItem().ignore().andContinueWithNull();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Uni<List<VNF_Instance>> listVNF(){
        return getCollection()
                .find()
                .map(document -> {
                    VNF_Instance  vnf= new VNF_Instance();
                    vnf.setId(document.getString("id"));
                    vnf.setName(document.getString("name"));
                    vnf.setVnfdId(document.getString("vnfdId"));
                    vnf.setState(document.getString("state"));
                    vnf.setDescription(document.getString("description"));
                    return vnf;
                }).collect().asList();
    }

    public Uni<List<VNF_Instance>> listVNFc(@PathParam("name") String name){
        return getCollection()
                .find(new Document("name", name))
                .map(document -> {
                    VNF_Instance vnf = new VNF_Instance();
                    vnf.setId(document.getString("id"));
                    vnf.setName(document.getString("name"));
                    vnf.setState(document.getString("state"));
                    vnf.setVnfdId(document.getString("vnfdId"));
                    vnf.setDescription(document.getString("description"));
                    vnf.setVnfcList((List<VNFc>) document.get("vnfc"));
                    return vnf;
                }).collect().asList();
    }

    public Uni<Void> deleteVNF(@PathParam("name") String name){
        return getCollection().deleteOne( new Document("name", name))
                .onItem().ignore().andContinueWithNull();
    }

//    public Uni<Void> updateVnfInstance(@PathParam("vnfInstanceName") String vnfInstanceName) {
//
//        Document query = new Document("vnfInstanceName", vnfInstanceName);
//        Document update = new Document("$set", new Document("instantiationState", "Terminate"));
//
//        return getCollection().updateOne(query, update)
//                .onItem().ignore().andContinueWithNull();
//    }
//
//    public Uni<Void> updateInstantiateVnfInstance(@PathParam("vnfInstanceName") String vnfInstanceName) {
//
//        Document query = new Document("vnfInstanceName", vnfInstanceName);
//        Document update = new Document("$set", new Document("instantiationState", "Active"));
//
//        return getCollection().updateOne(query, update)
//                .onItem().ignore().andContinueWithNull();
//    }
//
//    public Uni<Void> addVnf(@PathParam("namespace") String namespace) throws IOException {
//        String toscaFilePath = "D:\\NFVM\\src\\main\\resources\\TOSCA.yaml";
//
//        String toscaContent = new String(Files.readAllBytes(Paths.get(toscaFilePath)));
//
//        Yaml yaml = new Yaml();
//        Object parsedObject = yaml.load(toscaContent);
//
//        if (parsedObject instanceof Map) {
//            Map<String, Object> toscaMap = (Map<String, Object>) parsedObject;
//            Map<String, Object> topologyTemplate = (Map<String, Object>) toscaMap.get("topology_template");
//            Map<String, Object> nodeTemplates = (Map<String, Object>) topologyTemplate.get("node_templates");
//            Map<String, Object> vnf001 = (Map<String, Object>) nodeTemplates.get("vnf_001");
//            Map<String, Object> properties = (Map<String, Object>) vnf001.get("properties");
//
//            String vnfInstanceName = (String) properties.get("vnfInstanceName");
//            String vnfInstanceDescription = (String) properties.get("vnfInstanceDescription");
//            String vnfdid = (String) properties.get("vnfdid");
//            String vnfProvider = (String) properties.get("vnfProvider");
//            String vnfProductName = (String) properties.get("vnfProductName");
//            String vnfSoftwareVersion = (String) properties.get("vnfSoftwareVersion");
//            String instantiationState = (String) properties.get("instantiationState");
//
//
//            Document document = new Document();
//            document.append("vnfInstanceName", vnfInstanceName);
//            document.append("vnfInstanceDescription", vnfInstanceDescription);
//            document.append("vnfdid", vnfdid);
//            document.append("vnfProvider", vnfProvider);
//            document.append("vnfProductName", vnfProductName);
//            document.append("vnfSoftwareVersion", vnfSoftwareVersion);
//            document.append("instantiationState", instantiationState);
//
//            return getCollection().insertOne(document)
//                    .onItem().ignore().andContinueWithNull();
//        }
//
//        return null;
//    }
//
//    public Uni<Void> DeleteVnf(@PathParam("vnfInstanceName") String vnfInstanceName){
//        Document filter = new Document("vnfInstanceName", vnfInstanceName);
//        return getCollection().deleteOne(filter)
//                .onItem().ignore().andContinueWithNull();
//    }

    @Inject
    ReactiveMongoClient mongoClient;

//    public Uni<List<Vnf>> listVnf(){
//        return getCollection()
//                .find()
//                .map(document -> {
//                    Vnf  vnf= new Vnf();
//                    vnf.setId(document.getString("id"));
//                    vnf.setVnfInstanceName(document.getString("vnfInstanceName"));
//                    vnf.setVnfInstanceDescription(document.getString("vnfInstanceDescription"));
//                    vnf.setVnfdid(document.getString("vnfdid"));
//                    vnf.setVnfProvider(document.getString("vnfProvider"));
//                    vnf.setVnfProductName(document.getString("vnfProductName"));
//                    vnf.setVnfSoftwareVersion(document.getString("vnfSoftwareVersion"));
//                    vnf.setInstantiationState(document.getString("instantiationState"));
//                    return vnf;
//                }).collect().asList();
//    }

    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnf");
    }
}
