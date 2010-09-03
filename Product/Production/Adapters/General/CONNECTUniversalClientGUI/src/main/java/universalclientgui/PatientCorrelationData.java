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

import java.util.ArrayList;
import java.util.List;

public class PatientCorrelationData {

    private ArrayList<String> columns;

    public PatientCorrelationData(List<String> cols) {
        columns = new ArrayList<String>(cols);
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public String getAssignAuthorityId() {
        return columns.get(0);
    }

    public void setAssignAuthorityId(String col) {
        columns.set(0, col);
    }

    public String getRemotePatientId() {
        return columns.get(1);
    }

    public void setRemotePatientId(String col) {
        columns.set(1, col);
    }

    public String getOrganizationName() {
        return columns.get(2);
    }

    public void setOrganizationName(String col) {
        columns.set(2, col);
    }

    public String getOrganizationId() {
        return columns.get(3);
    }

    public void setOrganizationId(String col) {
        columns.set(3, col);
    }
}
