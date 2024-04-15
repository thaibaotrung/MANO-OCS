package org.acme.Model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

public class Vnfd {
    private String name;
    private String version;
    private String createdBy;

    private String provider;
    private Vdu vdu;

    public static class Vdu{
        @Id
        private ObjectId id;
        private String name;
        private int minNumberOfInstance;
        private int maxNumberOfInstance;
        private KubernetesResource kubernetesResource;

        public static class KubernetesResource{
            private List<Deployment> deployment;
            private List<Service> service;
            private ConfigMap configMap;
            private Secret secret;

            public List<Deployment> getDeployment() {
                return deployment;
            }

            public void setDeployment(List<Deployment> deployment) {
                this.deployment = deployment;
            }

            public List<Service> getService() {
                return service;
            }

            public void setService(List<Service> service) {
                this.service = service;
            }

            public ConfigMap getConfigMap() {
                return configMap;
            }

            public void setConfigMap(ConfigMap configMap) {
                this.configMap = configMap;
            }

            public Secret getSecret() {
                return secret;
            }

            public void setSecret(Secret secret) {
                this.secret = secret;
            }
        }

        public ObjectId getId() {
            return id;
        }

        public void setId(ObjectId id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMinNumberOfInstance() {
            return minNumberOfInstance;
        }

        public void setMinNumberOfInstance(int minNumberOfInstance) {
            this.minNumberOfInstance = minNumberOfInstance;
        }

        public int getMaxNumberOfInstance() {
            return maxNumberOfInstance;
        }



        public void setMaxNumberOfInstance(int maxNumberOfInstance) {
            this.maxNumberOfInstance = maxNumberOfInstance;
        }

        public KubernetesResource getKubernetesResource() {
            return kubernetesResource;
        }

        public void setKubernetesResource(KubernetesResource kubernetesResource) {
            this.kubernetesResource = kubernetesResource;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Vdu getVdu() {
        return vdu;
    }

    public void setVdu(Vdu vdu) {
        this.vdu = vdu;
    }
}
