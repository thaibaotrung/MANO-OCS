package org.acme.Model;

import org.mongodb.morphia.annotations.Property;

public class Service {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private Spec spec;

    public static class Metadata {
        private String name;

        // Getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Spec {
        private Selector selector;
        private Ports ports;

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class Selector{
            private String app;

            public String getApp() {
                return app;
            }

            public void setApp(String app) {
                this.app = app;
            }
        }
        public static class Ports{
            String protocol;
            int port;
            int targetPort;

            int NodePort;

            public int getNodePort() {
                return NodePort;
            }

            public void setNodePort(int nodePort) {
                NodePort = nodePort;
            }

            public String getProtocol() {
                return protocol;
            }

            public void setProtocol(String protocol) {
                this.protocol = protocol;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getTargetPort() {
                return targetPort;
            }

            public void setTargetPort(int targetPort) {
                this.targetPort = targetPort;
            }
        }

        public Selector getSelector() {
            return selector;
        }

        public void setSelector(Selector selector) {
            this.selector = selector;
        }

        public Ports getPorts() {
            return ports;
        }

        public void setPorts(Ports ports) {
            this.ports = ports;
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

