/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.service.port;

import gov.hhs.fha.nhinc.messaging.service.BaseServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SoapResponseServiceEndpointDecorator;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * @author bhumphrey
 * @param <T>
 * 
 */
public abstract class CachingCXFServicePortBuilder<T> extends CXFServicePortBuilder<T> {

    /**
     * Returns the cache map of the ports.
     * 
     * @return the cache map
     */
    protected abstract Map<Class<?>, Object> getCache();

    /**
     * Constructor.
     * 
     * @param portDescriptor
     */
    public CachingCXFServicePortBuilder(ServicePortDescriptor<T> portDescriptor) {
        super(portDescriptor);
    }

    /**
     * Configures the given port for all configuration that is not invocation specific. Since the port used is going to
     * be cached and reused, this method should configure everything on the port that is only needed to be applied once.
     * All non-thread safe configuration should be done here.
     * 
     * The following configuration to the port are considered not thread safe as they will be shared between all 
     * threads:
     * 1. Interceptors
     * 2. Modifying the HTTP Conduit
     * 
     * The following configuration to the port are considered local and are thread safe if configured properly: 
     * 1. The request context
     * 2. HTTPClientPolicy but only through the request context
     * 
     * @param port The port to be configured
     */
    protected void configurePort(T port) {
        ServiceEndpoint<T> serviceEndpoint = new BaseServiceEndpoint<T>(port);
        serviceEndpoint = new SoapResponseServiceEndpointDecorator<T>(serviceEndpoint);
        serviceEndpoint.configure();
    }

    /**
     * Returns a new port or one from the cache. The port will be configured for thread safety and reuse.
     */
    @SuppressWarnings("unchecked")
    public synchronized T createPort() {
        T port = (T) getCache().get(serviceEndpointClass);
        if (port == null) {
            port = super.createPort();
            ((BindingProvider) port).getRequestContext().put("thread.local.request.context", Boolean.TRUE);

            configurePort(port);

            getCache().put(serviceEndpointClass, port);
        }
        ((BindingProvider) port).getRequestContext().put(HTTPClientPolicy.class.getName(), new HTTPClientPolicy());

        return port;
    }
}
