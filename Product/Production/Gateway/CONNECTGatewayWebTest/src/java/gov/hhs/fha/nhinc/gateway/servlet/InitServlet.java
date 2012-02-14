package gov.hhs.fha.nhinc.gateway.servlet;

import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Started on webapplication init, creates the main ExecutorService and CamelContext instances Note the following: 1.
 * Main ExecutorService creates a new thread pool of size specified on construction, independent/in addition to
 * glassfish thread pool(s) set in domain.xml. 2. ExecutorService automatically handles any thread death condition and
 * creates a new thread in this case
 * 
 * 3. Also creates a second largeJobExecutor with a fixed size thread pool (largeJobExecutor is used for TaskExecutors
 * that get a callable list of size comporable to the size of the main ExecutorService)
 * 
 * Allows override of timeoutValues values from configs
 * 
 * @author paul.eftis
 */
public class InitServlet extends HttpServlet {

    private Log log = LogFactory.getLog(InitServlet.class);

    private static ExecutorService executor = null;
    private static ExecutorService largeJobExecutor = null;

    @Override
    @SuppressWarnings("static-access")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.debug("InitServlet start...");
        executor = Executors.newFixedThreadPool(ExecutorServiceHelper.getInstance().getExecutorPoolSize());
        largeJobExecutor = Executors.newFixedThreadPool(ExecutorServiceHelper.getInstance()
                .getLargeJobExecutorPoolSize());
    }

    public static ExecutorService getExecutorService() {
        return executor;
    }

    public static ExecutorService getLargeJobExecutorService() {
        return largeJobExecutor;
    }

    @Override
    public void destroy() {
        log.debug("InitServlet shutdown stopping executor(s)....");
        if (executor != null) {
            try {
                executor.shutdown();
            } catch (Exception e) {
            }
        }
        if (largeJobExecutor != null) {
            try {
                largeJobExecutor.shutdown();
            } catch (Exception e) {
            }
        }
    }

}
