//package org.acme.Service;
//
//import io.fabric8.kubernetes.api.model.LabelSelector;
//import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
//import io.fabric8.kubernetes.api.model.Pod;
//import io.fabric8.kubernetes.client.Config;
//import io.fabric8.kubernetes.client.ConfigBuilder;
//import io.fabric8.kubernetes.client.DefaultKubernetesClient;
//import io.fabric8.kubernetes.client.KubernetesClient;
//import io.quarkus.mongodb.reactive.ReactiveMongoClient;
//import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
//import io.smallrye.mutiny.Uni;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import jakarta.ws.rs.PathParam;
//import org.bson.Document;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//@ApplicationScoped
//public class VNFc_Services {
//    Config config = new ConfigBuilder()
//            .withMasterUrl("https://127.0.0.1:52175")
//            .withTrustCerts(true)
//            .build();
//    @Inject
//    ReactiveMongoClient mongoClient;
//
//    public Uni<Void> addVnfc1(@PathParam("namespace") String namespace) throws IOException {
//        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
//            LabelSelector selector = new LabelSelectorBuilder()
//                    .withMatchLabels(Collections.singletonMap("app", "mongo"))
//                    .build();
//
//            Pod podmongo = client.pods().inNamespace(namespace).withLabelSelector(selector).list().getItems().get(0);
//
//            // Lấy ID, tên, địa chỉ IP và trạng thái của các Pod
//            String ip = podmongo.getStatus().getPodIP();
//            String state = podmongo.getStatus().getPhase();
//            String uid = podmongo.getMetadata().getUid();
//            String podName = podmongo.getMetadata().getName();
//            String nodeName = podmongo.getSpec().getNodeName();
//
//
//            Document document = new Document();
//            document.append("nodeName", nodeName);
//            document.append("name", podName);
//            document.append("id", uid);
//            document.append("state", state);
//            document.append("ip", ip);
//
//            return getCollection().insertOne(document)
//                    .onItem().ignore().andContinueWithNull();
//        }
//    }
//
//
//    public Uni<Void> addVnfc2(@PathParam("namespace") String namespace) throws IOException {
//        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
//            String toscaFilePath = "D:\\NFVM\\src\\main\\resources\\TOSCA.yaml";
//
//            String toscaContent = new String(Files.readAllBytes(Paths.get(toscaFilePath)));
//
//            Yaml yaml = new Yaml();
//            Object parsedObject = yaml.load(toscaContent);
//
//            if (parsedObject instanceof Map) {
//
//                Map<String, Object> toscaMap1 = (Map<String, Object>) parsedObject;
//                Map<String, Object> topologyTemplate1 = (Map<String, Object>) toscaMap1.get("topology_template");
//                Map<String, Object> nodeTemplates1 = (Map<String, Object>) topologyTemplate1.get("node_templates");
//                Map<String, Object> mongo_deployment1 = (Map<String, Object>) nodeTemplates1.get("webapp_deployment");
//                Map<String, Object> properties1 = (Map<String, Object>) mongo_deployment1.get("properties");
//
//                String vnfInstanceName1 = (String) properties1.get("vnfInstanceName");
//                String vnfcName1 = (String) properties1.get("vnfcName");
//                String description1 = (String) properties1.get("description");
//                String vduid1 = (String) properties1.get("vduid");
//
//                LabelSelector selector = new LabelSelectorBuilder()
//                        .withMatchLabels(Collections.singletonMap("app", "webapp"))
//                        .build();
//
//                Pod podmongo = client.pods().inNamespace(namespace).withLabelSelector(selector).list().getItems().get(0);
//
//                // Lấy ID, tên, địa chỉ IP và trạng thái của các Pod
//                String ip1 = podmongo.getStatus().getPodIP();
//                String vnfcState1 = podmongo.getStatus().getPhase();
//
//                Document document1 = new Document();
//                document1.append("vnfInstanceName", vnfInstanceName1);
//                document1.append("vnfcName", vnfcName1);
//                document1.append("description", description1);
//                document1.append("vduid", vduid1);
//                document1.append("vnfcState", vnfcState1);
//                document1.append("ip", ip1);
//
//                return getCollection().insertOne(document1)
//                        .onItem().ignore().andContinueWithNull();
//
////             getCollection().insertOne(document1)
////                    .onItem().ignore().andContinueWithNull();
//            }
//
//            return null;
//        }
//    }
//
//    public Uni<Void> DeleteVnfc(@PathParam("vnfInstanceName") String vnfInstanceName){
//        Document filter = new Document("vnfInstanceName", vnfInstanceName);
//        return getCollection().deleteMany(filter)
//                .onItem().ignore().andContinueWithNull();
//    }
//
//
//    public Uni<Void> HeaingVnfc(@PathParam("namespace") String namespace){
//        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
//            // Tắt các pod có nhãn app: "reddit" trong namespace "default"
//            client.pods().inNamespace(namespace).withLabel("app", "mongo").delete();
//            client.pods().inNamespace(namespace).withLabel("app", "webapp").delete();
//            System.out.println("Các pod đã được tắt.");
//        }
//        return null;
//    }
//    public Uni<List<Vnfc>> listVnfc(){
//        return getCollection()
//                .find()
//                .map(document -> {
//                    Vnfc  vnfc= new Vnfc();
//                    vnfc.setVnfInstanceName(document.getString("vnfInstanceName"));
//                    vnfc.setVnfcName(document.getString("vnfcName"));
//                    vnfc.setDescription(document.getString("description"));
//                    vnfc.setVduid(document.getString("vduid"));
//                    vnfc.setVnfcState(document.getString("vnfcState"));
//                    vnfc.setIp(document.getString("ip"));
//                    return vnfc;
//                }).collect().asList();
//    }
//    private ReactiveMongoCollection<Document> getCollection(){
//        return mongoClient.getDatabase("test").getCollection("vnfc");
//    }
//}
