/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.admingui.model.fhir;

/**
 * Model class for FHIR Conformance Resource data
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
