package gov.hhs.fha.nhinc.adapters.general.adapterpolicyenginetransform.adapterpolicyengine;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineTransform", portName = "AdapterPolicyEngineTransformPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyenginetransform.AdapterPolicyEngineTransformPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyenginetransform", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineTransform/AdapterPolicyEngineTransform.wsdl")
public class AdapterPolicyEngineTransform {
    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineTransform/AdapterPolicyEngineTransform.wsdl")
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToCppAQRResponseType transformXACMLRequestToCppAQR(gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToCppAQRRequestType transformXACMLRequestToCppAQRRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformAQRToCppRDSRResponseType transformAQRToCppRDSR(gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformAQRToCppRDSRRequestType transformAQRToCppRDSRRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPatientOptInResponseType checkPatientOptIn(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPatientOptInRequestType checkPatientOptInRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToAQRForPatientIdResponseType transformXACMLRequestToAQRForPatientId(gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToAQRForPatientIdRequestType transformXACMLRequestToAQRForPatientIdRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLResponseType transformPatientIdAQRToCppXACML(gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLRequestType transformPatientIdAQRToCppXACMLRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
