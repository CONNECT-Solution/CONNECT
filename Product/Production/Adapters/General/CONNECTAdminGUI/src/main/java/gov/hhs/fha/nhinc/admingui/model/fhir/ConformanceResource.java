/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.model.fhir;

/**
 *
 * @author jassmit
 */
public class ConformanceResource {
    
    private String name;
    private boolean supportingRead = false;
    private boolean supportingVRead = false;
    private boolean supportingDelete = false;
    private boolean supportingCreate = false;
    private boolean supportingUpdate = false;
    private boolean supportingValidate = false;
    private boolean supportingSearchType = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSupportingRead() {
        return supportingRead;
    }

    public void setSupportingRead(boolean supportingRead) {
        this.supportingRead = supportingRead;
    }

    public boolean isSupportingVRead() {
        return supportingVRead;
    }

    public void setSupportingVRead(boolean supportingVRead) {
        this.supportingVRead = supportingVRead;
    }

    public boolean isSupportingDelete() {
        return supportingDelete;
    }

    public void setSupportingDelete(boolean supportingDelete) {
        this.supportingDelete = supportingDelete;
    }

    public boolean isSupportingCreate() {
        return supportingCreate;
    }

    public void setSupportingCreate(boolean supportingCreate) {
        this.supportingCreate = supportingCreate;
    }

    public boolean isSupportingUpdate() {
        return supportingUpdate;
    }

    public void setSupportingUpdate(boolean supportingUpdate) {
        this.supportingUpdate = supportingUpdate;
    }

    public boolean isSupportingValidate() {
        return supportingValidate;
    }

    public void setSupportingValidate(boolean supportingValidate) {
        this.supportingValidate = supportingValidate;
    }

    public boolean isSupportingSearchType() {
        return supportingSearchType;
    }

    public void setSupportingSearchType(boolean supportingSearchType) {
        this.supportingSearchType = supportingSearchType;
    }
    
}
