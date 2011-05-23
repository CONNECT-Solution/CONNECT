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
import org.uddi.api_v3.FindQualifiers;
import org.uddi.api_v3.Name;

/**
 *
 * @author richard.ettema
 */
public class UDDIFindBusinessProxyJuddiV3Impl extends UDDIFindBusinessProxy {

    private static Log log = LogFactory.getLog(UDDIFindBusinessProxyJuddiV3Impl.class);

    @Override
    public BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException {
        log.debug("Using jUDDI V3 Implementation for UDDI Business Info Service");

        BusinessList oBusinessList = null;

        try {
            // load relevant property info
            loadProperties();

            UDDIInquiryPortType oPort = getUDDIInquiryWebService();

            // Make the call...
            //-----------------
            FindBusiness oSearchParams = new FindBusiness();

            Name findName = new Name();
            FindQualifiers qualifiers = new FindQualifiers();

            findName.setValue("%");
            qualifiers.getFindQualifier().add("approximateMatch");

            oSearchParams.getName().add(findName);
            oSearchParams.setFindQualifiers(qualifiers);

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
