package gov.hhs.fha.nhinc.connectmgr.uddi;

import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentUDDIUpdateManager", portName = "NhincComponentUDDIUpdateManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentuddiupdatemanager.NhincComponentUDDIUpdateManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentuddiupdatemanager", wsdlLocation = "WEB-INF/wsdl/UDDIUpdateManager/NhincComponentUDDIUpdateManager.wsdl")
public class UDDIUpdateManager {
private static Log log = LogFactory.getLog(UDDIUpdateManager.class);

    /**
     * Default constructor.
     */
    public UDDIUpdateManager()
    {
        try
        {
            UDDITimer.startTimer();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to start UDDIUpdateManager's timer.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }
    }

    public gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshResponseType forceRefreshFileFromUDDIServer(gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshRequestType uddiUpdateManagerForceRefreshRequest) {
        return UDDIUpdateManagerHelper.forceRefreshFileFromUDDIServer(uddiUpdateManagerForceRefreshRequest);
    }

}
