package gov.hhs.fha.nhinc.configuration;
import gov.hhs.fha.nhinc.configuration.jmx.Configuration;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 
 */

/**
 * @author msw
 * 
 */
public class InitServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7738036165392946446L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
        super.init();

        String enableJMX = System.getProperty("org.connectopensource.enablejmx");
        if ("true".equalsIgnoreCase(enableJMX)) {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = null;
            try {
                name = new ObjectName("org.connectopensource.mbeans:type=Configuration");
                Configuration mbean = new Configuration();
                mbs.registerMBean(mbean, name);
            } catch (MalformedObjectNameException e) {
                e.printStackTrace();
                throw new ServletException(e);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
                throw new ServletException(e);
            } catch (MBeanRegistrationException e) {
                e.printStackTrace();
                throw new ServletException(e);
            } catch (NotCompliantMBeanException e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
        }
    }
}
