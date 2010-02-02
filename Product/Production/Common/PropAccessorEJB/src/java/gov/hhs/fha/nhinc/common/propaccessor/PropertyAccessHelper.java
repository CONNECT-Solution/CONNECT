package gov.hhs.fha.nhinc.common.propaccessor;

import java.util.Set;
import java.util.Iterator;
import java.util.Properties;

import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetRefreshDurationResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetRefreshDurationRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetDurationBeforeNextRefreshResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetDurationBeforeNextRefreshRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.ForceRefreshResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.ForceRefreshRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyFileLocationResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyFileLocationRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertiesType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;

import gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileResponseType;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyFileManager;

/**
 * This class is used by the PropertyAccessService to do the actual work
 * of this web service.  It keeps the web service class as a simple wrapper.
 * 
 * @author Les Westberg
 */
public class PropertyAccessHelper 
{
    /**
     * This method returns the value of the given property that is located within the
     * given property file.   If the properties have been cached and the cache is
     * still fresh, then it will return the value from the cache.  If the properties
     * are cached, but the cache is not fresh, then the cache will be updated with the
     * current values in the properties file and then the property will be returned.
     * If the properties for that file are not cached at all, the property will be
     * retrieved from the properties file and returned.
     * 
     * @param input The input parameters - Property File and Property Name.
     * @return The value for the property.
     */
    public static GetPropertyResponseType getProperty(GetPropertyRequestType input)
        throws PropertyAccessException
    {
        GetPropertyResponseType oOutput = new GetPropertyResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0) &&
            (input.getPropertyName() != null) &&
            (input.getPropertyName().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            String sPropertyName = input.getPropertyName();
            
            String sValue = PropertyAccessor.getProperty(sPropertyFile, sPropertyName);
            if (sValue != null)
            {
                oOutput.setPropertyValue(sValue);
            }
        }
        
