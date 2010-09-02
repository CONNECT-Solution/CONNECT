/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.properties;

import java.io.FileReader;
import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;


/**
 * This class is a POJO class that is used to access properties within a property
 * file.
 *
 * @author Les Westberg
 * @version 1.0
 * @created 04-Sep-2008 1:21:31 PM
 */
public class PropertyAccessor
{
    private static Log log = LogFactory.getLog(PropertyAccessor.class);
    private static final String CACHE_REFRESH_DURATION = "CacheRefreshDuration";
    private static final String CRLF = System.getProperty("line.separator");

    // This is a hash table that contains all the properties for all of the files that
    // have been accessed.  The hashtable is keyed off the property file name.
    //---------------------------------------------------------------------------------
    private static Hashtable<String, Properties> m_hAllProps = new Hashtable<String, Properties>();

    // This hash table is used to contain the information about when the next refresh is supposed
    // to take place on the property file.  The key is the name of the property file.  The
    // value is a RefreshInfo class that contains the refresh mode and the date/time that the
    // refresh should take place.
    //------------------------------------------------------------------------------------------------------
    private static Hashtable<String, RefreshInfo> m_hNextRefresh = new Hashtable<String, RefreshInfo>();
    private static String m_sPropertyFileDirAbsolutePath = "";
    private static final String m_sFailedPathMessage = "Unable to determine the path to the configuration files.  " +
            "Please make sure that the runtime nhinc.properties.dir system property is set to the absolute location " +
            "of your CONNECT configuration files.";
    private static boolean m_bFailedToLoadPath = false;

    static
    {
        m_sPropertyFileDirAbsolutePath = System.getProperty("nhinc.properties.dir");

        if(m_sPropertyFileDirAbsolutePath == null) {
            log.warn("The runtime property nhinc.properties.dir is not set!!!  " +
                    "Looking for the environment variable NHINC_PROPERTIES_DIR as a fall back.  " +
                    "Please set the runtime nhinc.properties.dir system property in your configuration files.");
            m_sPropertyFileDirAbsolutePath = System.getenv(NhincConstants.NHINC_PROPERTIES_DIR);
            if(m_sPropertyFileDirAbsolutePath == null) {
                m_bFailedToLoadPath = true;
                log.error(m_sFailedPathMessage);
            }
        }

        //
        // Set it up so that we always have a "/" at the end - in case
        //------------------------------------------------------------
        if (m_sPropertyFileDirAbsolutePath.endsWith(File.separator) == false) {
            m_sPropertyFileDirAbsolutePath = m_sPropertyFileDirAbsolutePath + File.separator;
        }
    }


    /**
     * Default constructor.
     */
    public PropertyAccessor()
    {
    }

    /**
     * This method does a quick check to make sure that the path variables have been
     * set.  It if has not, then an exception is thrown.
     *
     * @return  True if the path variables are set.   (It never gives false, because
     *          if it is not set, it throws an exception).
     * @throws PropertyAccessException This is thrown if the path variables were not set.
     */
    public static boolean checkEnvVarSet()
        throws PropertyAccessException
    {
        if (m_bFailedToLoadPath)
        {
            throw new PropertyAccessException(m_sFailedPathMessage);
        }

        return true;            // We only get here if the env variable was loaded.
    }

    /**
     * This is a helper method that checks to see if the string is not
     * null and that it contains a value other than the empty string.  It throws
     * an exception if it is null or contains the empty string.
     *
     * @param sMethodName The name of the method that is calling this one.
     * @param sVarName The name of the variable that is being checked. (Used in the error message text.)
     * @param sValue The variable that is being checked.
     * @throws PropertyAccessException If the variable is null or empty this exception is thrown.
     */
    private static void stringIsValid(String sMethodName, String sVarName, String sValue)
        throws PropertyAccessException
    {
        if (sValue == null)
        {
            throw new PropertyAccessException("Method: " + sMethodName +
                                              ", Variable: " + sVarName +
                                              " was null and it should have a valid value.");
        }
        else if ((sValue.length() == 0) ||
                 (sValue.trim().length() == 0))
        {
            throw new PropertyAccessException("Method: " + sMethodName +
                                              ", Variable: " + sVarName +
                                              " was '' (empty string) and it should have a valid value.");
        }
    }


