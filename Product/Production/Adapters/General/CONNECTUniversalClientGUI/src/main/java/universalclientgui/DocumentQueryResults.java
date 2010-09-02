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
import java.util.Date;
import java.util.List;

/**
 *
 * @author patlollav
 */
public class DocumentQueryResults
{
    List<DocumentInformation> documents = new ArrayList<DocumentInformation>();

    public List<DocumentInformation> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentInformation> documents) {
        this.documents = documents;
    }
    
}
