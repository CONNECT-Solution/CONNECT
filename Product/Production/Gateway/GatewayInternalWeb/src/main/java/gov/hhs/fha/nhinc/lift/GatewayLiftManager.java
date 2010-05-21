/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;

/**
 *
 * @author westberg
 */
@WebService(serviceName = "NhincComponentLiftManager", portName = "NhincComponentLiftManagerPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentliftmanager.NhincComponentLiftManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentliftmanager", wsdlLocation = "WEB-INF/wsdl/GatewayLiftManager/NhincComponentLiftManager.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class GatewayLiftManager {

    public StartLiftTransactionResponseType startLiftTransaction(StartLiftTransactionRequestType startLiftTransactionRequest)
    {
        StartLiftTransactionResponseType oResponse = new StartLiftTransactionResponseType();
        oResponse.setStatus("SUCCESS");
        return oResponse;
    }

    public CompleteLiftTransactionResponseType completeLiftTransaction(CompleteLiftTransactionRequestType completeLiftTransactionRequest)
    {
        CompleteLiftTransactionResponseType oResponse = new CompleteLiftTransactionResponseType();
        oResponse.setStatus("SUCCESS");
        return oResponse;
    }

    public FailedLiftTransactionResponseType failedLiftTransaction(FailedLiftTransactionRequestType failedLiftTransactionRequest)
    {
        FailedLiftTransactionResponseType oResponse = new FailedLiftTransactionResponseType();
        oResponse.setStatus("SUCCESS");
        return oResponse;
    }

}
