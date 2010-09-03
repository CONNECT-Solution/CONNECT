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

package gov.hhs.fha.nhinc.adapterauditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterAuditQueryNoOpImpl implements AdapterAuditQueryProxy {

    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request) {
        return new FindAuditEventsResponseType();
    }

}
