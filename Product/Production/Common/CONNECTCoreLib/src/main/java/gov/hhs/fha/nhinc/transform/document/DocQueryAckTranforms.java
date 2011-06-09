/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.transform.document;

import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author richard.ettema
 */
public class DocQueryAckTranforms {

    /**
     * Create acknowledgement message based on specific data values.
     *
     * @param status
     * @param errCode
     * @param errText
     * @return
     */
    public static DocQueryAcknowledgementType createAckMessage(String status, String errCode, String errText) {
        DocQueryAcknowledgementType ackMsg = new DocQueryAcknowledgementType();

        RegistryResponseType regResp = new RegistryResponseType();
        ackMsg.setMessage(regResp);

        // Assign the Ack status
        regResp.setStatus(status);

        // If an error is defined, create a registry error
        if (errCode != null && errText != null) {
            RegistryError regError = new RegistryError();
            regError.setErrorCode(errCode);
            regError.setCodeContext(errText);
            regError.setSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
            RegistryErrorList errorList = new RegistryErrorList();
            errorList.getRegistryError().add(regError);
            regResp.setRegistryErrorList(errorList);
        }

        return ackMsg;
    }

}