    /**
     * This creates a new properties class with a full copy of all of the
     * properties.
     *
     * @param oProps The property file that is to be copied.
     * @return The copy that is returned.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException This exception is thrown if
     *         it cannot load the property file for some reason.
     */
    private static Properties deepCopyProperties(Properties oProps)
        throws PropertyAccessException
    {
        Properties oRetProps = new Properties();

        Set<String> setKeys = oProps.stringPropertyNames();
        Iterator<String> iterKeys = setKeys.iterator();
        while (iterKeys.hasNext())
        {
            String sKey = iterKeys.next();
            String sValue = oProps.getProperty(sKey);
            if (sValue != null)
            {
                sValue = sValue.trim();
            }
            oRetProps.put(sKey, sValue);
        }

        return oRetProps;
    }

    /**
     * This method loads the property file and sets the refresh time.  If the property:
     * "CacheRefreshDuration" is found in the property file, then it will set it as follows:
     * If the value is "-1", then
     *
     * @param sPropertyFile The name of the property file to be loaded.
     * @param oInfo The refresh information that we already know - this may be null if this
     *              is the first time the file is being loaded.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException This exception is thrown if
     *         it cannot load the property file for some reason.
     */
    private static void loadPropertyFile(String sPropertyFile, RefreshInfo oInfo)
        throws PropertyAccessException
    {
        String sPropFilePathAndName = m_sPropertyFileDirAbsolutePath + sPropertyFile + ".properties";
        RefreshInfo oNewInfo = null;
        if (oInfo != null)
        {
            oNewInfo = oInfo;

            // Reset the refresh time...
            //--------------------------
            if (oNewInfo.m_oRefreshMode == RefreshInfo.Mode.PERIODIC)
            {
                Calendar oRefreshDate = Calendar.getInstance();
                oRefreshDate.add(Calendar.MILLISECOND, oNewInfo.m_iRefreshMilliseconds);
                oNewInfo.m_dtRefreshDate = oRefreshDate.getTime();
            }
            else if (oNewInfo.m_oRefreshMode == RefreshInfo.Mode.ALWAYS)
            {
                oNewInfo.m_dtRefreshDate = Calendar.getInstance().getTime();
            }
        }
        else
        {
            // Default Values -
            //------------------
            oNewInfo = new RefreshInfo();
            oNewInfo.m_oRefreshMode = RefreshInfo.Mode.NEVER;
            oNewInfo.m_dtRefreshDate = new Date();
            oNewInfo.m_iRefreshMilliseconds = -1;
        }

        Properties oProps = new Properties();
        FileReader frPropFile = null;
        try
        {
            File fPropFile = new File(sPropFilePathAndName);
            if (!fPropFile.exists())
            {
                throw new PropertyAccessException("Failed to open property file:'" + sPropFilePathAndName + "'.  " +
                                                  "File does not exist.");
            }
            frPropFile = new FileReader(fPropFile);
            oProps.load(frPropFile);

            // Look to see if we have a refresh property setting.  If so, update
            // the settings we have had to the new ones...
            //-------------------------------------------------------------------
            String sValue = oProps.getProperty(CACHE_REFRESH_DURATION);
            if ((sValue != null) && (sValue.length() > 0))
            {
                int iMilliseconds = -1;
                try
                {
                    iMilliseconds = Integer.parseInt(sValue.trim());
                }
                catch (Exception e1)
                {
                    log.warn("Property File:'" + sPropertyFile + "' contained an invalid '" +
                             CACHE_REFRESH_DURATION + "' value.  It is supposed to be numeric and it was not.  " +
                             "The value was '" + sValue + "'.  Treating this property file as 'refresh never'.");
                }

                // Update any refresh information that may now be in the file...
                //---------------------------------------------------------------
                if (iMilliseconds <= -1)
                {
                    oNewInfo.m_oRefreshMode = RefreshInfo.Mode.NEVER;
                    oNewInfo.m_iRefreshMilliseconds = -1;
                    oNewInfo.m_dtRefreshDate = null;
                }
                else if (iMilliseconds == 0)
                {
                    oNewInfo.m_oRefreshMode = RefreshInfo.Mode.ALWAYS;
                    oNewInfo.m_iRefreshMilliseconds = 0;
                    oNewInfo.m_dtRefreshDate = new Date();
                }
                else
                {
                    oNewInfo.m_oRefreshMode = RefreshInfo.Mode.PERIODIC;
                    oNewInfo.m_iRefreshMilliseconds = iMilliseconds;
                    Calendar oRefreshDate = Calendar.getInstance();
                    oRefreshDate.add(Calendar.MILLISECOND, iMilliseconds);
                    oNewInfo.m_dtRefreshDate = oRefreshDate.getTime();
                }
            }   // if ((sValue != null) && (sValue.length() > 0))

            // Now lets refresh the property information
            //------------------------------------------
            synchronized(m_hNextRefresh)
            {
                m_hAllProps.put(sPropertyFile, oProps);
                m_hNextRefresh.put(sPropertyFile, oNewInfo);
            }

//            if (log.isDebugEnabled())
//            {
//                String sMessage = "Loaded/Refreshed property file: " + sPropertyFile;
//                log.debug(sMessage);
//            }
        }
        catch (Exception e)
        {
            String sMessage = "Failed to load property file.  Error: " + e.getMessage();
            throw new PropertyAccessException(sMessage, e);
        }
        finally
        {
            if (frPropFile != null)
            {
                try
                {
                    frPropFile.close();
                }
                catch (Exception e1)
                {
                    log.error("Failed to close property file: '" + sPropFilePathAndName + "'", e1);
                }
            }
        }
    }

