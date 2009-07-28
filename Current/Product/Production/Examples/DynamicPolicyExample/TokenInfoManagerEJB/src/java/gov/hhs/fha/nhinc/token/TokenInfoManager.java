package gov.hhs.fha.nhinc.token;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinccomponenttokeninfomanager.NhincComponentTokenInfoManagerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class exposes the Saml Token Manager Services.  These services are used 
 * to write information needed in the Saml token to a file and to retrieve those 
 * values.
 *
 * @author Victoria Vickers
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentTokenInfoManagerService", portName = "NhincComponentTokenInfoManagerPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponenttokeninfomanager.NhincComponentTokenInfoManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponenttokeninfomanager", wsdlLocation = "META-INF/wsdl/TokenInfoManager/NhincComponentTokenInfoManager.wsdl")
@Stateless
public class TokenInfoManager implements NhincComponentTokenInfoManagerPortType
{
    private static Log log = LogFactory.getLog(TokenInfoManager.class);

    /**
     * This method wraps the POJO which retrieves the extracted information
     * from the SAML Token and creates a new Assertion object.
     * @param request Currently not used
     * @return The new Assertion object containing the extracted token information
     */
    public gov.hhs.fha.nhinc.common.nhinccommon.AssertionType retrieveInfoOperation(gov.hhs.fha.nhinc.common.nhinccommon.TokenRetrieveInfoType request)
    {
        log.debug("TokenInfoManager.retrieveInfoOperation() -- Begin");
        AssertionType assertInfo = null;
        InternalTokenMgr mgr = new InternalTokenMgr();
        try
        {
            assertInfo = mgr.retrieveInfoOperation();
        }
        catch (Exception e)
        {
            log.error("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        log.debug("TokenInfoManager.retrieveInfoOperation() -- End");
        return assertInfo;
    }

    /**
     * This method wraps the POJO which stores the information needed to create
     * the SAML Token.
     * @param infoIn Composite object enclosing the assertion information as 
     * well as values set by the bpel to provide action and resource names.
     */
    public void storeInfoOperation(gov.hhs.fha.nhinc.common.nhinccommon.TokenCreationInfoType infoIn)
    {
        log.debug("TokenInfoManager.storeInfoOperation() -- Begin");
        InternalTokenMgr mgr = new InternalTokenMgr();
        try
        {
            if (infoIn != null)
            {
                mgr.storeInfoOperation(infoIn.getAssertion(), infoIn.getActionName(), infoIn.getResourceName());
            }
            else
            {
                log.info("TokenInfoManager.storeInfoOperation expects an non-null input");
            }
        }
        catch (Exception e)
        {
            log.error("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        log.debug("TokenInfoManager.storeInfoOperation() -- End");
    }
}
