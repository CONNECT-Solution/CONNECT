/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.connectionmanager.persistence;
import java.io.FileReader;
import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author dunnek
 */
public class HibernateAccessor
{
    private static Log log = LogFactory.getLog(HibernateAccessor.class);
    private static final String CRLF = System.getProperty("line.separator");
    private static String m_sPropertyFileDir = "";
    private static final String m_sFailedPathMessage = "Failed to load Hibernate Directory.  " +
            "Unable to determine the path to the configuration files.  " +
            "Please make sure that the runtime nhinc.properties.dir system property is set to the absolute location " +
            "of your CONNECT configuration files.";
    private static boolean m_bFailedToLoadEnvVar = false;

    static
    {
        //
        // This is a duplication of what is in the PropertyAccessor class and should
        // be replaced by a call to the PropertyAccessor.getPropertyFileLocation().
        // The reason we can't do that right now is because NhincCommonLib, which is where
        // PropertyAccessor lives, is dependent on NhincCommonDAO, which is where this code lives.
        //
        m_sPropertyFileDir = System.getProperty("nhinc.properties.dir");

        if(m_sPropertyFileDir == null) {
            log.warn("The runtime property nhinc.properties.dir is not set!!!  " +
                    "Looking for the environment variable NHINC_PROPERTIES_DIR as a fall back.  " +
                    "Please set the runtime nhinc.properties.dir system property in your configuration files.");
            m_sPropertyFileDir = System.getenv("NHINC_PROPERTIES_DIR");
            if(m_sPropertyFileDir == null) {
                m_bFailedToLoadEnvVar = true;
                log.error(m_sFailedPathMessage);
            }
        }

        //
        // Set it up so that we always have a "/" at the end - in case
        //------------------------------------------------------------
        if (m_sPropertyFileDir.endsWith(File.separator) == false) {
            m_sPropertyFileDir = m_sPropertyFileDir + File.separator;
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
            throw new Exception(m_sFailedPathMessage);
        }

        return true;            // We only get here if the env variable was loaded.
    }
}
