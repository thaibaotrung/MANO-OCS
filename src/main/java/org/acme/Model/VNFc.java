package org.acme.Model;

public class VNFc {
    private String id;
    private String name;
    private String state;
    private String ip;
    private String nodeName;

    private String lcmState;
    private int numberofinstance;

    public String getLcmState() {
        return lcmState;
    }

    public void setLcmState(String lcmState) {
        this.lcmState = lcmState;
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getNumberofinstance() {
        return numberofinstance;
    }

    public void setNumberofinstance(int numberofinstance) {
        this.numberofinstance = numberofinstance;
    }
}