        return oOutput;
    }

    /**
     * This will return true if the property value is: T, t, or any case combination
     * of "TRUE" and it will return false for all other values.
     * 
     * @param input The property file and property name.
     * @return TRUE if the property is true and false if it is not.
     */
    public static GetPropertyBooleanResponseType getPropertyBoolean(GetPropertyBooleanRequestType input)
        throws PropertyAccessException
    {
        GetPropertyBooleanResponseType oOutput = new GetPropertyBooleanResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0) &&
            (input.getPropertyName() != null) &&
            (input.getPropertyName().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            String sPropertyName = input.getPropertyName();
            
            boolean bValue = PropertyAccessor.getPropertyBoolean(sPropertyFile, sPropertyName);
            oOutput.setPropertyValue(bValue);
        }
        
        return oOutput;
    }

    /**
     * This method returns the set of keys in a property file.
     * 
     * @param input The name of the property file.
     * @return The list of property names in the property file.
     */
    public static GetPropertyNamesResponseType getPropertyNames(GetPropertyNamesRequestType input)
        throws PropertyAccessException
    {
        GetPropertyNamesResponseType oOutput = new GetPropertyNamesResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            
            Set<String> setKeys = PropertyAccessor.getPropertyNames(sPropertyFile);
            if (setKeys != null)
            {
                Iterator<String> iterKeys = setKeys.iterator();
                while (iterKeys.hasNext())
                {
                    String sKey = iterKeys.next();
                    oOutput.getPropertyName().add(sKey);
                }
            }
        }
        
        return oOutput;
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
     * @param input Name of the property file.
     * @return Returns all of the properties and values in the property file.
     */
    public static GetPropertiesResponseType getProperties(GetPropertiesRequestType input)
        throws PropertyAccessException
    {
        GetPropertiesResponseType oOutput = new GetPropertiesResponseType();
        PropertiesType oProperties = new PropertiesType();
        boolean bHasProps = false;
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            
            Properties oProps = PropertyAccessor.getProperties(sPropertyFile);
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
                        
                        PropertyType oProperty = new PropertyType();
                        oProperty.setPropertyName(sKey);
                        oProperty.setPropertyValue(sValue);
                        oProperties.getProperty().add(oProperty);
                        bHasProps = true;
                    }
                }
            }
        }
        
        if (bHasProps)
        {
            oOutput.setProperties(oProperties);
        }
        
        return oOutput;
    }

    /**
     * This will return the in milliseconds the refresh duration on the property file.
     * A setting of -1 means it never refreshes.
     * 
     * @param input The name of the property file.
     * @return the refresh duration for the property file.
     */
    public static GetRefreshDurationResponseType getRefreshDuration(GetRefreshDurationRequestType input)
        throws PropertyAccessException
    {
        GetRefreshDurationResponseType oOutput = new GetRefreshDurationResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            
            int iValue = PropertyAccessor.getRefreshDuration(sPropertyFile);
            oOutput.setDurationMillis(iValue);
        }
        
        return oOutput;
    }

    /**
     * This will return the duration in milliseconds before the next refresh of the
     * properties file.  A value of -1 indicates that no refresh will occur.
     * 
     * @param input The name of the property file.
     * @return The number of milliseconds before the next refresh will occur.
     */
    public static GetDurationBeforeNextRefreshResponseType getDurationBeforeNextRefresh(GetDurationBeforeNextRefreshRequestType input)
        throws PropertyAccessException
    {
        GetDurationBeforeNextRefreshResponseType oOutput = new GetDurationBeforeNextRefreshResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            
            int iValue = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
            oOutput.setDurationMillis(iValue);
        }
        
        return oOutput;
    }

    /**
     * If a property file has been cached, this will force a refresh of the property
     * file.  If a property file is not cached, then this operation will do nothing.
     * 
     * @param input The name of the property file.
     * @return true if the property file was refreshed.
     */
    public static ForceRefreshResponseType forceRefresh(ForceRefreshRequestType input)
        throws PropertyAccessException
    {
        ForceRefreshResponseType oOutput = new ForceRefreshResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            
            PropertyAccessor.forceRefresh(sPropertyFile);
            
            // If we got here without an exception, then we refreshed.
            //----------------------------------------------------------
            oOutput.setRefreshed(true);
        }
        
        return oOutput;
    }

    /**
     * This method will return the location of the property files.  Essentially it
     * will return the value in the NHINC_PROPERTIES_DIR environment variable.
     * 
     * @param input Nothing important - just need this unique for document literal binding.
     * @return The path and location of the property files.
     */
    public static GetPropertyFileLocationResponseType getPropertyFileLocation(GetPropertyFileLocationRequestType input)
        throws PropertyAccessException
    {
        GetPropertyFileLocationResponseType oOutput = new GetPropertyFileLocationResponseType();
        
        String sLocation = PropertyAccessor.getPropertyFileLocation();
        oOutput.setLocation(sLocation);

        return oOutput;
    }

    /**
     * This method dumps the properties and associated values for a properties file to
     * the log file.
     * 
     * @param input the name of the property file.
     * @return Nothing - it simply always returns true.
     * @throws PropertyAccessException 
     */
    public static DumpPropsToLogResponseType dumpPropsToLog(DumpPropsToLogRequestType input)
        throws PropertyAccessException
    {
        DumpPropsToLogResponseType oOutput = new DumpPropsToLogResponseType();
        
        if ((input != null) &&
            (input.getPropertyFile() != null) &&
            (input.getPropertyFile().length() > 0))
        {
            String sPropertyFile = input.getPropertyFile();
            
            PropertyAccessor.dumpPropsToLog(sPropertyFile);
            
            // If we got here without an exception, then we refreshed.
            //----------------------------------------------------------
            oOutput.setCompleted(true);
        }
        
        return oOutput;
    }

    /**
     * This method writes out the given properties as the specified properties file.
     * Note:  It does not merge information.  It will completely overwrite the current
     * file with the new properties information.  If the file does not exist, it will
     * create it. This writes the property file to the NHINC properties directory.
     * 
     * WARNING: If a property file is currently cached in memory - the file will not be
     * re-read until the next time the cache refreshes its property from the file based on
     * the criteria that was put in place when the properties were last loaded from file.
     * This is based on setting of the "CacheRefreshDuration" property in the property file.
     * 
     * NOTE THAT THERE MUST BE AT LEAST ONE PROPERTY TO WRITE.
     * 
     * @param part1 The name of the property file and the properties to write.
     * @return True if this succeeds.
     * @throws PropertyAccessException 
     */
    public static WritePropertyFileResponseType writePropertyFile(WritePropertyFileRequestType part1)
        throws PropertyAccessException
    {
        WritePropertyFileResponseType oOutput = new WritePropertyFileResponseType();
        
        if ((part1 != null) &&
            (part1.getPropertyFile() != null) &&
            (part1.getPropertyFile().length() > 0) &&
            (part1.getProperties() != null) &&
            (part1.getProperties().getProperty() != null) &&
            (part1.getProperties().getProperty().size() > 0))
        {
            String sPropertyFile = part1.getPropertyFile();
            
            java.util.Properties oPropsToStore = new java.util.Properties();
            
            for (PropertyType oInputProp : part1.getProperties().getProperty())
            {
                if ((oInputProp.getPropertyName() == null) ||
                    (oInputProp.getPropertyName().length() <= 0))
                {
                    throw new PropertyAccessException("Found a property without a name.  All properties must have a name.");
                }
                
                if (oInputProp.getPropertyValue() == null)
                {
                    throw new PropertyAccessException("The property value cannot be null.  Property: " + 
                                                      oInputProp.getPropertyName());
                }
                
                String sPropName = oInputProp.getPropertyName();
                String sPropValue = oInputProp.getPropertyValue();
                
                oPropsToStore.setProperty(sPropName, sPropValue);
            }
            
            PropertyFileManager.writePropertyFile(sPropertyFile, oPropsToStore);
        }
        else
        {
            int iCount = 0;
            if ((part1 != null) &&
                (part1.getProperties() != null) &&
                (part1.getProperties().getProperty() != null))
            {
                iCount = part1.getProperties().getProperty().size();
            }
            String sErrorMessage = "Failed to write property file.  There must be both a valid " +
                                   "file name without the '.properties' extension and at least " +
                                   "one property to write. PropertyFile:" + part1.getPropertyFile() +
                                   ", PropertiesCount = " + iCount;
            throw new PropertyAccessException(sErrorMessage);
        }
                
        oOutput.setSuccess(true);       // If we got here we were successful
        return oOutput;
    }

    /**
     * This method deletes the specified properties file.
     * Note:  It will completely delete the file from the NHINC properties directory.
     * 
     * WARNING: If a property file is currently cached in memory - the file will not be
     * removed from memory until the next time the cache refreshes its property from the file based on
     * the criteria that was put in place when the properties were last loaded from file.
     * This is based on setting of the "CacheRefreshDuration" property in the property file.
     * 
     * @param part1 The name of the property file to be deleted without the ".properties" extension.
     * @return True if this succeeds.
     * @throws PropertyAccessException 
     */
    public static DeletePropertyFileResponseType deletePropertyFile(DeletePropertyFileRequestType part1)
        throws PropertyAccessException
    {
        DeletePropertyFileResponseType oOutput = new DeletePropertyFileResponseType();
        
        if ((part1 != null) &&
            (part1.getPropertyFile() != null) &&
            (part1.getPropertyFile().length() > 0))
        {
            String sPropertyFile = part1.getPropertyFile();
            PropertyFileManager.deletePropertyFile(sPropertyFile);
        }
        
        oOutput.setSuccess(true);       // If we got here we were successful
        return oOutput;
    }
}
