package org.acme.Service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.acme.Model.*;
import org.bson.Document;
import org.bson.conversions.Bson;


import javax.print.Doc;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LCM_Services {
    Config config = new ConfigBuilder()
            .withMasterUrl("https://127.0.0.1:58655")
            .withTrustCerts(true)
            .build();


    public CompletableFuture<Void> Instantiate(@PathParam("name") String name) throws InterruptedException {

        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            String secretYamlPath = "D:\\TOSCA\\TOSCA\\k8s_resources\\secret.yaml";
            byte[] secretYamlBytes = Files.readAllBytes(Paths.get(secretYamlPath));
            String secretYamlContent = new String(secretYamlBytes);

            Secret secret = Serialization.unmarshal(secretYamlContent, Secret.class);
            client.secrets().inNamespace(name).create(secret);

            String configMapYamlPath = "D:\\TOSCA\\TOSCA\\k8s_resources\\configMap.yaml";
            byte[] configMapYamlBytes = Files.readAllBytes(Paths.get(configMapYamlPath));
            String configMapYamlContent = new String(configMapYamlBytes);

            ConfigMap configMap = Serialization.unmarshal(configMapYamlContent, ConfigMap.class);
            client.configMaps().inNamespace(name).create(configMap);

            String MongodeploymentYamlPath = "D:\\TOSCA\\TOSCA\\k8s_resources\\mongo_deployment.yaml";
            byte[] MongodeploymentYamlBytes = Files.readAllBytes(Paths.get(MongodeploymentYamlPath));
            String MongodeploymentYamlContent = new String(MongodeploymentYamlBytes);

            Deployment Mongodeployment = Serialization.unmarshal(MongodeploymentYamlContent, Deployment.class);
            client.apps().deployments().inNamespace(name).create(Mongodeployment);

            String WebdeploymentYamlPath = "D:\\TOSCA\\TOSCA\\k8s_resources\\web_deployment.yaml";
            byte[] WebdeploymentYamlBytes = Files.readAllBytes(Paths.get(WebdeploymentYamlPath));
            String WebdeploymentYamlContent = new String(WebdeploymentYamlBytes);

            Deployment Webdeployment = Serialization.unmarshal(WebdeploymentYamlContent, Deployment.class);
            client.apps().deployments().inNamespace(name).create(Webdeployment);


                String MongoServiceYamlPath = "D:\\TOSCA\\TOSCA\\k8s_resources\\mongo_service.yaml";
                byte[] MongoServiceYamlBytes = Files.readAllBytes(Paths.get(MongoServiceYamlPath));
                String MongoServiceYamlContent = new String(MongoServiceYamlBytes);

                Service MongoService = Serialization.unmarshal(MongoServiceYamlContent, Service.class);
                client.services().inNamespace(name).create(MongoService);

            ServiceList services = client.services().inAnyNamespace().list();
            boolean serviceExists = services.getItems().stream()
                    .anyMatch(service -> service.getMetadata().getName().equals("webapp-service"));

            if (!serviceExists) {
                String WebServiceYamlPath = "D:\\TOSCA\\TOSCA\\k8s_resources\\web_service.yaml";
                byte[] WebServiceYamlBytes = Files.readAllBytes(Paths.get(WebServiceYamlPath));
                String WebServiceYamlContent = new String(WebServiceYamlBytes);

                Service WebService = Serialization.unmarshal(WebServiceYamlContent, Service.class);
                client.services().inNamespace(name).create(WebService);
            }

            System.out.println("Đã bật VNF thành công");

            TimeUnit.SECONDS.sleep(30);

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

            DeploymentList deploymentList = client.apps().deployments().inNamespace(name).list();
            List<VNFc> listDeployment = new ArrayList<>();
            for(Deployment deployment : deploymentList.getItems()){
                String name1 = deployment.getMetadata().getName();
                VNFc vnFc = new VNFc();
                vnFc.setName(name1);
                vnFc.setNumberofinstance(1);
                listDeployment.add(vnFc);
            }

            List<LCMOPCC> listLcmOpocc = new ArrayList<>();

            for(Pod pod : podList.getItems()){
                LCMOPCC lcmopcc = new LCMOPCC();
                String name1 = pod.getMetadata().getName();
                VNFc vnFc = new VNFc();
                vnFc.setName(name1);
                vnFc.setLcmState("ADDED");
                lcmopcc.setOperation("INSTANTIATE");
                lcmopcc.setOperationState("COMPLETED");
                Date currentTime = new Date();
                lcmopcc.setStartTime(currentTime);
                lcmopcc.setAffectVnfc(vnFc);
                listLcmOpocc.add(lcmopcc);
            }


//            List<LCMOPCC> listLcmOpocc = new ArrayList<>();
//            LCMOPCC lcmopcc = new LCMOPCC();
//            lcmopcc.setOperationState("Instatiate");
//            Date currentTime = new Date();
//            lcmopcc.setStartTime(currentTime);
//
//            listLcmOpocc.add(lcmopcc);

            Document document = new Document()
                    .append("vnfc", listDeployment)
                    .append("name", name);

//             getInstanceCollection().insertOne(document).onItem().ignore().andContinueWithNull();

            Document query = new Document("name", name);
//            Document update = new Document("$set", new Document("vnfc", listVNFc));
//            Document update1 = new Document("$set", new Document("state", "IN_USE"));
            Document update = new Document("$set", new Document("vnfc", listVNFc).append("status", "STARTED"));
//            return getCollection().updateMany(query, update)
//                    .onItem().ignore().andContinueWithNull();
//             getCollection().updateOne(query, update1)
//                    .onItem().ignore().andContinueWithNull();
//             return null;

            CompletableFuture<Void> future = new CompletableFuture<>();

            getInstanceCollection().insertOne(document)
                    .onItem().ignore().andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với insertOne, thường không cần xử lý kết quả ở đây
                    });

            getCollection().updateMany(query, update)
                    .onItem().ignore().andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với updateMany, thường không cần xử lý kết quả ở đây
                    });

            getCollection().updateOne(Filters.eq("name", name),
                            Updates.pushEach("lcmopocc", listLcmOpocc))
                    .onItem().ignore().andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với insertOne, thường không cần xử lý kết quả ở đây
                    });

            // Khi cả hai hoạt động hoàn thành, hoặc bất kỳ lỗi nào xảy ra,
            // chúng ta gán giá trị null cho CompletableFuture để hoàn thành nó
            future.complete(null);

            return future;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        return null;
    }

    public CompletableFuture<Void> Terminate(@PathParam("name") String name){
        List<LCMOPCC> listLcmOpocc = new ArrayList<>();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            PodList podList = client.pods().inNamespace(name).list();
            for(Pod pod : podList.getItems()){
                LCMOPCC lcmopcc = new LCMOPCC();
                String name1 = pod.getMetadata().getName();
                VNFc vnFc = new VNFc();
                vnFc.setName(name1);
                vnFc.setLcmState("REMOVED");
                lcmopcc.setOperation("TERMINATE");
                lcmopcc.setOperationState("COMPLETED");
                Date currentTime = new Date();
                lcmopcc.setStartTime(currentTime);
                lcmopcc.setAffectVnfc(vnFc);
                listLcmOpocc.add(lcmopcc);
            }
            client.apps().deployments().inNamespace(name).delete();
            client.apps().deployments().inNamespace(name).delete();
            client.services().inNamespace(name).delete();
            client.services().inNamespace(name).delete();
            client.secrets().inNamespace(name).delete();
            client.configMaps().inNamespace(name).delete();
            System.out.println("Pod đã được tắt.");

        }

        Document query = new Document("name", name);
