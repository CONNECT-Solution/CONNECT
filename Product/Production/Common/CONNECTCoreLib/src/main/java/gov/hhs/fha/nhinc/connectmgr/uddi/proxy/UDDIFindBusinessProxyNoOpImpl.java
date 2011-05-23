/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.connectmgr.uddi.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessList;

/**
 *
 * @author richard.ettema
 */
public class UDDIFindBusinessProxyNoOpImpl extends UDDIFindBusinessProxy {

    private static Log log = LogFactory.getLog(UDDIFindBusinessProxyNoOpImpl.class);

    public BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException {
        log.debug("Using NoOp Implementation for UDDI Business Info Service");
        return new BusinessList();
    }

}
