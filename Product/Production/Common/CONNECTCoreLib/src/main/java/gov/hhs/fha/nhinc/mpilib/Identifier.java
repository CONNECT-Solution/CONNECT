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
package gov.hhs.fha.nhinc.mpilib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class Identifier implements java.io.Serializable {
    private static Log log = LogFactory.getLog(Identifier.class);
    static final long serialVersionUID = -4713959967816233116L;

    public Identifier() {
    }

    public Identifier(String id, String organizationId) 
    {
        this.id = id;
        this.organizationId = organizationId;
    }
    
    private String id = "";
    private String organizationId = "";

    public String getOrganizationId()
    {
        return organizationId;
    }
    public void setOrganizationId(String id)
    {
        organizationId = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
}