//        Document update = new Document("$set", new Document("vnfc", null));
//        Document update1 = new Document("$set", new Document("state", "NOT_IN_USE"));
        Document update = new Document("$set", new Document("vnfc", null).append("state", "STOPPED"));

        Document lcmopoccDocument = new Document("lcmopocc", listLcmOpocc);

        CompletableFuture<Void> future = new CompletableFuture<>();
        getCollection().updateMany(query, update)
                .onItem().ignore().andContinueWithNull()
                .subscribe().with(result -> {
                    // Xử lý kết quả nếu cần
                    // Đối với insertOne, thường không cần xử lý kết quả ở đây
                });

        getCollection().updateOne(Filters.eq("name", name),
                Updates.pushEach("lcmopocc", listLcmOpocc))
                .onItem().ignore().andContinueWithNull()
                .subscribe().with(result -> {
                    // Xử lý kết quả nếu cần
                    // Đối với insertOne, thường không cần xử lý kết quả ở đây
                });

        future.complete(null);

        return future;
    }



    public CompletableFuture<Void> HealingVnf(@PathParam("name") String name){
        List<LCMOPCC> listLcmOpocc = new ArrayList<>();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            PodList podList2 = client.pods().inNamespace(name).list();
            for(Pod pod : podList2.getItems()){
                LCMOPCC lcmopcc = new LCMOPCC();
                String name1 = pod.getMetadata().getName();
                VNFc vnFc = new VNFc();
                vnFc.setName(name1);
                vnFc.setLcmState("REMOVED");
                lcmopcc.setOperation("HEALING");
                lcmopcc.setOperationState("COMPLETED");
                Date currentTime = new Date();
                lcmopcc.setStartTime(currentTime);
                lcmopcc.setAffectVnfc(vnFc);
                listLcmOpocc.add(lcmopcc);
            }
            // Tắt các pod có nhãn app: "reddit" trong namespace "default"
            client.pods().inNamespace(name).delete();
            System.out.println("Các pod đã được tắt.");

            TimeUnit.SECONDS.sleep(30);

            PodList podList = client.pods().inNamespace(name).list();
            for(Pod pod : podList.getItems()){
                LCMOPCC lcmopcc = new LCMOPCC();
                String name1 = pod.getMetadata().getName();
                VNFc vnFc = new VNFc();
                vnFc.setName(name1);
                vnFc.setLcmState("ADDED");
                lcmopcc.setOperation("HEALING");
                lcmopcc.setOperationState("COMPLETED");
                Date currentTime = new Date();
                lcmopcc.setStartTime(currentTime);
                lcmopcc.setAffectVnfc(vnFc);
                listLcmOpocc.add(lcmopcc);
            }

            TimeUnit.SECONDS.sleep(30);
            PodList podList1 = client.pods().inNamespace(name).list();
            List<VNFc> listVNFc = new ArrayList<>();
            // In ra tên của các Pod
            for (Pod pod : podList1.getItems()) {
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

            CompletableFuture<Void> future = new CompletableFuture<>();
            Document query = new Document("name", name);
//            Document update = new Document("$set", new Document("vnfc", listVNFc));
//            Document update1 = new Document("$set", new Document("state", "IN_USE"));
            Document update = new Document("$set", new Document("vnfc", listVNFc));
             getCollection().updateOne(query, update)
                     .onItem().ignore().andContinueWithNull()
                     .subscribe().with(result -> {
                         // Xử lý kết quả nếu cần
                         // Đối với insertOne, thường không cần xử lý kết quả ở đây
                     });;

            getCollection().updateOne(Filters.eq("name", name),
                            Updates.pushEach("lcmopocc", listLcmOpocc))
                    .onItem().ignore().andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với insertOne, thường không cần xử lý kết quả ở đây
                    });;

            future.complete(null);

            return future;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    public CompletableFuture<Void> scaleVNF(@PathParam("name") String name, VNFc vnFc, @QueryParam("vnfcName") String vnfcName) throws InterruptedException {
        List<LCMOPCC> listLcmOpocc = new ArrayList<>();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            PodList podList1 = client.pods().inNamespace(name).list();

//                LCMOPCC lcmopcc = new LCMOPCC();
//                VNFc vnFc1 = new VNFc();
//                vnFc1.setName(vnfcName);
//                vnFc1.setLcmState("SCALE");
//                lcmopcc.setOperation("SCALE");
//                lcmopcc.setOperationState("COMPLETED");
//                Date currentTime = new Date();
//                lcmopcc.setStartTime(currentTime);
//                lcmopcc.setAffectVnfc(vnFc1);
//                listLcmOpocc.add(lcmopcc);

            client.apps().deployments().inNamespace(name).withName(vnfcName)
                    .scale(vnFc.getNumberofinstance());

                            TimeUnit.SECONDS.sleep(30);
                PodList podList = client.pods().inNamespace(name).list();

                List<Pod> newPod = new ArrayList<>();
                for(Pod pod : podList.getItems()){
                    boolean isNew = true;
                    for (Pod currentPod : podList1.getItems()) {
                        if (pod.getMetadata().getName().equals(currentPod.getMetadata().getName())) {
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        newPod.add(pod);
                    }
                }

            for(Pod pod : newPod){
                LCMOPCC lcmopcc = new LCMOPCC();
                String name1 = pod.getMetadata().getName();
                System.out.println(name1);
                VNFc vnFc1 = new VNFc();
                vnFc1.setName(name1);
                vnFc1.setLcmState("ADDED");
                lcmopcc.setOperation("SCALE");
                lcmopcc.setOperationState("COMPLETED");
                Date currentTime = new Date();
                lcmopcc.setStartTime(currentTime);
                lcmopcc.setAffectVnfc(vnFc1);
                listLcmOpocc.add(lcmopcc);
            }



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
//                return getCollection().updateMany(query, update)
//                        .onItem().ignore().andContinueWithNull();

            Document document1 = new Document("$set", new Document("vnfc.$.numberofinstance", vnFc.getNumberofinstance()));

            Document query1 = new Document(
                    "vnfc",
                    new Document("$elemMatch",
                            new Document()
                                    .append("name", vnfcName)
                    )
            )
                    .append("name", name);

//            return getInstanceCollection().updateOne(query1, document1).onItem().ignore()
//                    .andContinueWithNull();

            CompletableFuture<Void> future = new CompletableFuture<>();

            getCollection().updateMany(query, update)
                    .onItem().ignore().andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với insertOne, thường không cần xử lý kết quả ở đây
                    });

            getInstanceCollection().updateOne(query1, document1).onItem().ignore()
                    .andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với updateMany, thường không cần xử lý kết quả ở đây
                    });
            getCollection().updateOne(Filters.eq("name", name),
                            Updates.pushEach("lcmopocc", listLcmOpocc))
                    .onItem().ignore().andContinueWithNull()
                    .subscribe().with(result -> {
                        // Xử lý kết quả nếu cần
                        // Đối với insertOne, thường không cần xử lý kết quả ở đây
                    });;

            // Khi cả hai hoạt động hoàn thành, hoặc bất kỳ lỗi nào xảy ra,
            // chúng ta gán giá trị null cho CompletableFuture để hoàn thành nó
            future.complete(null);

            return future;
        }

