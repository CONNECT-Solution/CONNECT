/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.connectionmanager.persistence;
import java.io.FileReader;
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Date;


import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author dunnek
 */
public class HibernateAccessor
{
    private static Log log = LogFactory.getLog(HibernateAccessor.class);
    private static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
    private static final String CRLF = System.getProperty("line.separator");
    private static String m_sPropertyFileDir = "";
    private static boolean m_bFailedToLoadEnvVar = false;

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
                String sFileSeparator = System.getProperty("file.separator");
                m_sPropertyFileDir = sValue + sFileSeparator;
            }
        }
        else
        {
            log.error("Failed to load Hibernate Directory");
            m_bFailedToLoadEnvVar = true;
        }
    }
    public static File getHibernateFile(String hibernateFileName)
           throws Exception
    {
        String sFileSeparator = System.getProperty("file.separator");
        checkEnvVarSet();

        File result = new File(m_sPropertyFileDir + "hibernate" +
                sFileSeparator + hibernateFileName );

        if(result == null)
        {
            throw new Exception("Unable to locate " +
                    hibernateFileName);
        }
        return result;
    }
    private static boolean checkEnvVarSet()
        throws Exception
    {
        if (m_bFailedToLoadEnvVar)
        {
            throw new Exception("Failed to load Hibernate Directory");
        }

        return true;            // We only get here if the env variable was loaded.
    }
}
