package gov.hhs.fha.nhinc.transform.marshallers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class is used to manage JAXB contexts.  Once loaded a
 * JAXB context is kept in static memory so that it does not need
 * to be loaded again when used again.
 *
 * @author Les Westberg
 *
 */
public class JAXBContextHandler
{
    private static Log log = LogFactory.getLog(JAXBContextHandler.class);

    // Contexts that are being managed.  The name will be the context.
    //----------------------------------------------------------------
    private static Hashtable<String, JAXBContext> hContexts = new Hashtable<String, JAXBContext>();

    /**
     * This method returns the JAXB context
     *
     * @param sContextName The name of the context.  (i.e. "org.hl7.v3").
     * @return The JAXB context for that context name.
     * @throws javax.xml.bind.JAXBException
     */
    public JAXBContext getJAXBContext(String sContextName)
        throws javax.xml.bind.JAXBException
    {
        if ((sContextName != null) &&
            (sContextName.length() > 0))
        {
            JAXBContext oContext = hContexts.get(sContextName);
            if (oContext == null)
            {
                log.debug("Loading JAXB Context for '" + sContextName + "'.");
                oContext = JAXBContext.newInstance(sContextName);
                hContexts.put(sContextName, oContext);
                log.debug("Finished loading JAXB Context for '" + sContextName + "'.");
            }
            else
            {
                log.debug("Reusing JAXB Context for '" + sContextName + "'.");
            }
            return oContext;
        }
        else
        {
            log.debug("Request for JAXB Context without a valid name.");
            return null;
        }
    }

    /**
     * This method returns the JAXB context
     *
     * @param oClass1 The object factory class for the first context.
     * @param oClass2 The object factory class for the second context.
     * @return The JAXB context for that context name.
     * @throws javax.xml.bind.JAXBException
     */
    public JAXBContext getJAXBContext(Class oClass1, Class oClass2)
        throws javax.xml.bind.JAXBException
    {
        JAXBContext oContext = null;
        String sContextName = "";
        if ((oClass1 != null) && (oClass2 != null))
        {
            sContextName = oClass1.getPackage().getName() + "_" + oClass2.getPackage().getName();
        }
        else if (oClass1 != null)
        {
            sContextName = oClass1.getPackage().getName();
        }
        else if (oClass2 != null)
        {
            sContextName = oClass2.getPackage().getName();
        }

        if ((sContextName != null) &&
            (sContextName.length() > 0))
        {
            oContext = hContexts.get(sContextName);
            if (oContext == null)
            {
                log.debug("Loading JAXB Context for '" + sContextName + "'.");
                if ((oClass1 != null) && (oClass2 != null))
                {
                    oContext = JAXBContext.newInstance(oClass1, oClass2);
                }
                else if (oClass1 != null)
                {
                    oContext = JAXBContext.newInstance(oClass1);
                }
                else if (oClass2 != null)
                {
                    oContext = JAXBContext.newInstance(oClass2);
                }
                log.debug("Finished loading JAXB Context for '" + sContextName + "'.");

                if (oContext != null)
                {
                    hContexts.put(sContextName, oContext);
                }
            }
            else
            {
                log.debug("Reusing JAXB Context for '" + sContextName + "'.");
            }
        }
        else
        {
            log.debug("Request for JAXB Context without object factory classes.");
        }

        return oContext;
    }

    /**
     * This method returns the JAXB context
     *
     * @param oClass The object factory class for the first context.
     * @return The JAXB context for that context name.
     * @throws javax.xml.bind.JAXBException
     */
    public JAXBContext getJAXBContext(Class oClass)
        throws javax.xml.bind.JAXBException
    {
        JAXBContext oContext = null;
        String sContextName = "";
        if (oClass != null)
        {
            sContextName = oClass.getPackage().getName();
        }

        if ((sContextName != null) &&
            (sContextName.length() > 0))
        {
            oContext = hContexts.get(sContextName);
            if (oContext == null)
            {
                log.debug("Loading JAXB Context for '" + sContextName + "'.");
                oContext = JAXBContext.newInstance(oClass);
                log.debug("Finished loading JAXB Context for '" + sContextName + "'.");

                if (oContext != null)
                {
                    hContexts.put(sContextName, oContext);
                }
            }
            else
            {
                log.debug("Reusing JAXB Context for '" + sContextName + "'.");
            }
        }
        else
        {
            log.debug("Request for JAXB Context without object factory classes.");
        }

        return oContext;
    }

}
