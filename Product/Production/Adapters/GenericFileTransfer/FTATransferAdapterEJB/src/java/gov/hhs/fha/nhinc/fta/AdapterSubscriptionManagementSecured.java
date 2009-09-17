/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterNotificationProducerSecured", portName = "AdapterNotificationProducerPortSecuredSoap11", endpointInterface = "gov.hhs.fha.nhinc.adaptersubscriptionmanagementsecured.AdapterNotificationProducerPortSecuredType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagementsecured", wsdlLocation = "META-INF/wsdl/AdapterSubscriptionManagementSecured/AdapterSubscriptionManagementSecured.wsdl")
@Stateless
public class AdapterSubscriptionManagementSecured implements AdapterNotificationProducerPortSecuredType {
    private static Log log = LogFactory.getLog(AdapterSubcriptionManagement.class);
    
    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.oasis_open.docs.wsn.b_2.Subscribe subscribeRequest) {
        SubscribeResponse response = new SubscribeResponse();

        log.info("Received Subscribe Request: " + subscribeRequest);

        
        return response;
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeDocumentRequestSecuredType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
