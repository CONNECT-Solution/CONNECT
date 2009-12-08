package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

//import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.netbeans.xml.schema.endpoint.EPR;

/**
 *
 * The Helper Class is used to load & Maintain the service mapping table
 * Provides the central mapping of the service.
 *
 * @author Jerry Goodnough
 */
public class ServiceEndpointCache
{

    private static Log log = LogFactory.getLog(ServiceEndpointCache.class);
    private static final String CRLF = System.getProperty("line.separator");
    private static final String BOS_XML_FILE_NAME =
                                "adapterServicesMappings.xml";
    private static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
    private static String m_sPropertyFileDir = "";
    private static String m_sBOSXMLfileDir = "";
    private static String m_sFileSeparator =
                          System.getProperty("file.separator");
    private static final String m_sFailedEnvVarMessage =
                                "Unable to access environment variable: NHINC_PROPERTIES_DIR.";
    private static boolean m_bFailedToLoadEnvVar = false;
    private static ServiceInfo m_siServices;
    private static HashMap<String, ServiceMapping> m_hServiceByName =
                                                   new HashMap<String, ServiceMapping>();       // Array of connection information
    private static HashMap<String, ServiceMapping> m_hServiceByURL =
                                                   new HashMap<String, ServiceMapping>();       // Array of connection information
    private static boolean m_bServiceLoaded = false;      // TRUE if the properties have been loaded
    private static long m_lServiceFileLastModified = 0;
    private static long m_lLastCheckTime = 0;
    private static long CHECK_LIMIT = 60000L;   //Number of Milliseconds between cache checks
    

    static
    {
        String sValue = System.getenv(NHINC_PROPERTIES_DIR);

        if ((sValue != null) && (sValue.length() > 0))
        {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\")))
            {
                m_sPropertyFileDir = sValue;
            }
            else
            {
                m_sPropertyFileDir = sValue + m_sFileSeparator;
            }

            m_sBOSXMLfileDir = m_sPropertyFileDir + BOS_XML_FILE_NAME;

        }
        else
        {
            log.error(m_sFailedEnvVarMessage);
            m_bFailedToLoadEnvVar = true;
        }
    }

    /**
     * Helper function to read the servce points in from the file
     * @throws gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceLookupException
     */
    public static void loadServiceEndPoints() throws ServiceLookupException
    {
        if (m_bFailedToLoadEnvVar)
        {
            throw new ServiceLookupException(m_sFailedEnvVarMessage);
        }

        String sXml = "";

        try
        {
            sXml = StringUtil.readTextFile(m_sBOSXMLfileDir);
            File fServiceFile = new File(m_sBOSXMLfileDir);
            if (fServiceFile.exists())
            {
                m_lServiceFileLastModified = fServiceFile.lastModified();
            }
            else
            {
                m_lServiceFileLastModified = 0;
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to read from file: '" +
                                   m_sBOSXMLfileDir + "'.  Error: " + e.
                    getMessage();
            log.error(sErrorMessage);
            throw new ServiceLookupException(sErrorMessage, e);
        }

        log.debug("Setting Service cache to be: " + CRLF + sXml);

        m_siServices = ServiceInfoXML.deserialize(sXml);
        //If we get here the Deserialization

        //Refresh the cache
        m_hServiceByName.clear();
        m_hServiceByURL.clear();

        //OK we now have the new Service
        Iterator<ServiceMapping> itr = m_siServices.m_ServiceList.listIterator();
        while (itr.hasNext())
        {
            ServiceMapping siCur = itr.next();
            String srvName = siCur.getServiceName();
            if (srvName != null && !srvName.isEmpty())
            {
                m_hServiceByName.put(srvName, siCur);
            }
            String srvURL = siCur.getServiceURL();
            if (srvURL != null && !srvURL.isEmpty())
            {
                m_hServiceByURL.put(srvURL, siCur);
            }
        }
        m_bServiceLoaded = true;
    }

    /**
     * This method simply checks to see if the cache is loaded.  If it is not, then
     * it is loaded as a byproduct of calling this method. It might also result
     * the cache being reloaded if it hase been changed
     *
     */
    private static void checkLoaded()
            throws ServiceLookupException
    {
        if (!m_bServiceLoaded)
        {
            forceCacheRefresh();
        }
        // One last check for refreshing...
        //---------------------------------
        refreshIfExpired();
    }

    /**
     * Cause the Cache to be refreashed if the file has been modified
     * For santity and performance this check is limited by CHECK_LIMIT
     * which defines the time in milliseconds that must occure between
     * checking the file for modification. Initialy this value is set at
     * 60 seconds. This value is a prime candidate for the properties file.
     *
     * @throws gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceLookupException
     */
    private static void refreshIfExpired()
            throws ServiceLookupException
    {

        //Sanity Check to avoid File I/O
        // Will only check to see if the file has changed base on
        long currentTime = System.currentTimeMillis();
        if (currentTime - m_lLastCheckTime > CHECK_LIMIT)
        {
            m_lLastCheckTime = currentTime;
            long lInternalLastModified = 0;


            // Find out our refrhes timing from the properties file.
            //-------------------------------------------------------
            try
            {
                File fInternalFile = new File(m_sBOSXMLfileDir);
                if (fInternalFile.exists())
                {
                    lInternalLastModified = fInternalFile.lastModified();
                }
            }
            catch (Exception e)
            {
                // Assume a refresh is required...  But log a message.
                //----------------------------------------------------
                String sErrorMessage = "Failed to retrieve last modified dates on the BOS Endpoint Service XML files." +
                                       "Error: " + e.getMessage();
                log.warn(sErrorMessage, e);
            }

            //Check to see if the

            if (lInternalLastModified != m_lServiceFileLastModified)
            {
                forceCacheRefresh();
                log.info(
                        "Service Endpoint cache was refreshed based on last modified time stamp change.");
            }
        }
    }

    /** Force the Cache to reload
     * @throws gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceLookupException
     */
    public static void forceCacheRefresh() throws ServiceLookupException
    {
        //Mark as not loaded to insure that timeing checks will not take effect 
        //until a sucessful load occurs
        m_bServiceLoaded=false;
        //Load the Endpoints
        loadServiceEndPoints();
    }

    /**
     * Lookup an endpoint by Service name
     *
     * @param name The service name.
     * @return The EPOR for service name or null
     * @throws gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceLookupException
     */
    public static EPR findEndpointByServiceName(String name) throws ServiceLookupException
    {
        EPR eprOut = null;

        //Refreach Cache if we need to
        checkLoaded();

        ServiceMapping smOut = m_hServiceByName.get(name);

        if (smOut != null)
        {
            eprOut = smOut.getEndPoint();
        }
        return eprOut;
    }

    /**
     * Lookup an endpoint by URL
     *
     * @param url The url
     * @return The EPR for the URL or null
     * @throws gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceLookupException
     */
    public static EPR findEndpointByServiceURL(String url) throws ServiceLookupException
    {
        EPR eprOut = null;
        //Refreach Cache if we need to
        checkLoaded();

        ServiceMapping smOut = m_hServiceByURL.get(url);

        if (smOut != null)
        {
            eprOut = smOut.getEndPoint();
        }
        return eprOut;
    }
}
