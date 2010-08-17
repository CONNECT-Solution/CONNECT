/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrieve.entity.EntityDocRetrieveOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
/**
 *
 * @author dunnek
 */
public class EntityDocRetrieveJavaImpl implements EntityDocRetrieveProxy{
    private static org.apache.commons.logging.Log log = null;
    
    public EntityDocRetrieveJavaImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion, NhinTargetCommunitiesType targets) {
        return getEntityImpl().respondingGatewayCrossGatewayRetrieve(body, assertion, targets);
    }
    protected EntityDocRetrieveOrchImpl getEntityImpl()
    {
        return new EntityDocRetrieveOrchImpl();
    }

}
