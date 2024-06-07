package org.acme.Model;

import org.bson.types.ObjectId;

import java.util.List;

public class VNF_Instance {
    private ObjectId _id;
    private  String name;
    private String status;
    private String vnfdName;

    private String description;
    private List<VNFc> vnfcList;

    private List<LCMOPCC> listLcmOpcc;

    public List<LCMOPCC> getListLcmOpcc() {
        return listLcmOpcc;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void setListLcmOpcc(List<LCMOPCC> listLcmOpcc) {
        this.listLcmOpcc = listLcmOpcc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVnfdName() {
        return vnfdName;
    }

    public void setVnfdName(String vnfdName) {
        this.vnfdName = vnfdName;
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
