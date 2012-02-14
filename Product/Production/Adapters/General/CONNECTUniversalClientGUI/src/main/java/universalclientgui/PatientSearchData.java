/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package universalclientgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientSearchData implements Serializable {

    private ArrayList<String> columns;
    private String assigningAuthorityID;

    public PatientSearchData() {
    }

    public PatientSearchData(List<String> cols) {
        columns = new ArrayList<String>(cols);
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public String getAssigningAuthorityID() {
        return assigningAuthorityID;
    }

    public void setAssigningAuthorityID(String assigningAuthorityID) {
        this.assigningAuthorityID = assigningAuthorityID;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public String getLastName() {
        return columns.get(0);
    }

    public void setLastName(String col) {
        columns.set(0, col);
    }

    public String getFirstName() {
        return columns.get(1);
    }

    public void setFirstName(String col) {
        columns.set(1, col);
    }

    public String getPatientId() {
        return columns.get(2);
    }

    public void setPatientId(String col) {
        columns.set(2, col);
    }

    public String getSsn() {
        return columns.get(3);
    }

    public void setSsn(String col) {
        columns.set(3, col);
    }

    public String getDob() {
        return columns.get(4);
    }

    public void setDob(String col) {
        columns.set(4, col);
    }

    public String getGender() {
        return columns.get(5);
    }

    public void setGender(String col) {
        columns.set(5, col);
    }

}
