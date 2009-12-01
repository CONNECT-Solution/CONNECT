/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.ejb.Stateless;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterNotificationProducer", portName = "AdapterNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagement", wsdlLocation = "META-INF/wsdl/AdapterSubcriptionManagement/AdapterSubscriptionManagement.wsdl")
@Stateless
public class AdapterSubcriptionManagement implements AdapterNotificationProducerPortType {
    private static Log log = LogFactory.getLog(AdapterSubcriptionManagement.class);

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeRequestType subscribeRequest) {
        SubscribeResponse response = new SubscribeResponse();

        log.info("Received Subscribe Request: " + subscribeRequest);

        
        return response;
        
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeDocumentRequestType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Deprecated");
    }

}
