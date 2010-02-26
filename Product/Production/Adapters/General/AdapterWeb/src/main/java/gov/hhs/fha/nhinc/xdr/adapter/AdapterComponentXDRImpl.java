/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.nhinc.xdr.routing.RoutingObjectFactory;
import gov.hhs.nhinc.xdr.routing.XDRRouting;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterComponentXDRImpl {
    private static Log log = null;
    public AdapterComponentXDRImpl()
    {
        log = createLogger();
    }
    public RegistryResponseType provideAndRegisterDocumentSetb(AdapterProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Begin provideAndRegisterDocumentSetb()");
        XDRHelper helper = new XDRHelper();
        RegistryErrorList errorList = helper.validateDocumentMetaData(body.getProvideAndRegisterDocumentSetRequest());

        RegistryResponseType result = null;

        if(errorList.getHighestSeverity().equals(helper.XDS_ERROR_SEVERITY_ERROR))
        {
            result = helper.createErrorResponse(errorList);
        }
        else
        {
            log.info(" Request contained " + body.getProvideAndRegisterDocumentSetRequest().getDocument().size() + " documents.");
            log.info(" Request Id: " + body.getProvideAndRegisterDocumentSetRequest().getSubmitObjectsRequest().getId());

            List<String> recips = helper.getIntendedRecepients(body.getProvideAndRegisterDocumentSetRequest());

            if(recips != null)
            {
                List<String>  xdrBeans = helper.getRoutingBeans(recips);
                RoutingObjectFactory factory = new RoutingObjectFactory();

                for(String bean : xdrBeans)
                {
                    log.debug("Bean name = " + bean);
                    XDRRouting proxy = factory.getNhinXDRRouting(bean);
                    result = proxy.provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), body.getAssertion());
                }
            }
            else
            {
                log.debug("No beans to forward the message to");
                result = helper.createPositiveAck();
            }

            
        }
        return result;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private String getRoutingBeanName(List<String> recips)
    {
        String result = "";

        return RoutingObjectFactory.BEAN_REFERENCE_IMPLEMENTATION;
    }
    private XDRRouting getRoutingBean(String beanName)
    {
        XDRRouting result = null;

        result = new RoutingObjectFactory().getNhinXDRRouting(beanName);

        return result;
    }

    
}
