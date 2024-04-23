package org.acme.Service;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.Model.*;
import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TOSCA_Services {

    public Deployment addDeployment() throws IOException {
        String metadataFilePath = "D:\\20232\\TOSCA\\metadata\\tosca.yaml";

        String metadataContent = new String(Files.readAllBytes(Paths.get(metadataFilePath)));

        Yaml metadata3 = new Yaml();
        Object parsedObject = metadata3.load(metadataContent);
        Deployment deployment = new Deployment();

        if (parsedObject instanceof Map) {
            Map<String, Object> metadataMap1 = (Map<String, Object>) parsedObject;
            String name4 = (String) metadataMap1.get("name");
            Double versionValue = (Double) metadataMap1.get("version");
            String version = String.valueOf(versionValue);
            String createdBy = (String) metadataMap1.get("createdBy");


            String mongoDeploymentFilePath = "D:\\20232\\TOSCA\\k8s_resources\\mongo_deployment.yaml";

            String mongoDeploymentContent = new String(Files.readAllBytes(Paths.get(mongoDeploymentFilePath)));

            Yaml MongoDeploymentdata = new Yaml();
            Object parsedObject1 = MongoDeploymentdata.load(mongoDeploymentContent);

            Map<String, Object> MongoDeploymentMap = (Map<String, Object>) parsedObject1;
            String apiVersion = (String) MongoDeploymentMap.get("apiVersion");
            String kind = (String) MongoDeploymentMap.get("kind");
            Map<String, Object> MetadataMap = (Map<String, Object>) MongoDeploymentMap.get("metadata");
            String name = (String) MetadataMap.get("name");
            Deployment.Metadata metadata = new Deployment.Metadata();
            metadata.setName(name);
            Map<String, Object> LabelsMap = (Map<String, Object>) MetadataMap.get("labels");
            String app = (String) LabelsMap.get("app");
            Deployment.Metadata.Labels labels = new Deployment.Metadata.Labels();
            labels.setApp(app);
            metadata.setLabels(labels);

            System.out.println(name);
            Map<String, Object> SpecMap = (Map<String, Object>) MongoDeploymentMap.get("spec");
            Integer replicasString = (Integer) SpecMap.get("replicas");
            String replicas = replicasString.toString();
            Map<String, Object> selectorMap = (Map<String, Object>) SpecMap.get("selector");
            Map<String, Object> matchLabelsMap = (Map<String, Object>) selectorMap.get("matchLabels");
            String app1 = (String) matchLabelsMap.get("app");
            Deployment.Spec.Selector.MatchLabels matchLabels = new Deployment.Spec.Selector.MatchLabels();
            matchLabels.setApp(app1);
            Deployment.Spec.Selector selector = new Deployment.Spec.Selector();
            selector.setMatchLabels(matchLabels);
            Map<String, Object> templateMap = (Map<String, Object>) SpecMap.get("template");
            Map<String, Object> metadataMap = (Map<String, Object>) templateMap.get("metadata");
            Map<String, Object> labelsMap = (Map<String, Object>) metadataMap.get("labels");
            String app2 = (String) labelsMap.get("app");
            Deployment.Spec.Template.Metadata.Labels labels1 = new Deployment.Spec.Template.Metadata.Labels();
            labels1.setApp(app2);
            Deployment.Spec.Template.Metadata metadata1 = new Deployment.Spec.Template.Metadata();
            metadata1.setLabels(labels1);
            Deployment.Spec.Template template = new Deployment.Spec.Template();
            template.setMetadata(metadata1);

            Map<String, Object> specifitionMap = (Map<String, Object>) templateMap.get("spec");
//            Map<String, Object> containersMap = (Map<String, Object>) specifitionMap.get("containers");
            List<Map<String, Object>> containersMap = (List<Map<String, Object>>) specifitionMap.get("containers");
            for (Map<String, Object> container : containersMap) {
                String name1 = (String) container.get("name");
                String image = (String) container.get("image");
//                Map<String, Object> portMap = (Map<String, Object>) container.get("ports");
                List<Map<String, Object>> portsMap = (List<Map<String, Object>>) container.get("ports");
                Deployment.Spec.Template.Specification.Container.Ports ports = new Deployment.Spec.Template.Specification.Container.Ports();
                for (Map<String, Object> port : portsMap) {
//                    String containerPort = (String) port.get("containerPort");
                    Integer containerPortInteger = (Integer) port.get("containerPort");
                    String containerPort = containerPortInteger.toString();
                    ports.setContainerPort(containerPort);
                }

//                Deployment.Spec.Template.Specification.Container.Ports ports = new Deployment.Spec.Template.Specification.Container.Ports();
//                ports.setContainerPort(containerPort);
                Deployment.Spec.Template.Specification.Container containers = new Deployment.Spec.Template.Specification.Container();
                containers.setName(name1);
                containers.setImage(image);
                containers.setPorts(ports);
                Deployment.Spec.Template.Specification specification = new Deployment.Spec.Template.Specification();
                specification.setContainers(containers);

                template.setSpecification(specification);

                Deployment.Spec spec = new Deployment.Spec();
                spec.setReplicas(replicas);
                spec.setSelector(selector);
                spec.setTemplate(template);

//                Deployment deployment = new Deployment();
                deployment.setApiVersion(apiVersion);
                deployment.setKind(kind);
                deployment.setMetadata(metadata);
                deployment.setSpec(spec);


//
            }
        }

        return  deployment;
    }

    public Deployment addDeployment2() throws IOException {

        Deployment deployment = new Deployment();


            String mongoDeploymentFilePath = "D:\\20232\\TOSCA\\k8s_resources\\web_deployment.yaml";

            String mongoDeploymentContent = new String(Files.readAllBytes(Paths.get(mongoDeploymentFilePath)));

            Yaml MongoDeploymentdata = new Yaml();
            Object parsedObject1 = MongoDeploymentdata.load(mongoDeploymentContent);

            Map<String, Object> MongoDeploymentMap = (Map<String, Object>) parsedObject1;
            String apiVersion = (String) MongoDeploymentMap.get("apiVersion");
            String kind = (String) MongoDeploymentMap.get("kind");
            Map<String, Object> MetadataMap = (Map<String, Object>) MongoDeploymentMap.get("metadata");
            String name = (String) MetadataMap.get("name");
            Deployment.Metadata metadata = new Deployment.Metadata();
            metadata.setName(name);
            Map<String, Object> LabelsMap = (Map<String, Object>) MetadataMap.get("labels");
            String app = (String) LabelsMap.get("app");
            Deployment.Metadata.Labels labels = new Deployment.Metadata.Labels();
            labels.setApp(app);
            metadata.setLabels(labels);

            System.out.println(name);
            Map<String, Object> SpecMap = (Map<String, Object>) MongoDeploymentMap.get("spec");
            Integer replicasString = (Integer) SpecMap.get("replicas");
            String replicas = replicasString.toString();
            Map<String, Object> selectorMap = (Map<String, Object>) SpecMap.get("selector");
            Map<String, Object> matchLabelsMap = (Map<String, Object>) selectorMap.get("matchLabels");
            String app1 = (String) matchLabelsMap.get("app");
            Deployment.Spec.Selector.MatchLabels matchLabels = new Deployment.Spec.Selector.MatchLabels();
            matchLabels.setApp(app1);
            Deployment.Spec.Selector selector = new Deployment.Spec.Selector();
            selector.setMatchLabels(matchLabels);
            Map<String, Object> templateMap = (Map<String, Object>) SpecMap.get("template");
            Map<String, Object> metadataMap = (Map<String, Object>) templateMap.get("metadata");
            Map<String, Object> labelsMap = (Map<String, Object>) metadataMap.get("labels");
            String app2 = (String) labelsMap.get("app");
            Deployment.Spec.Template.Metadata.Labels labels1 = new Deployment.Spec.Template.Metadata.Labels();
            labels1.setApp(app2);
            Deployment.Spec.Template.Metadata metadata1 = new Deployment.Spec.Template.Metadata();
            metadata1.setLabels(labels1);
            Deployment.Spec.Template template = new Deployment.Spec.Template();
            template.setMetadata(metadata1);

            Map<String, Object> specifitionMap = (Map<String, Object>) templateMap.get("spec");
//            Map<String, Object> containersMap = (Map<String, Object>) specifitionMap.get("containers");
            List<Map<String, Object>> containersMap = (List<Map<String, Object>>) specifitionMap.get("containers");
            for (Map<String, Object> container : containersMap) {
                String name1 = (String) container.get("name");
                String image = (String) container.get("image");
//                Map<String, Object> portMap = (Map<String, Object>) container.get("ports");
                List<Map<String, Object>> portsMap = (List<Map<String, Object>>) container.get("ports");
                Deployment.Spec.Template.Specification.Container.Ports ports = new Deployment.Spec.Template.Specification.Container.Ports();
                for (Map<String, Object> port : portsMap) {
//                    String containerPort = (String) port.get("containerPort");
                    Integer containerPortInteger = (Integer) port.get("containerPort");
                    String containerPort = containerPortInteger.toString();
                    ports.setContainerPort(containerPort);
                }

//                Deployment.Spec.Template.Specification.Container.Ports ports = new Deployment.Spec.Template.Specification.Container.Ports();
//                ports.setContainerPort(containerPort);
                Deployment.Spec.Template.Specification.Container containers = new Deployment.Spec.Template.Specification.Container();
                containers.setName(name1);
                containers.setImage(image);
                containers.setPorts(ports);
                Deployment.Spec.Template.Specification specification = new Deployment.Spec.Template.Specification();
                specification.setContainers(containers);

                template.setSpecification(specification);

                Deployment.Spec spec = new Deployment.Spec();
                spec.setReplicas(replicas);
                spec.setSelector(selector);
                spec.setTemplate(template);

//                Deployment deployment = new Deployment();
                deployment.setApiVersion(apiVersion);
                deployment.setKind(kind);
                deployment.setMetadata(metadata);
                deployment.setSpec(spec);


        }

        return  deployment;
    }


    public Service addService() throws IOException {
        String mongoServiceFilePath = "D:\\20232\\TOSCA\\k8s_resources\\mongo_service.yaml";

        String mongoServiceContent = new String(Files.readAllBytes(Paths.get(mongoServiceFilePath)));

        Yaml MongoServicedata = new Yaml();
        Object parsedObject2 = MongoServicedata.load(mongoServiceContent);
        Map<String, Object> MongoServiceMap = (Map<String, Object>) parsedObject2;
        String apiVersionService = (String) MongoServiceMap.get("apiVersion");
        String kindService = (String) MongoServiceMap.get("kind");
        Map<String, Object> metadataServiceMap = (Map<String, Object>) MongoServiceMap.get("metadata");
        String nameService = (String) metadataServiceMap.get("name");
        Service.Metadata metadataService = new Service.Metadata();
        metadataService.setName(nameService);
        Map<String, Object> specServiceMap = (Map<String, Object>) MongoServiceMap.get("spec");
        Map<String, Object> selectorServiceMap = (Map<String, Object>) specServiceMap.get("selector");
        String appService = (String) selectorServiceMap.get("app");
        Service.Spec.Selector selectorService = new Service.Spec.Selector();
        selectorService.setApp(appService);
        List<Map<String, Object>> portsServiceMap = (List<Map<String, Object>>) specServiceMap.get("ports");
        Service.Spec.Ports portsService = new Service.Spec.Ports();
        Service mongoService = new Service();
        for (Map<String, Object> ports : portsServiceMap) {
            String protocol = (String) ports.get("protocol");
            int port = (int) ports.get("port");
            int targetPort = (int) ports.get("targetPort");
            portsService.setProtocol(protocol);
            portsService.setPort(port);
            portsService.setTargetPort(targetPort);

            Service.Spec specService = new Service.Spec();
            specService.setSelector(selectorService);
            specService.setPorts(portsService);
//            Service mongoService = new Service();
            mongoService.setApiVersion(apiVersionService);
            mongoService.setApiVersion(kindService);
            mongoService.setMetadata(metadataService);
            mongoService.setSpec(specService);
//            Document query = new Document("name", "MERN-app");
//            Document update = new Document("$set", new Document("service", mongoService));
//
//            System.out.println(apiVersionService);
//            System.out.println(kindService);
//            return getCollection().updateOne(query, update)
//                    .onItem().ignore().andContinueWithNull();
        }
        return mongoService;
    }


    public Service addService2() throws IOException {
        String mongoServiceFilePath = "D:\\20232\\TOSCA\\k8s_resources\\web_service.yaml";

        String mongoServiceContent = new String(Files.readAllBytes(Paths.get(mongoServiceFilePath)));

        Yaml MongoServicedata = new Yaml();
        Object parsedObject2 = MongoServicedata.load(mongoServiceContent);
        Map<String, Object> MongoServiceMap = (Map<String, Object>) parsedObject2;
        String apiVersionService = (String) MongoServiceMap.get("apiVersion");
        String kindService = (String) MongoServiceMap.get("kind");
        Map<String, Object> metadataServiceMap = (Map<String, Object>) MongoServiceMap.get("metadata");
        String nameService = (String) metadataServiceMap.get("name");
        Service.Metadata metadataService = new Service.Metadata();
        metadataService.setName(nameService);
        Map<String, Object> specServiceMap = (Map<String, Object>) MongoServiceMap.get("spec");
        String type = (String) specServiceMap.get("type");
        Map<String, Object> selectorServiceMap = (Map<String, Object>) specServiceMap.get("selector");
        String appService = (String) selectorServiceMap.get("app");
        Service.Spec.Selector selectorService = new Service.Spec.Selector();
        selectorService.setApp(appService);
        List<Map<String, Object>> portsServiceMap = (List<Map<String, Object>>) specServiceMap.get("ports");
        Service.Spec.Ports portsService = new Service.Spec.Ports();
        Service mongoService = new Service();
        for (Map<String, Object> ports : portsServiceMap) {
            String protocol = (String) ports.get("protocol");
            int port = (int) ports.get("port");
            int targetPort = (int) ports.get("targetPort");
            int nodePort = (int) ports.get("nodePort");
            portsService.setProtocol(protocol);
            portsService.setPort(port);
            portsService.setTargetPort(targetPort);
            portsService.setNodePort(nodePort);

            Service.Spec specService = new Service.Spec();
            specService.setSelector(selectorService);
            specService.setPorts(portsService);
            specService.setType(type);
//            Service mongoService = new Service();
            mongoService.setApiVersion(apiVersionService);
            mongoService.setApiVersion(kindService);
            mongoService.setMetadata(metadataService);
            mongoService.setSpec(specService);
//            Document query = new Document("name", "MERN-app");
//            Document update = new Document("$set", new Document("service", mongoService));
//
//            System.out.println(apiVersionService);
//            System.out.println(kindService);
//            return getCollection().updateOne(query, update)
//                    .onItem().ignore().andContinueWithNull();
        }
        return mongoService;
    }

    public ConfigMap addConfigMap() throws IOException{
        String configMapFilePath = "D:\\20232\\TOSCA\\k8s_resources\\configMap.yaml";

        String configMapContent = new String(Files.readAllBytes(Paths.get(configMapFilePath)));

        Yaml configMapdata = new Yaml();
        Object parsedObject3 = configMapdata.load(configMapContent);
        Map<String, Object> configMapMap = (Map<String, Object>) parsedObject3;
        String apiVersionConfigMap = (String) configMapMap.get("apiVersion");
        String kindConfigMap = (String) configMapMap.get("kind");
        Map<String, Object> metadataConfigMap = (Map<String, Object>) configMapMap.get("metadata");
        String nameconfigMap = (String) metadataConfigMap.get("name");
        ConfigMap.Metadata metadataConfigMap1 = new ConfigMap.Metadata();
        metadataConfigMap1.setName(nameconfigMap);
        Map<String, Object> dataMap = (Map<String, Object>) configMapMap.get("data");
        String mongo_url = (String) dataMap.get("mongo-url");
        ConfigMap.Data dataConfigMap = new ConfigMap.Data();
        dataConfigMap.setMongoUrl(mongo_url);
        ConfigMap configMap = new ConfigMap();
        configMap.setApiVersion(apiVersionConfigMap);
        configMap.setKind(kindConfigMap);
        configMap.setMetadata(metadataConfigMap1);
        configMap.setData(dataConfigMap);
//        Document query = new Document("name", "MERN-app");
//        Document update = new Document("$set", new Document("configMap", configMap));
//
//        return getCollection().updateOne(query, update)
//                .onItem().ignore().andContinueWithNull();
        return configMap;
    }

    public Secret addSecret() throws IOException{
        String SecretFilePath = "D:\\20232\\TOSCA\\k8s_resources\\secret.yaml";

        String SecretContent = new String(Files.readAllBytes(Paths.get(SecretFilePath)));

        Yaml Secretdata = new Yaml();
        Object parsedObject4 = Secretdata.load(SecretContent);
        Map<String, Object> SecretMap = (Map<String, Object>) parsedObject4;
        String apiVersionSecret = (String) SecretMap.get("apiVersion");
        String kindSecret = (String) SecretMap.get("kind");
        Map<String, Object> metadataSecret = (Map<String, Object>) SecretMap.get("metadata");
        String nameSecret = (String) metadataSecret.get("name");
        Secret.Metadata metadataSecret1 = new Secret.Metadata();
        metadataSecret1.setName(nameSecret);
        String typeSecret = (String) SecretMap.get("type");
        Map<String, Object> dataSecret = (Map<String, Object>) SecretMap.get("data");
        String mongo_user = (String) dataSecret.get("mongo-user");
        String mongo_password = (String) dataSecret.get("mongo-password");
        Secret.Data dataSecret1 = new Secret.Data();
        dataSecret1.setMongoUser(mongo_user);
        dataSecret1.setMongoPassword(mongo_password);

        Secret secret = new Secret();
        secret.setApiVersion(apiVersionSecret);
        secret.setKind(kindSecret);
        secret.setType(typeSecret);
        secret.setMetadata(metadataSecret1);
        secret.setData(dataSecret1);

//        Document query = new Document("name", "MERN-app");
//        Document update = new Document("$set", new Document("secret", secret));
//
//        return getCollection().updateOne(query, update)
//                .onItem().ignore().andContinueWithNull();
        return secret;
    }

    public Uni<Void> addVnfd() throws IOException{
        String metadataFilePath = "D:\\20232\\TOSCA\\metadata\\tosca.yaml";

        String metadataContent = new String(Files.readAllBytes(Paths.get(metadataFilePath)));

        Yaml metadata3 = new Yaml();
        Object parsedObject = metadata3.load(metadataContent);
        Map<String, Object> metadataMap1 = (Map<String, Object>) parsedObject;
        String name4 = (String) metadataMap1.get("name");
        Double versionValue = (Double) metadataMap1.get("version");
        String version = String.valueOf(versionValue);
        String createdBy = (String) metadataMap1.get("created-by");
        String provider = (String) metadataMap1.get("provider");

        String definitionsFilePath = "D:\\20232\\TOSCA\\definitions\\definitions.yaml";

        String definitionsContent = new String(Files.readAllBytes(Paths.get(definitionsFilePath)));

        Yaml metadata4 = new Yaml();
        Object parsedObject1 = metadata4.load(definitionsContent);
        List<Map<String, Object>> definitionsMap = (List<Map<String, Object>>) parsedObject1;
        for(Map<String, Object> definition : definitionsMap) {
            int min_number_of_instance = (int) definition.get("min_number_of_instance");
            int max_number_of_instance = (int) definition.get("max_number_of_instance");
            String nameVdu = (String) definition.get("name");

            Vnfd.Vdu.KubernetesResource kubernetesResource = new Vnfd.Vdu.KubernetesResource();
            Deployment deployment = (Deployment) this.addDeployment();
            Service service = (Service) this.addService();
            Service service1 = (Service) this.addService2();
            ConfigMap configMap = (ConfigMap) this.addConfigMap();
            Secret secret = (Secret) this.addSecret();
            Deployment deployment1 = (Deployment) this.addDeployment2();
            List<Deployment> deployments = new ArrayList<>();
            deployments.add(deployment);
            deployments.add(deployment1);
            List<Service> services = new ArrayList<>();
            services.add(service);
            services.add(service1);
            kubernetesResource.setService(services);
            kubernetesResource.setConfigMap(configMap);
            kubernetesResource.setSecret(secret);
            kubernetesResource.setDeployment(deployments);

            Vnfd.Vdu vdu = new Vnfd.Vdu();
            vdu.setName(nameVdu);
            vdu.setMinNumberOfInstance(min_number_of_instance);
            vdu.setMaxNumberOfInstance(max_number_of_instance);
            vdu.setKubernetesResource(kubernetesResource);

            Vnfd vnfd = new Vnfd();
            vnfd.setVersion(version);
            vnfd.setName(name4);
            vnfd.setCreatedBy(createdBy);
            vnfd.setProvider(provider);
            vnfd.setVdu(vdu);
            Document document = new Document();
            document.append("name", name4);
            document.append("version", version);
            document.append("createdBy", createdBy);
            document.append("provider", provider);
            document.append("vdu", vdu);
            return getCollection().insertOne(document)
                    .onItem().ignore().andContinueWithNull();
        }
        return null;
    }


    public Uni<Deployment> getMongoDeployment() throws IOException {
        String mongoDeploymentFilePath = "D:\\20232\\TOSCA\\k8s_resources\\mongo_deployment.yaml";

        String mongoDeploymentContent = new String(Files.readAllBytes(Paths.get(mongoDeploymentFilePath)));

        Yaml MongoDeploymentdata = new Yaml();
        Object parsedObject = MongoDeploymentdata.load(mongoDeploymentContent);

        if (parsedObject instanceof Map) {
            Map<String, Object> MongoDeploymentMap = (Map<String, Object>) parsedObject;
            String apiVersion = (String) MongoDeploymentMap.get("apiVersion");
            String kind = (String) MongoDeploymentMap.get("kind");
            Map<String, Object> MetadataMap = (Map<String, Object>) parsedObject;
            String name = (String) MetadataMap.get("name");
            Deployment.Metadata metadata = new Deployment.Metadata();
            metadata.setName(name);
            Map<String, Object> LabelsMap = (Map<String, Object>) MetadataMap;
            String app = (String) LabelsMap.get("app");
            Deployment.Metadata.Labels labels = new Deployment.Metadata.Labels();
            labels.setApp(app);
            metadata.setLabels(labels);

            Map<String, Object> SpecMap = (Map<String, Object>) parsedObject;
            String replicas = (String) SpecMap.get("replicas");
            Map<String, Object> selectorMap = (Map<String, Object>) SpecMap;
            Map<String, Object> matchLabelsMap = (Map<String, Object>) selectorMap;
            String app1 = (String) matchLabelsMap.get("app");
            Deployment.Spec.Selector.MatchLabels matchLabels = new Deployment.Spec.Selector.MatchLabels();
            matchLabels.setApp(app1);
            Deployment.Spec.Selector selector = new Deployment.Spec.Selector();
            selector.setMatchLabels(matchLabels);
            Map<String, Object> templateMap = (Map<String, Object>) SpecMap;
            Map<String, Object> metadataMap = (Map<String, Object>) templateMap;
            Map<String, Object> labelsMap = (Map<String, Object>) metadataMap;
            String app2 = (String) labelsMap.get("app");
            Deployment.Spec.Template.Metadata.Labels labels1 = new Deployment.Spec.Template.Metadata.Labels();
            labels1.setApp(app2);
            Deployment.Spec.Template.Metadata metadata1 = new Deployment.Spec.Template.Metadata();
            metadata1.setLabels(labels1);
            Deployment.Spec.Template template = new Deployment.Spec.Template();
            template.setMetadata(metadata1);

            Map<String, Object> specifitionMap = (Map<String, Object>) templateMap;
            Map<String, Object> containersMap = (Map<String, Object>) specifitionMap;
            String name1 = (String) specifitionMap.get("- name");
            String image = (String) specifitionMap.get("image");
            Map<String, Object> portMap = (Map<String, Object>) containersMap;
            String containerPort = (String) portMap.get("- containerPort");
            Deployment.Spec.Template.Specification.Container.Ports ports = new Deployment.Spec.Template.Specification.Container.Ports();
            ports.setContainerPort(containerPort);
            Deployment.Spec.Template.Specification.Container container = new Deployment.Spec.Template.Specification.Container();
            container.setName(name1);
            container.setImage(image);
            container.setPorts(ports);
            Deployment.Spec.Template.Specification specification = new Deployment.Spec.Template.Specification();
            specification.setContainers(container);

            template.setSpecification(specification);

            Deployment.Spec spec = new Deployment.Spec();
            spec.setReplicas(replicas);
            spec.setSelector(selector);
            spec.setTemplate(template);

            Deployment deployment = new Deployment();
            deployment.setApiVersion(apiVersion);
            deployment.setKind(kind);
            deployment.setMetadata(metadata);
            deployment.setSpec(spec);

        }

        return null;
    }
    @Inject
    ReactiveMongoClient mongoClient;
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnfd");
    }
}
