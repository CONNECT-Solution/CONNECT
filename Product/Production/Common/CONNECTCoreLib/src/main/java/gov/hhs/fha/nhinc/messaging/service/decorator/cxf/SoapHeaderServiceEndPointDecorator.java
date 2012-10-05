/**
 * @author achidamb
 *
 */
/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;




/**
 * @author achidamb
 * @param <T>
 *
 */
public class SoapHeaderServiceEndPointDecorator<T> extends ServiceEndpointDecorator<T> {

    private String SubscriptionId = null;
    private Log log = null;

    private BindingProvider bindingProviderPort;
    
    public SoapHeaderServiceEndPointDecorator(ServiceEndpoint<T> decoratoredEndpoint,String SubscriptionId) {
     super(decoratoredEndpoint);
     this.SubscriptionId = SubscriptionId;
     this.bindingProviderPort = (BindingProvider) decoratedEndpoint.getPort();
    }
    
    @Override
    public void configure() {
        super.configure();
        List<Header> headers = new ArrayList<Header>();
        Header SoapHeader;
        try {
            SoapHeader = new Header(new QName("http://www.hhs.gov/healthit/nhin", "SubscriptionId"), SubscriptionId,
                    new JAXBDataBinding(String.class));
            headers.add(SoapHeader);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        bindingProviderPort.getRequestContext().put(Header.HEADER_LIST,headers);
    }
}