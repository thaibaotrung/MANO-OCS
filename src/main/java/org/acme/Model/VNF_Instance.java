package org.acme.Model;

import java.util.List;

public class VNF_Instance {
    private String id;
    private  String name;
    private String state;
    private String vnfdId;

    private String description;
    private List<VNFc> vnfcList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVnfdId() {
        return vnfdId;
    }

    public void setVnfdId(String vnfdId) {
        this.vnfdId = vnfdId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VNFc> getVnfcList() {
        return vnfcList;
    }

    public void setVnfcList(List<VNFc> vnfcList) {
        this.vnfcList = vnfcList;
    }
}
