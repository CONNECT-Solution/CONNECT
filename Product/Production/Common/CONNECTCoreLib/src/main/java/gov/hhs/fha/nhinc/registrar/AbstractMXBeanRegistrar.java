/**
 *
 */
package gov.hhs.fha.nhinc.registrar;

import gov.hhs.fha.nhinc.configuration.jmx.PassthruMXBeanRegistry;
import gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMXBeanRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMXBeanRegistrar.class);

    protected  Set<WebServicesMXBean> beans = new HashSet<WebServicesMXBean>();

    /**
     * This method should be annotated with @PostConstruct in the concrete class as annotations are not inherited
     *
     * This method will register the beans from the PassthruBeanMXRegistry
     */
    public void init() {
        LOG.info("Constuction of {} service beans has begun...", this.getClass());
        beans = getWebServiceMXBean();
        for(WebServicesMXBean bean : beans) {
            LOG.info("Registering {} from {}", bean.getClass().getName(), Thread.currentThread().getStackTrace()[0].getClassName());
            PassthruMXBeanRegistry.getInstance().registerWebServiceMXBean(bean);
        }

    }

    /**
     * This method should be annotated with @PreDestroy in the concrete class as annotations are not inherited
     *
     * This method will unregister the beans from the PassthruBeanMXRegistry
     */
    public void destroy() {
        LOG.info("Destruction of {} has begun...", this.getClass());
        for(WebServicesMXBean bean : beans) {
            if (PassthruMXBeanRegistry.getInstance().unregisterBean(bean)) {
                LOG.info("Unregistering {} from destruction of {} bean", bean.getClass().getName(), Thread.currentThread().getStackTrace()[0].getClassName());
            } else {
                LOG.error("Could not remove MX Bean {}! Was it not registered in the creation of {}?", bean.getClass().getName(), Thread.currentThread().getStackTrace()[0].getClassName() );
            }

        }

    }

    public abstract Set<WebServicesMXBean> getWebServiceMXBean();
}
