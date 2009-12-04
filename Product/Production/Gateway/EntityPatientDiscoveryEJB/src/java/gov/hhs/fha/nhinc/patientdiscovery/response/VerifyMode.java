/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;

/**
 *
 * @author dunnek
 */
public class VerifyMode implements ResponseMode{
    private Log log = null;
    
    public VerifyMode() {
        super();
        log = createLogger();
    }
    public PRPAIN201306UV02 processResponse(ResponseParams params)
    {
        log.debug("begin processResponse");
        PRPAIN201306UV02 response = params.response;
        WebServiceContext context = params.context;
        PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        PRPAIN201306UV02 result = response;

        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        if(patientExistsLocally(requestMsg, assertion))
        {
            log.debug("patient exists locally, adding correlation");
            new TrustMode().processResponse(params);
        }
        else
        {
            log.warn("Patient does not exist locally, correlation not added");
        }
        return result;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected boolean patientExistsLocally(PRPAIN201305UV02 query, AssertionType assertion)
    {
        return (queryMpi(query, assertion) != null);
    }
    private PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            log.info("Sending query to the Secured MPI");
            queryResults =
                    mpiProxy.findCandidates(query, assertion);

        } else {
            log.error("MPI Request is null");
            queryResults =
                    null;
        }

        return queryResults;
    }
}
