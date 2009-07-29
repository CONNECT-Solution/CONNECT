/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptersubscription;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterNotificationProducer", portName = "AdapterNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagement", wsdlLocation = "META-INF/wsdl/AdapterSubscriptionService/AdapterSubscriptionManagement.wsdl")
@Stateless
public class AdapterSubscriptionService implements AdapterNotificationProducerPortType {

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeRequestType subscribeRequest) {
        return AdapterSubscriptionHelper.subscribe(subscribeRequest);
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeDocument(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeDocumentRequestType subscribeDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribeCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
