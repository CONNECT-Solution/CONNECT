package gov.hhs.fha.nhinc.hiem.entity.notify;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityNotificationConsumerSecured", portName = "EntityNotificationConsumerSecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitynotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/EntityNotifySecured/EntityNotificationConsumerSecured.wsdl")
@HandlerChain(file = "EntityNotifySoapHeaderHandler.xml")
public class EntityNotifySecured
{
    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notifySubscribersOfDocument(gov.hhs.fha.nhinc.common.nhinccommonentity.NotifySubscribersOfDocumentRequestSecuredType notifySubscribersOfDocumentRequestSecured)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType notify(org.oasis_open.docs.wsn.b_2.Notify notifyRequestSecured)
    {
        return new EntityNotifyServiceImpl().notify(notifyRequestSecured, context);
    }

}
