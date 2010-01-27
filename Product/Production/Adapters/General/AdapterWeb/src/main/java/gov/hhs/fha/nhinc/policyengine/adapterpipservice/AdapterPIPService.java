package gov.hhs.fha.nhinc.policyengine.adapterpipservice;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPIP", portName = "AdapterPIPPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpip", wsdlLocation = "WEB-INF/wsdl/AdapterPIPService/AdapterPIP.wsdl")
public class AdapterPIPService {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType retrievePtConsentByPtIdRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType retrievePtConsentByPtDocIdRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType storePtConsent(gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType storePtConsentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
