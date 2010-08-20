package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

class EntityPatientDiscoveryDeferredRequestImpl
{

    private Log log = null;

    public EntityPatientDiscoveryDeferredRequestImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceHelper createWebServiceHelper()
    {
        return new WebServiceHelper();
    }

    MCCIIN000002UV01 processPatientDiscoveryAsyncRequestSecured(RespondingGatewayPRPAIN201305UV02SecuredRequestType request, WebServiceContext context)
    {
        log.info("Begin processPatientDiscoveryAsyncRequestSecured(RespondingGatewayPRPAIN201305UV02SecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityPatientDiscoveryDeferredRequestOrchImpl implOrch = createEntityPatientDiscoveryDeferredRequestOrchImpl();
        MCCIIN000002UV01 response = null;

        try
        {
            if (request != null)
            {
                response = (MCCIIN000002UV01) oHelper.invokeSecureWebService(implOrch, implOrch.getClass(), "processPatientDiscoveryAsyncReq", request, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    MCCIIN000002UV01 processPatientDiscoveryAsyncRequestUnsecured(RespondingGatewayPRPAIN201305UV02RequestType request, WebServiceContext context)
    {
        log.info("Begin processPatientDiscoveryAsyncRequestUnsecured(RespondingGatewayPRPAIN201305UV02RequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityPatientDiscoveryDeferredRequestOrchImpl implOrch = createEntityPatientDiscoveryDeferredRequestOrchImpl();
        MCCIIN000002UV01 response = null;

        try
        {
            if (request != null)
            {
                RespondingGatewayPRPAIN201305UV02SecuredRequestType secureRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
                secureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
                secureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
                AssertionType assertIn = request.getAssertion();
                response = (MCCIIN000002UV01) oHelper.invokeUnsecureWebService(implOrch, implOrch.getClass(), "processPatientDiscoveryAsyncReq", assertIn, secureRequest, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".processPatientDiscoveryAsyncReq).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    private EntityPatientDiscoveryDeferredRequestOrchImpl createEntityPatientDiscoveryDeferredRequestOrchImpl()
    {
        return new EntityPatientDiscoveryDeferredRequestOrchImpl();
    }
}
