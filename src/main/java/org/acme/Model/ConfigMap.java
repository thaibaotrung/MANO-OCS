package org.acme.Model;

public class ConfigMap {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private Data data;

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

    public static class Data {
        private String mongoUrl;

        // Getters and setters

        public String getMongoUrl() {
            return mongoUrl;
        }

        public void setMongoUrl(String mongoUrl) {
            this.mongoUrl = mongoUrl;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

