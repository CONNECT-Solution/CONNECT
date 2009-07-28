/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.adapternotificationconsumer.AdapterNotificationConsumerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterNotificationConsumer", portName = "AdapterNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapternotificationconsumer.AdapterNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapternotificationconsumer", wsdlLocation = "META-INF/wsdl/AdapterFTAService/AdapterNotificationConsumer.wsdl")
@Stateless
public class AdapterFTAService implements AdapterNotificationConsumerPortType {

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifyRequestType notifyRequest) {
        return NotificationImpl.processNotify(notifyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifySubscribersOfDocumentRequestType notifySubscribersOfDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifySubscribersOfCdcBioPackageRequestType notifySubscribersOfCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
