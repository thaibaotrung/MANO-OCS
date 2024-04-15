package org.acme.Model;

import org.mongodb.morphia.annotations.Property;

public class Secret {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private String type;
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
        @Property("mongo-user")
        private String mongoUser;

        @Property("mongo-password")
        private String mongoPassword;

        // Getters and setters

        public String getMongoUser() {
            return mongoUser;
        }

        public void setMongoUser(String mongoUser) {
            this.mongoUser = mongoUser;
        }

        public String getMongoPassword() {
            return mongoPassword;
        }

        public void setMongoPassword(String mongoPassword) {
            this.mongoPassword = mongoPassword;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