    /**
     * This method will check to see if the property file needs to be refreshed
     * and if it does, it will reload it.  Otherwise it will leave it as is.
     *
     * @param sPropertyFile The name of the property file that is being checked and
     *                      possibly loaded.
     * @throws PropertyAccessException If an error occurs during the load process,
     *                                 this exception is thrown.
     */
    private static void checkForRefreshAndLoad(String sPropertyFile)
        throws PropertyAccessException
    {
        Date dtNow = new Date();
        RefreshInfo oInfo = m_hNextRefresh.get(sPropertyFile);

        if (oInfo != null)
        {
            if ((oInfo.m_oRefreshMode == RefreshInfo.Mode.ALWAYS) ||
                ((oInfo.m_oRefreshMode == RefreshInfo.Mode.PERIODIC)) &&
                 (oInfo.m_dtRefreshDate.before(dtNow)))
            {
                loadPropertyFile(sPropertyFile, oInfo);
            }
        }
        else if (oInfo == null)
        {
            loadPropertyFile(sPropertyFile, oInfo);     // This means that this is the firat time property file has been accessed
        }
    }

    /**
     * This method returns the value of the given property that is located within the
     * given property file.   If the properties have been cached and the cache is
     * still fresh, then it will return the value from the cache.  If the properties
     * are cached, but the cache is not fresh, then the cache will be updated with the
     * current values in the properties file and then the property will be returned.
     * If the properties for that file are not cached at all, the property will be
     * retrieved from the properties file and returned.
     *
     * @param sPropertyFile    The name of the property file.  This is the name of the
     * file without a path and without the ".properties" extension.   Examples of this
     * would be "connection" or "gateway".
     * @param sPropertyName    This is the name of the property within the property
     * file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static String getProperty(String sPropertyFile, String sPropertyName)
        throws PropertyAccessException
    {
        String sReturnValue = "";

        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getProperty", "sPropertyFile", sPropertyFile);
        stringIsValid("getProperty", "sPropertyName", sPropertyName);

        checkForRefreshAndLoad(sPropertyFile);

        Properties oProps = m_hAllProps.get(sPropertyFile);

        if (oProps != null)
        {
            String sValue = oProps.getProperty(sPropertyName);
            if ((sValue != null) && (sValue.length() > 0))
            {
                sReturnValue = sValue.trim();
            }
        }

        return sReturnValue;
    }

    /**
     * This will return true if the property value is: T, t, or any case combination
     * of "TRUE" and it will return false for all other values.
     *
     * @param sPropertyFile    The name of the property file.
     * @param sPropertyName    The name of the property that contains a boolean value.
     * This will return true if the value is: T, t, or any case combination of "TRUE"
     * and it will return false for all other values.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static boolean getPropertyBoolean(String sPropertyFile, String sPropertyName)
        throws PropertyAccessException
    {
        boolean bReturnValue = false;

        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getPropertyBoolean", "sPropertyFile", sPropertyFile);
        stringIsValid("getPropertyBoolean", "sPropertyName", sPropertyName);

        checkForRefreshAndLoad(sPropertyFile);

        Properties oProps = m_hAllProps.get(sPropertyFile);

        if (oProps != null)
        {
            String sValue = oProps.getProperty(sPropertyName);
            if ((sValue != null) && (sValue.length() > 0) &&
                ((sValue.trim().equalsIgnoreCase("T")) ||
                 (sValue.trim().equalsIgnoreCase("TRUE"))))
            {
                bReturnValue = true;
            }
        }

        return bReturnValue;
    }

    /**
     * This will return the long value conversion of the property.  If the property value
     * cannot be converted to a long, an exception will be thrown.
     *
     * @param sPropertyFile    The name of the property file.
     * @param sPropertyName    The name of the property that contains a boolean value.
     * @return This will return the long representation of the value.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static long getPropertyLong(String sPropertyFile, String sPropertyName)
        throws PropertyAccessException
    {
        long lReturnValue = 0;

        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getPropertyBoolean", "sPropertyFile", sPropertyFile);
        stringIsValid("getPropertyBoolean", "sPropertyName", sPropertyName);

        checkForRefreshAndLoad(sPropertyFile);

        Properties oProps = m_hAllProps.get(sPropertyFile);

        if (oProps != null)
        {
            String sValue = oProps.getProperty(sPropertyName);
            if ((sValue != null) && (sValue.length() > 0))
            {
                try
                {
                    lReturnValue = Long.parseLong(sValue.trim());
                }
                catch (Exception e)
                {
                    String sError = "Failed to convert string value: '" + sValue + "' to a long.  Error: " +
                                    e.getMessage();
                    log.warn(sError, e);
                    throw new PropertyAccessException(sError, e);
                }
            }
        }

        return lReturnValue;
    }

    /**
     * This method returns the set of keys in a property file.
     *
     * @param sPropertyFile The name of the property file.
     * @return An enumeration of property keys in the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static final Set<String> getPropertyNames(String sPropertyFile)
        throws PropertyAccessException
    {
        Set<String> setPropNames = null;

        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getPropertyNames", "sPropertyFile", sPropertyFile);

        checkForRefreshAndLoad(sPropertyFile);

        Properties oProps = m_hAllProps.get(sPropertyFile);

        if (oProps != null)
        {
            setPropNames = oProps.stringPropertyNames();
        }

        return setPropNames;
    }

    /**
     * This method returns the properties that are located within the given property
     * file.   If the properties have been cached and the cache is still fresh, then
     * it will return the values from the cache.  If the properties are cached, but
     * the cache is not fresh, then the cache will be updated with the current values
     * in the properties file and then the property values will be returned.  If the
     * properties for that file are not cached at all, the property will be retrieved
     * from the properties file and returned.
     *
     * NOTE:  THIS IS AN EXPENSIVE OPERATION.  IT WILL CREATE A DEEP COPY OF THE
     *        PROPERTIES AND RETURN IT.  THAT MEANS IT WILL CREATE AN EXACT REPLICA
     *        WITH ALL DATA.  THIS IS A PROTECTION TO MAKE SURE THAT A PROPERTY
     *        IS NOT INADVERTANTLY CHANGED OUTSIDE OF THIS CLASS.
     *
     * @param sPropertyFile    The name of the properties file without the path or
     *                         extension.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static final Properties getProperties(String sPropertyFile)
        throws PropertyAccessException
    {
        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getProperties", "sPropertyFile", sPropertyFile);

        checkForRefreshAndLoad(sPropertyFile);

        Properties oProps = m_hAllProps.get(sPropertyFile);

        Properties oRetProps = deepCopyProperties(oProps);

        return oRetProps;
    }

    /**
     * This will return the in milliseconds the refresh duration on the property file.
     * A setting of -1 means it never refreshes.
     *
     * @param sPropertyFile    The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static int getRefreshDuration(String sPropertyFile)
        throws PropertyAccessException
    {
        int iRefreshDuration = -1;

        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getRefreshDuration", "sPropertyFile", sPropertyFile);

        checkForRefreshAndLoad(sPropertyFile);

        RefreshInfo oInfo = m_hNextRefresh.get(sPropertyFile);
        if (oInfo != null)
        {
            iRefreshDuration = oInfo.m_iRefreshMilliseconds;
        }

        return iRefreshDuration;
    }

    /**
     * This will return the duration in milliseconds before the next refresh of the
     * properties file.  A value of -1 indicates that no refresh will occur.
     *
     * @param sPropertyFile    The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static int getDurationBeforeNextRefresh(String sPropertyFile)
        throws PropertyAccessException
    {
        int iNextRefreshDuration = -1;

        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("getDurationBeforeNextRefresh", "sPropertyFile", sPropertyFile);

        checkForRefreshAndLoad(sPropertyFile);

        RefreshInfo oInfo = m_hNextRefresh.get(sPropertyFile);
        if (oInfo != null)
        {
            if (oInfo.m_oRefreshMode == RefreshInfo.Mode.ALWAYS)
            {
                iNextRefreshDuration = 0;
            }
            else if (oInfo.m_oRefreshMode == RefreshInfo.Mode.NEVER)
            {
                iNextRefreshDuration = -1;
            }
            else
            {
                long lNowMilli = new Date().getTime();
                long lRefreshMilli = oInfo.m_dtRefreshDate.getTime();
                iNextRefreshDuration = (int) (lRefreshMilli - lNowMilli);
            }
        }

        return iNextRefreshDuration;
    }

    /**
     * If a property file has been cached, this will force a refresh of the property
     * file.  If a property file is not cached, then this operation will do nothing.
     *
     * @param sPropertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static void forceRefresh(String sPropertyFile)
        throws PropertyAccessException
    {
        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("forceRefresh", "sPropertyFile", sPropertyFile);

        RefreshInfo oInfo = m_hNextRefresh.get(sPropertyFile);
        // This means we have never loaded the file - so load it now...
        //--------------------------------------------------------------
        if (oInfo == null)
        {
            checkForRefreshAndLoad(sPropertyFile);
        }
        else if (oInfo.m_oRefreshMode != RefreshInfo.Mode.NEVER)
        {
            loadPropertyFile(sPropertyFile, oInfo);
        }
    }

    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public static String getPropertyFileLocation()
    {
        return m_sPropertyFileDirAbsolutePath;
    }

    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public static String getPropertyFileURL()
    {
        return "file:///" + m_sPropertyFileDirAbsolutePath;
    }

    /**
     * This method dumps the properties and associated values for a properties file to
     * the log file.
     *
     * @param sPropertyFile    The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public static void dumpPropsToLog(String sPropertyFile)
        throws PropertyAccessException
    {
        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("dumpPropsToLog", "sPropertyFile", sPropertyFile);

        StringBuffer sbLogMessage = new StringBuffer();

        sbLogMessage.append("Dumping contents of property file: '" + sPropertyFile + "'." + CRLF);

        RefreshInfo oInfo = m_hNextRefresh.get(sPropertyFile);
        // This means we have never loaded the file - so load it now...
        //--------------------------------------------------------------
        if (oInfo != null)
        {
            sbLogMessage.append("RefreshInfo.RefreshMode=" + oInfo.m_oRefreshMode + CRLF);
            sbLogMessage.append("RefreshInfo.RefreshMilliseconds=" + oInfo.m_iRefreshMilliseconds + CRLF);
            if (oInfo.m_dtRefreshDate != null)
            {
                SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");
                sbLogMessage.append("RefreshInfo.RefreshDate=" + oFormat.format(oInfo.m_dtRefreshDate) + CRLF);
            }
            else
            {
                sbLogMessage.append("RefreshInfo.RefreshDate=null" + CRLF);
            }

            sbLogMessage.append("Properties:");

            Properties oProps = m_hAllProps.get(sPropertyFile);
            if (oProps != null)
            {
                Set<String> setKeys = oProps.stringPropertyNames();
                if (setKeys != null)
                {
                    Iterator<String> iterKeys = setKeys.iterator();
                    while (iterKeys.hasNext())
                    {
                        String sKey = iterKeys.next();
                        String sValue = oProps.getProperty(sKey);
                        if (sValue != null)
                        {
                            sValue = sValue.trim();
                        }
                        sbLogMessage.append("Property:" + sKey + "=" + sValue + CRLF);
                    }
                }   // if (setKeys != null)
                else
                {
                    sbLogMessage.append("No properties were found in the property file." + CRLF);
                }
            }   // if (oProps != null)
            else
            {
                sbLogMessage.append("No properties were found in the property file." + CRLF);
            }
        }   //if (oInfo != null)
        else
        {
            sbLogMessage.append("No content.  Property file has never been loaded." + CRLF);
        }

        log.info(sbLogMessage.toString());
    }

    /**
     * This method is protected.  It is only to be used for unit testing and should
     * NEVER be used in the run-time environment. It's purpose is to allow properties
     * to be staged for a unit test.  It is marked as protected so that only classes
     * that are derived from this one can use it.  Note that this sets the property in
     * the cache memory only.   Because of the nature of this setting...   If a
     * property file is marked as not cached, or is marked with a cache refresh, the
     * setting of a property will override that and it will now be marked as cached
     * only and never refresh.  The purpose for this is two fold:   1) You do not want
     * the property file to be unknowningly changed.  2) Since a controlled
     * environment is required in testing, it is not wise to have the properties reset
     * back to the values in the stored file during the middle of a test.
     *
     * @param sPropertyFile    The name of the property file.
     * @param sPropertyName    The name of the property.
     * @param sValue    The value to set for the property.  NOTE:  This will only set
     *                  the property in memory (in the cache).  It will not alter the file itself.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    protected static void setProperty(String sPropertyFile, String sPropertyName, String sValue)
        throws PropertyAccessException
    {
        // Make sure everything is in a good state.
        //-----------------------------------------
        checkEnvVarSet();
        stringIsValid("setProperty", "sPropertyFile", sPropertyFile);
        stringIsValid("setProperty", "sPropertyName", sPropertyName);

        // Check for null and set it to empty string...
        //---------------------------------------------
        String sPropValue = sValue;
        if (sPropValue == null)
        {
            sPropValue = "";
        }

        // This call is primarily for the case where the file has not
        // yet been loaded.  We need to make sure it gets laoded.  After that
        // if a setProperty is called the mode will be set to Never, and this
        // call will essentially turn into a NOOP.
        //---------------------------------------------------------------------
        checkForRefreshAndLoad(sPropertyFile);

        RefreshInfo oInfo = m_hNextRefresh.get(sPropertyFile);
        if (oInfo == null)
        {
            String sMessage = "Property file: '" + sPropertyFile +
                              "' does not exist.  You cannot set a property for a file that does not exist.";
            log.error(sMessage);
            throw new PropertyAccessException(sMessage);
        }

        // Make sure we set this to "Never" refresh...
        //---------------------------------------------
        if (oInfo.m_oRefreshMode != RefreshInfo.Mode.NEVER)
        {
            synchronized (m_hNextRefresh)
            {
                oInfo.m_oRefreshMode = RefreshInfo.Mode.NEVER;
                oInfo.m_iRefreshMilliseconds = -1;
                oInfo.m_dtRefreshDate = null;
                m_hNextRefresh.put(sPropertyFile, oInfo);
            }

            // Put out a warning that a property file has been modified with a run-time modification.
            //---------------------------------------------------------------------------------------
            log.warn("Property File: " + sPropertyFile + " is being changed to 'NEVER' refresh because it is being " +
                     "modified at run-time.");
        }

        // Now update the property.
        //-------------------------
        Properties oProps = m_hAllProps.get(sPropertyFile);
        if (oProps == null)
        {
            String sMessage = "Property file: '" + sPropertyFile +
                              "' does not exist.  You cannot set a property for a file that does not exist.";
            log.error(sMessage);
            throw new PropertyAccessException(sMessage);
        }
        synchronized (m_hNextRefresh)
        {
            oProps.setProperty(sPropertyName, sValue);
        }
        // Put out a warning that a property file has been modified with a run-time modification.
        //---------------------------------------------------------------------------------------
        log.warn("Property File: " + sPropertyFile + ", PropertyName=" + sPropertyName +
                 " is being changed at run-time to:'" + sValue + "'.");
    }

    /**
     * This class is used to hold refresh information for a properties file.
     */
    private static class RefreshInfo
    {
        public static enum Mode {NEVER, ALWAYS, PERIODIC};

        Mode m_oRefreshMode;
        Date m_dtRefreshDate;
        int m_iRefreshMilliseconds;
    }
}