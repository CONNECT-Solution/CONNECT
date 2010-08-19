package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocRetrieveProxyNoOpImpl implements PassthruDocRetrieveProxy
{
    private Log log = null;

    public PassthruDocRetrieveProxyNoOpImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin PassthruDocRetrieveProxyNoOpImpl.respondingGatewayCrossGatewayRetrieve");
        return new RetrieveDocumentSetResponseType();
    }

}
