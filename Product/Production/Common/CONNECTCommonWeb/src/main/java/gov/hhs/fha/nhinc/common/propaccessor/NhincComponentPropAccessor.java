/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.common.propaccessor;

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
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileRequestType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincComponentPropAccessor", portName = "NhincComponentPropAccessorPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpropaccessor.NhincComponentPropAccessorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpropaccessor", wsdlLocation = "WEB-INF/wsdl/NhincComponentPropAccessor/NhincComponentPropAccessor.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincComponentPropAccessor {

    private static Log log = LogFactory.getLog(NhincComponentPropAccessor.class);


    /**
     * This method returns the value of the given property that is located within the
     * given property file.   If the properties have been cached and the cache is
     * still fresh, then it will return the value from the cache.  If the properties
     * are cached, but the cache is not fresh, then the cache will be updated with the
     * current values in the properties file and then the property will be returned.
     * If the properties for that file are not cached at all, the property will be
     * retrieved from the properties file and returned.
     *
     * @param getPropertyRequest The input parameters - Property File and Property Name.
     * @return The value for the property.
     */
    public GetPropertyResponseType getProperty(GetPropertyRequestType getPropertyRequest)
    {
        GetPropertyResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getProperty(getPropertyRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve property.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This will return true if the property value is: T, t, or any case combination
     * of "TRUE" and it will return false for all other values.
     *
     * @param getPropertyBooleanRequest The property file and property name.
     * @return TRUE if the property is true and false if it is not.
     */
    public GetPropertyBooleanResponseType getPropertyBoolean(GetPropertyBooleanRequestType getPropertyBooleanRequest)
    {
        GetPropertyBooleanResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getPropertyBoolean(getPropertyBooleanRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve boolean property.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method returns the set of keys in a property file.
     *
     * @param getPropertyNamesRequest The name of the property file.
     * @return The list of property names in the property file.
     */
    public GetPropertyNamesResponseType getPropertyNames(GetPropertyNamesRequestType getPropertyNamesRequest)
    {
        GetPropertyNamesResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getPropertyNames(getPropertyNamesRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve property names.  Exception: " + e.getMessage();
            log.error(sMessage, e);
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
     * @param getPropertiesRequest Name of the property file.
     * @return Returns all of the properties and values in the property file.
     */
    public GetPropertiesResponseType getProperties(GetPropertiesRequestType getPropertiesRequest)
    {
        GetPropertiesResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getProperties(getPropertiesRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve properties.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This will return the in milliseconds the refresh duration on the property file.
     * A setting of -1 means it never refreshes.
     *
     * @param getRefreshDurationRequest The name of the property file.
     * @return the refresh duration for the property file.
     */
    public GetRefreshDurationResponseType getRefreshDuration(GetRefreshDurationRequestType getRefreshDurationRequest)
    {
        GetRefreshDurationResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getRefreshDuration(getRefreshDurationRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve refresh duration.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This will return the duration in milliseconds before the next refresh of the
     * properties file.  A value of -1 indicates that no refresh will occur.
     *
     * @param getDurationBeforeNextRefreshRequest The name of the property file.
     * @return The number of milliseconds before the next refresh will occur.
     */
    public GetDurationBeforeNextRefreshResponseType getDurationBeforeNextRefresh(GetDurationBeforeNextRefreshRequestType getDurationBeforeNextRefreshRequest)
    {
        GetDurationBeforeNextRefreshResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getDurationBeforeNextRefresh(getDurationBeforeNextRefreshRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve duration before next refresh.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * If a property file has been cached, this will force a refresh of the property
     * file.  If a property file is not cached, then this operation will do nothing.
     *
     * @param forceRefreshRequest The name of the property file.
     * @return true if the property file was refreshed.
     */
    public ForceRefreshResponseType forceRefresh(ForceRefreshRequestType forceRefreshRequest)
    {
        ForceRefreshResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.forceRefresh(forceRefreshRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to force refresh.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method will return the location of the property files.  Essentially it
     * will return the value in the NHINC_PROPERTIES_DIR environment variable.
     *
     * @param getPropertyFileLocationRequest Nothing important - just need this unique for document literal binding.
     * @return The path and location of the property files.
     */
    public GetPropertyFileLocationResponseType getPropertyFileLocation(GetPropertyFileLocationRequestType getPropertyFileLocationRequest)
    {
        GetPropertyFileLocationResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.getPropertyFileLocation(getPropertyFileLocationRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to retrieve property file location.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method dumps the properties and associated values for a properties file to
     * the log file.
     *
     * @param dumpPropsToLogRequest the name of the property file.
     * @return Nothing - it simply always returns true.
     */
    public DumpPropsToLogResponseType dumpPropsToLog(DumpPropsToLogRequestType dumpPropsToLogRequest)
    {
        DumpPropsToLogResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.dumpPropsToLog(dumpPropsToLogRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to dump property file to log.  Exception: " + e.getMessage();
            log.error(sMessage, e);
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
     * @param writePropertyFileRequest The name of the property file and the properties to write.
     * @return True if this succeeds.
     */
    public WritePropertyFileResponseType writePropertyFile(WritePropertyFileRequestType writePropertyFileRequest)
    {
        WritePropertyFileResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.writePropertyFile(writePropertyFileRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to write property file.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

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
     * @param deletePropertyFileRequest The name of the property file to be deleted without the ".properties" extension.
     * @return True if this succeeds.
     */
    public DeletePropertyFileResponseType deletePropertyFile(DeletePropertyFileRequestType deletePropertyFileRequest)
    {
        DeletePropertyFileResponseType oOutput = null;

        try
        {
            oOutput = PropertyAccessHelper.deletePropertyFile(deletePropertyFileRequest);
        }
        catch (Exception e)
        {
            String sMessage = "Failed to delete property file.  Exception: " + e.getMessage();
            log.error(sMessage, e);
        }

        return oOutput;
    }

}
