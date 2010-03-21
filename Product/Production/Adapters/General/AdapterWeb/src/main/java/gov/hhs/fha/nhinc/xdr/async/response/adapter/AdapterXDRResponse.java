/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import javax.jws.WebService;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterXDRResponse_Service", portName = "AdapterXDRResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrresponse", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponse/AdapterXDRResponse.wsdl")
public class AdapterXDRResponse {

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(AdapterRegistryResponseType body) {
        //TODO implement this method
        return new AdapterXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body);

    }

}
