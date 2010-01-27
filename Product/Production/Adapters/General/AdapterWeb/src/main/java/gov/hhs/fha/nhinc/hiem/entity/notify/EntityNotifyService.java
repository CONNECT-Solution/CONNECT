package gov.hhs.fha.nhinc.hiem.entity.notify;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityNotificationConsumer", portName = "EntityNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitynotificationconsumer", wsdlLocation = "WEB-INF/wsdl/EntityNotifyService/EntityNotificationConsumer.wsdl")
public class EntityNotifyService {

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifySubscribersOfDocumentRequestType notifySubscribersOfDocumentRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfCdcBioPackage(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifySubscribersOfCdcBioPackageRequestType notifySubscribersOfCdcBioPackageRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType notifyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
