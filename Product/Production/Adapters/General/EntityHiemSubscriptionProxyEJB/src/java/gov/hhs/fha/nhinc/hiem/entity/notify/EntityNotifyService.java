/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.notify;

import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityNotificationConsumer", portName = "EntityNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitynotificationconsumer", wsdlLocation = "META-INF/wsdl/EntityNotifyService/EntityNotificationConsumer.wsdl")
@Stateless
public class EntityNotifyService implements EntityNotificationConsumerPortType {
    @Resource
    private WebServiceContext context;
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
        return new EntityNotifyServiceImpl().notify(notifyRequest, context);
    }

}
