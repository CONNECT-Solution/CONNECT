package gov.hhs.fha.nhinc.auditrepository;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AuditRepositoryManagerSecuredService", portName = "AuditRepositoryManagerSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository", wsdlLocation = "WEB-INF/wsdl/AuditRepository/NhincComponentAuditRepositorySecured.wsdl")
public class AuditRepository
{
    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(AuditRepository.class);

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType queryAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest)
    {
        log.debug("Entering AuditRepository.FindCommunitiesAndAuditEventsResponseType(...)");
        gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType oResponse = AuditRepositoryHelper.queryAuditEvents(queryAuditEventsRequest, context);;
        log.debug("Exiting AuditRepository.FindCommunitiesAndAuditEventsResponseType(...)");
        return oResponse;
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEvent(gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType logEventRequest)
    {
        log.debug("Entering AuditRepository.logEvent(...)");
        gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType oResponse = AuditRepositoryHelper.logEvent(logEventRequest, context);
        log.debug("Exiting AuditRepository.logEvent(...)");
        return oResponse;
    }

}
