/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy.AdapterComponentDocSubmissionResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy.AdapterComponentDocSubmissionResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.service.WebServiceHelper;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseSecuredImpl
{

    private static final Log logger = LogFactory.getLog(AdapterXDRResponseSecuredImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context)
    {

        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");
        WebServiceHelper oHelper = new WebServiceHelper();
        XDRAcknowledgementType ack = null;
        try
        {
            if (body != null)
            {
                ack = (XDRAcknowledgementType) oHelper.invokeSecureDeferredResponseWebService(this, this.getClass(), "callAdapterComponentXDR", body, context);
            } else
            {
                logger.error("Failed to call the web orchestration (" + this.getClass() + ".callAdapterComponentXDR).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            logger.error("Failed to call the web orchestration (" + this.getClass() + ".callAdapterComponentXDR).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return ack;
    }

    protected Log getLogger()
    {
        return logger;
    }

    public XDRAcknowledgementType callAdapterComponentXDR(RegistryResponseType body, AssertionType assertion)
    {

        getLogger().debug("Calling AdapterComponentXDRImpl");

        XDRAcknowledgementType ack = null;

        AdapterComponentDocSubmissionResponseProxyObjectFactory oFactory = new AdapterComponentDocSubmissionResponseProxyObjectFactory();
        AdapterComponentDocSubmissionResponseProxy oProxy = oFactory.getAdapterComponentDocSubmissionResponseProxy();
        ack = oProxy.provideAndRegisterDocumentSetBResponse(body, assertion);

        return ack;

    }

}
