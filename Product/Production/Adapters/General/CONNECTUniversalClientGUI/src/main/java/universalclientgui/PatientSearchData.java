/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientSearchData implements Serializable
{

    private ArrayList<String> columns;
    private String assigningAuthorityID;

    public PatientSearchData()
    {
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