//        Document document1 = new Document("$set", new Document("vnfc.$.numberofinstance", vnFc.getNumberofinstance()));
//
//        Document query1 = new Document(
//                "vnfc",
//                new Document("$elemMatch",
//                        new Document()
//                                .append("name", vnfcName)
//                )
//        )
//                .append("name", name);
//
//        return getInstanceCollection().updateOne(query1, document1).onItem().ignore()
//                .andContinueWithNull();
    }
//        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
//            DeploymentList deploymentList = client.apps().deployments().inNamespace(name).list();
//            List<VNFc> listDeployment = new ArrayList<>();
//            for (Deployment deployment : deploymentList.getItems()) {
//                String name1 = deployment.getMetadata().getName();
//                VNFc vnFc = new VNFc();
//                vnFc.setName(name1);
//                vnFc.setNumberofinstance(2);
//                listDeployment.add(vnFc);
//            }
//            Document query = new Document("name", name);
//
//            Document document = new Document()
//                    .append("$set", new Document("vnfc", listDeployment));
//
//            return getInstanceCollection().updateOne(query, document).onItem().ignore().andContinueWithNull();
//        }
//    }
//            try (KubernetesClient client = new DefaultKubernetesClient(config)) {
////                client.apps().deployments().inNamespace(name).withLabel("app", "mongo")
////                        .resources()
////                        .forEach(s -> s.scale(x));
////                client.apps().deployments().inNamespace(name).withLabel("app", "webapp")
////                        .resources()
////                        .forEach(s -> s.scale(y));
//
//
//
//                TimeUnit.SECONDS.sleep(15);
//                PodList podList = client.pods().inNamespace(name).list();
//
//                List<VNFc> listVNFc = new ArrayList<>();
//                // In ra tên của các Pod
//                for (Pod pod : podList.getItems()) {
//                    String ip = pod.getStatus().getPodIP();
//                    String state = pod.getStatus().getPhase();
//                    String uid = pod.getMetadata().getUid();
//                    String podName = pod.getMetadata().getName();
//                    String nodeName = pod.getSpec().getNodeName();
//
//                    VNFc vnfc = new VNFc();
//                    vnfc.setId(uid);
//                    vnfc.setName(podName);
//                    vnfc.setIp(ip);
//                    vnfc.setNodeName(nodeName);
//                    vnfc.setState(state);
//
//                    listVNFc.add(vnfc);
//                }
//
//
//                Document query = new Document("name", name);
////            Document update = new Document("$set", new Document("vnfc", listVNFc));
////            Document update1 = new Document("$set", new Document("state", "IN_USE"));
//                Document update = new Document("$set", new Document("vnfc", listVNFc));
//                return getCollection().updateMany(query, update)
//                        .onItem().ignore().andContinueWithNull();
//
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

    public Uni<List<VNF_Instance>> listLCMOPOCC(@PathParam("name") String name){
        return getCollection()
                .find(new Document("name", name))
                .map(document -> {
                    VNF_Instance vnf_instance = new VNF_Instance();
                    vnf_instance.setListLcmOpcc((List<LCMOPCC>) document.get("lcmopocc"));
                    return vnf_instance;
                }).collect().asList();
    }
    public Uni<List<Vnfd>> listVNFD(){
        return getVnfdCollection()
                .find()
                .map(document -> {
                    Vnfd  vnfd= new Vnfd();
                    vnfd.setName(document.getString("name"));
                    vnfd.setState(document.getString("state"));
                    vnfd.setProvider(document.getString("provider"));
                    vnfd.setVersion(document.getString("version"));
                    vnfd.setCreatedBy(document.getString("createdBy"));
                    return vnfd;
                }).collect().asList();
    }


    public Uni<List<VNF_Instance>> listVNF(@QueryParam("name") String name){
        String regexPattern = ".*" + name + ".*"; // Tạo mẫu regex gần đúng
        Bson regexFilter = Filters.regex("name", regexPattern, "i"); // "i" để không phân biệt chữ hoa chữ thường
        return getCollection()
                .find(regexFilter)
                .map(document -> {
                    VNF_Instance  vnf= new VNF_Instance();
                    vnf.set_id(document.getObjectId("_id"));
                    vnf.setName(document.getString("name"));
                    vnf.setVnfdName(document.getString("vnfdName"));
                    vnf.setStatus(document.getString("status"));
                    vnf.setDescription(document.getString("description"));
                    return vnf;
                }).collect().asList();
    }

    public Uni<List<Instance>> listDeployment(@PathParam("name") String name) throws InterruptedException {
       return getInstanceCollection()
               .find(new Document("name", name))
               .map(document -> {
                   Instance instance = new Instance();
                   instance.setName(document.getString("name"));
                   instance.setVnfcList((List<VNFc>) document.get("vnfc"));
                   return instance;
               })
               .collect().asList();
    }


