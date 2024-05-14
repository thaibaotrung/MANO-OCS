package org.acme.Model;

import java.util.List;

public class Instance {
     private List<VNFc> vnfcList;
     private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VNFc> getVnfcList() {
        return vnfcList;
    }

    public void setVnfcList(List<VNFc> vnfcList) {
        this.vnfcList = vnfcList;
    }
}
