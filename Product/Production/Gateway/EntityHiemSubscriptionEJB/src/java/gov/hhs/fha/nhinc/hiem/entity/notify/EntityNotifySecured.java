/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.notify;

import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerSecuredPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityNotificationConsumerSecured", portName = "EntityNotificationConsumerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitynotificationconsumer", wsdlLocation = "META-INF/wsdl/EntityNotifySecured/EntityNotificationConsumerSecured.wsdl")
@Stateless
public class EntityNotifySecured implements EntityNotificationConsumerSecuredPortType {
    @Resource
    private WebServiceContext context;
    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestSecuredType notifyRequestSecured) {
        EntityNotifyServiceImpl impl = new EntityNotifyServiceImpl();
        return impl.notify(notifyRequestSecured, context);
    }

}