//    public List<Instance> listDeployment(@PathParam("name") String name) {
//        CopyOnWriteArrayList<Instance> resultList = new CopyOnWriteArrayList<>();
//
//        CompletableFuture<List<Instance>> completionStage = getInstanceCollection()
//                .find(new Document("name", name))
//                .map(document -> {
//                    Instance instance = new Instance();
//                    instance.setName(document.getString("name"));
//                    instance.setVnfcList((List<VNFc>) document.get("vnfc"));
//                    resultList.add(instance);
//                    return instance;
//                })
//                .collect().asList().subscribe().asCompletionStage();
//
//        completionStage.toCompletableFuture().join(); // Chờ đợi hoàn thành
//
//        return resultList;
//    }
    public Uni<List<VNF_Instance>> listVNFc(@PathParam("name") String name){
        return getCollection()
                .find(new Document("name", name))
                .map(document -> {
                    VNF_Instance vnf = new VNF_Instance();
                    vnf.set_id(document.getObjectId("_id"));
                    vnf.setName(document.getString("name"));
                    vnf.setStatus(document.getString("status"));
                    vnf.setVnfdName(document.getString("vnfdName"));
                    vnf.setDescription(document.getString("description"));
                    vnf.setVnfcList((List<VNFc>) document.get("vnfc"));
                    return vnf;
                }).collect().asList();
    }

    public CompletableFuture<Void> deleteVNF(@PathParam("name") String name){
        try(KubernetesClient client = new DefaultKubernetesClient(config)){
            client.namespaces().withName(name).delete();
            System.out.println("Da xoa VNF Instance");
        }
        CompletableFuture<Void> future = new CompletableFuture<>();

        getInstanceCollection().deleteMany( new Document("name", name))
                .onItem().ignore().andContinueWithNull()
                .subscribe().with(result -> {
                    // Xử lý kết quả nếu cần
                    // Đối với insertOne, thường không cần xử lý kết quả ở đây
                });;
        getCollection().deleteOne( new Document("name", name))
                .onItem().ignore().andContinueWithNull()
                .subscribe().with(result -> {
                    // Xử lý kết quả nếu cần
                    // Đối với insertOne, thường không cần xử lý kết quả ở đây
                });;
        future.complete(null);

        return future;
    }


    @Inject
    ReactiveMongoClient mongoClient;

    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnf");
    }

    private ReactiveMongoCollection<Document> getInstanceCollection(){
        return mongoClient.getDatabase("test").getCollection("vnfc");
    }

    private ReactiveMongoCollection<Document> getVnfdCollection(){
        return mongoClient.getDatabase("test").getCollection("vnfd");
    }

    private ReactiveMongoCollection<Document> getLcmOpoccCollection(){
        return mongoClient.getDatabase("test").getCollection("lcmopocc");
    }
}
