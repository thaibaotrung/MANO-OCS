package org.acme.Model;

import java.util.List;

public class Deployment {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private Spec spec;

    public static class Metadata {
        private String name;
        private Labels labels;

        public static class Labels {
            private String app;

            // Getters and setters

            public String getApp() {
                return app;
            }

            public void setApp(String app) {
                this.app = app;
            }
        }

        // Getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Labels getLabels() {
            return labels;
        }

        public void setLabels(Labels labels) {
            this.labels = labels;
        }
    }

    public static class Spec {
        private String replicas;
        private Selector selector;
        private Template template;

        public static class Selector {
            private MatchLabels matchLabels;

            public static class MatchLabels {
                private String app;

                // Getters and setters

                public String getApp() {
                    return app;
                }

                public void setApp(String app) {
                    this.app = app;
                }
            }

            // Getters and setters

            public MatchLabels getMatchLabels() {
                return matchLabels;
            }

            public void setMatchLabels(MatchLabels matchLabels) {
                this.matchLabels = matchLabels;
            }
        }

        public static class Template {
            private Metadata metadata;
            private Specification specification;

            public static class Metadata {
                private Deployment.Spec.Template.Metadata.Labels labels;

                public static class Labels{
                    String app;

                    public String getApp() {
                        return app;
                    }

                    public void setApp(String app) {
                        this.app = app;
                    }
                }

                // Getters and setters


                public Deployment.Spec.Template.Metadata.Labels getLabels() {
                    return labels;
                }

                public void setLabels(Deployment.Spec.Template.Metadata.Labels labels) {
                    this.labels = labels;
                }
            }

            public static class Specification {
                private Container containers;

                public static class Container {
                    private String name;
                    private String image;
                    private Ports ports;
                    private List<Env> env;

                    public static class Ports {
                        private String containerPort;

                        // Getters and setters

                        public String getContainerPort() {
                            return containerPort;
                        }

                        public void setContainerPort(String containerPort) {
                            this.containerPort = containerPort;
                        }
                    }

                    public static class Env {
                        private String name;
                        private ValueFrom valueFrom;

                        public static class ValueFrom {
                            private SecretKeyRef secretKeyRef;

                            public static class SecretKeyRef {
                                private String name;
                                private String key;

                                // Getters and setters

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getKey() {
                                    return key;
                                }

                                public void setKey(String key) {
                                    this.key = key;
                                }
                            }

                            // Getters and setters

                            public SecretKeyRef getSecretKeyRef() {
                                return secretKeyRef;
                            }

                            public void setSecretKeyRef(SecretKeyRef secretKeyRef) {
                                this.secretKeyRef = secretKeyRef;
                            }
                        }

                        // Getters and setters

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public ValueFrom getValueFrom() {
                            return valueFrom;
                        }

                        public void setValueFrom(ValueFrom valueFrom) {
                            this.valueFrom = valueFrom;
                        }
                    }

                    // Getters and setters

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getImage() {
                        return image;
                    }

                    public void setImage(String image) {
                        this.image = image;
                    }

                    public Ports getPorts() {
                        return ports;
                    }

                    public void setPorts(Ports ports) {
                        this.ports = ports;
                    }

                    public List<Env> getEnv() {
                        return env;
                    }

                    public void setEnv(List<Env> env) {
                        this.env = env;
                    }
                }

                // Getters and setters


                public Container getContainers() {
                    return containers;
                }

                public void setContainers(Container containers) {
                    this.containers = containers;
                }
            }

            // Getters and setters

            public Metadata getMetadata() {
                return metadata;
            }

            public void setMetadata(Metadata metadata) {
                this.metadata = metadata;
            }

            public Specification getSpecification() {
                return specification;
            }

            public void setSpecification(Specification specification) {
                this.specification = specification;
            }
        }

        // Getters and setters


        public String getReplicas() {
            return replicas;
        }

        public void setReplicas(String replicas) {
            this.replicas = replicas;
        }

        public Selector getSelector() {
            return selector;
        }

        public void setSelector(Selector selector) {
            this.selector = selector;
        }

        public Template getTemplate() {
            return template;
        }

        public void setTemplate(Template template) {
            this.template = template;
        }
    }

    // Getters and setters


    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }
}
