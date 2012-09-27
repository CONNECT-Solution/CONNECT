/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.service.port;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

/**
 * @author bhumphrey
 * @param <T>
 *
 */
public class CachingCXFServicePortBuilder<T> extends CXFServicePortBuilder<T> {

    private static Map<Class<?>, Object> CACHED_PORTS = new HashMap<Class<?>, Object>();
    
    public CachingCXFServicePortBuilder(ServicePortDescriptor<T> portDescriptor) {
        super(portDescriptor);    
    }
    
    
    @SuppressWarnings("unchecked")
    public T createPort() {
        T port = (T) CACHED_PORTS.get(serviceEndpointClass);
        if (port == null) {
                   port  = super.createPort();
            ((BindingProvider) port).getRequestContext().put(
                    "thread.local.request.context", Boolean.TRUE); 
            CACHED_PORTS.put(serviceEndpointClass, port);
        }
        return port;
    }
    
}
