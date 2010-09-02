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

package gov.hhs.fha.nhinc.hiem.processor.faults;

import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import org.oasis_open.docs.wsn.b_2.*;
import org.oasis_open.docs.wsn.bw_2.*;
import org.oasis_open.docs.wsrf.r_2.ResourceUnknownFaultType;

/**
 *
 * @author rayj
 */
public class SubscriptionManagerSoapFaultFactory {
    public UnableToDestroySubscriptionFault getGenericProcessingExceptionFault(Exception ex) {
        UnableToDestroySubscriptionFaultType faultInfo = new UnableToDestroySubscriptionFaultType();
        UnableToDestroySubscriptionFault fault = new UnableToDestroySubscriptionFault("Failed to process unsubscribe [" + ex.getMessage() + "]", faultInfo);
        return fault;
    }
    public UnableToDestroySubscriptionFault getFailedToRemoveSubscriptionFault(Exception ex) {
        UnableToDestroySubscriptionFaultType faultInfo = new UnableToDestroySubscriptionFaultType();
        UnableToDestroySubscriptionFault fault = new UnableToDestroySubscriptionFault("Unable to remove subscription [" + ex.getMessage() + "]", faultInfo);
        return fault;
    }
    public ResourceUnknownFault getUnableToFindSubscriptionFault() {
        ResourceUnknownFaultType faultInfo = new ResourceUnknownFaultType();
        ResourceUnknownFault fault = new ResourceUnknownFault("Unknown subscription", faultInfo);
        return fault;
    }
    public ResourceUnknownFault getErrorDuringSubscriptionRetrieveFault(SubscriptionRepositoryException ex) {
        ResourceUnknownFaultType faultInfo = new ResourceUnknownFaultType();
        ResourceUnknownFault fault = new ResourceUnknownFault("Error occurred while retrieving subscription [" + ex.getMessage() + "]", faultInfo);
        return fault;
    }
}
