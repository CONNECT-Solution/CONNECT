/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.connectmgr.uddi.proxy;

import gov.hhs.fha.nhinc.nhin_uddi_api_v3.UDDIInquiryPortType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.FindBusiness;

/**
 *
 * @author richard.ettema
 */
public class UDDIFindBusinessProxyHPImpl extends UDDIFindBusinessProxy {

    private static Log log = LogFactory.getLog(UDDIFindBusinessProxyHPImpl.class);

    /**
     *
     * @return list of businesses from UDDI
     * @throws UDDIFindBusinessException
     */
    public BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException {
        log.debug("Using HP Implementation for UDDI Business Info Service");

        BusinessList oBusinessList = null;

        try {
            // load relevant property info
            loadProperties();

            UDDIInquiryPortType oPort = getUDDIInquiryWebService();

            // Make the call...
            //-----------------
            FindBusiness oSearchParams = new FindBusiness();
            oSearchParams.setMaxRows(100);
            oBusinessList = oPort.findBusiness(oSearchParams);

        } catch (Exception e) {
            String sErrorMessage = "Failed to call 'find_business' web service on the NHIN UDDI server.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIFindBusinessException(sErrorMessage, e);
        }

        return oBusinessList;
    }

}