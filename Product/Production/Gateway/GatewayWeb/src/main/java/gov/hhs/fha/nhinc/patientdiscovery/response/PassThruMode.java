/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.PRPAIN201306UV02;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author dunnek
 */
public class PassThruMode implements ResponseMode{
    private Log log = null;
    
    public PassThruMode() {
        super();
        log = createLogger();
    }
    public PRPAIN201306UV02 processResponse(ResponseParams params)
    {
        //In pass through mode, no additional processing is done by the Entity.
        //201306 is returned directly to the agency. 
        log.debug("begin processResponse");
        return params.response;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
