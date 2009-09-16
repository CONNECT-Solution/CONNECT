package gov.hhs.fha.nhinc.hiem.entity.notify;

import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityNotificationConsumerSecured", portName = "EntityNotificationConsumerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitynotificationconsumer", wsdlLocation = "META-INF/wsdl/EntityNotifySecured/EntityNotificationConsumerSecured.wsdl")
@Stateless
public class EntityNotifySecured implements EntityNotificationConsumerSecuredPortType
{

    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured)
    {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(Notify notifyRequest)
    {
        return new EntityNotifyServiceImpl().notify(notifyRequest, context);
    }
}
