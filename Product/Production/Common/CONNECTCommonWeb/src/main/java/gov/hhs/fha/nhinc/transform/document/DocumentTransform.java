/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.document;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentDocumentTransformService", portName = "NhincComponentDocumentTransformPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentdocumenttransform.NhincComponentDocumentTransformPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentdocumenttransform", wsdlLocation = "WEB-INF/wsdl/DocumentTransform/NhincComponentDocumentTransform.wsdl")
public class DocumentTransform {

    public gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdResponseType replaceAdhocQueryPatientId(gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdRequestType replaceAdhocQueryPatientIdRequest) {
        return new DocumentTransformHelper().replaceAdhocQueryPatientId(replaceAdhocQueryPatientIdRequest);
    }

}
