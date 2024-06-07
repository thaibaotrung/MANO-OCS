package org.acme.Model;

import java.util.Date;

public class LCMOPCC {
    private String operation;
    private String operationState;
    private Date startTime;
    private VNFc affectVnfc;
    private String description;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperationState() {
        return operationState;
    }

    public void setOperationState(String operationState) {
        this.operationState = operationState;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public VNFc getAffectVnfc() {
        return affectVnfc;
    }

    public void setAffectVnfc(VNFc affectVnfc) {
        this.affectVnfc = affectVnfc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
