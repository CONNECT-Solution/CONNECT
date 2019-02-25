/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.common.propaccessor;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.checkPropertyList;

import gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyFileLocationRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyFileLocationResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.ListPropertiesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;
import gov.hhs.fha.nhinc.common.propertyaccess.SavePropertyRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.SimplePropertyResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileResponseType;
import gov.hhs.fha.nhinc.nhinccomponentpropaccessor.NhincComponentPropAccessorPortType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.List;
import java.util.Properties;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sai Valluripalli
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class NhincComponentPropAccessor implements NhincComponentPropAccessorPortType {

    private static final Logger LOG = LoggerFactory.getLogger(NhincComponentPropAccessor.class);
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ACT_FAIL = "fail";

    /**
     * This method returns the value of the given property that is located within the given property file. If the
     * properties have been cached and the cache is still fresh, then it will return the value from the cache. If the
     * properties are cached, but the cache is not fresh, then the cache will be updated with the current values in the
     * properties file and then the property will be returned. If the properties for that file are not cached at all,
     * the property will be retrieved from the properties file and returned.
     *
     * @param getPropertyRequest The input parameters - Property File and Property Name.
     * @return The value for the property.
     */
    @Override
    public GetPropertyResponseType getProperty(GetPropertyRequestType getPropertyRequest) {
        GetPropertyResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.getProperty(getPropertyRequest);
        } catch (Exception e) {
            String sMessage = "Failed to retrieve property.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This will return true if the property value is: T, t, or any case combination of "TRUE" and it will return false
     * for all other values.
     *
     * @param getPropertyBooleanRequest The property file and property name.
     * @return TRUE if the property is true and false if it is not.
     */
    @Override
    public GetPropertyBooleanResponseType getPropertyBoolean(GetPropertyBooleanRequestType getPropertyBooleanRequest) {
        GetPropertyBooleanResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.getPropertyBoolean(getPropertyBooleanRequest);
        } catch (Exception e) {
            String sMessage = "Failed to retrieve boolean property.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method returns the set of keys in a property file.
     *
     * @param getPropertyNamesRequest The name of the property file.
     * @return The list of property names in the property file.
     */
    @Override
    public GetPropertyNamesResponseType getPropertyNames(GetPropertyNamesRequestType getPropertyNamesRequest) {
        GetPropertyNamesResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.getPropertyNames(getPropertyNamesRequest);
        } catch (Exception e) {
            String sMessage = "Failed to retrieve property names.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method returns the properties that are located within the given property file. If the properties have been
     * cached and the cache is still fresh, then it will return the values from the cache. If the properties are cached,
     * but the cache is not fresh, then the cache will be updated with the current values in the properties file and
     * then the property values will be returned. If the properties for that file are not cached at all, the property
     * will be retrieved from the properties file and returned.
     *
     * NOTE: THIS IS AN EXPENSIVE OPERATION. IT WILL CREATE A DEEP COPY OF THE PROPERTIES AND RETURN IT. THAT MEANS IT
     * WILL CREATE AN EXACT REPLICA WITH ALL DATA. THIS IS A PROTECTION TO MAKE SURE THAT A PROPERTY IS NOT
     * INADVERTANTLY CHANGED OUTSIDE OF THIS CLASS.
     *
     * @param getPropertiesRequest Name of the property file.
     * @return Returns all of the properties and values in the property file.
     */
    @Override
    public GetPropertiesResponseType getProperties(GetPropertiesRequestType getPropertiesRequest) {
        GetPropertiesResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.getProperties(getPropertiesRequest);
        } catch (Exception e) {
            String sMessage = "Failed to retrieve properties.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method will return the location of the property files. Essentially it will return the value in the
     * nhinc.properties.dir system variable.
     *
     * @param getPropertyFileLocationRequest Nothing important - just need this unique for document literal binding.
     * @return The path and location of the property files.
     */
    @Override
    public GetPropertyFileLocationResponseType getPropertyFileLocation(
        GetPropertyFileLocationRequestType getPropertyFileLocationRequest) {
        GetPropertyFileLocationResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.getPropertyFileLocation(getPropertyFileLocationRequest);
        } catch (Exception e) {
            String sMessage = "Failed to retrieve property file location.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method dumps the properties and associated values for a properties file to the log file.
     *
     * @param dumpPropsToLogRequest the name of the property file.
     * @return Nothing - it simply always returns true.
     */
    @Override
    public DumpPropsToLogResponseType dumpPropsToLog(DumpPropsToLogRequestType dumpPropsToLogRequest) {
        DumpPropsToLogResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.dumpPropsToLog(dumpPropsToLogRequest);
        } catch (Exception e) {
            String sMessage = "Failed to dump property file to log.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method writes out the given properties as the specified properties file. Note: It does not merge
     * information. It will completely overwrite the current file with the new properties information. If the file does
     * not exist, it will create it. This writes the property file to the NHINC properties directory.
     *
     *
     * @param writePropertyFileRequest The name of the property file and the properties to write.
     * @return True if this succeeds.
     */
    @Override
    public WritePropertyFileResponseType writePropertyFile(WritePropertyFileRequestType writePropertyFileRequest) {
        WritePropertyFileResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.writePropertyFile(writePropertyFileRequest);
        } catch (Exception e) {
            String sMessage = "Failed to write property file.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    /**
     * This method deletes the specified properties file. Note: It will completely delete the file from the NHINC
     * properties directory.
     *
     *
     * @param deletePropertyFileRequest The name of the property file to be deleted without the ".properties" extension.
     * @return True if this succeeds.
     */
    @Override
    public DeletePropertyFileResponseType deletePropertyFile(DeletePropertyFileRequestType deletePropertyFileRequest) {
        DeletePropertyFileResponseType oOutput = null;

        try {
            oOutput = PropertyAccessHelper.deletePropertyFile(deletePropertyFileRequest);
        } catch (Exception e) {
            String sMessage = "Failed to delete property file.  Exception: " + e.getMessage();
            LOG.error(sMessage, e);
        }

        return oOutput;
    }

    @Override
    public SimplePropertyResponseType listProperties(ListPropertiesRequestType listPropertiesRequest) {
        String file = listPropertiesRequest.getFile();

        if (!checkPropertyList(file)) {
            return buildSimpleResponse(false, "Incorrect file name: " + file);
        }
        SimplePropertyResponseType response = new SimplePropertyResponseType();

        try {
            Properties propList = PropertyAccessor.getInstance().getProperties(file);
            addProperties(propList, response.getPropertyList(), file);
            response.setStatus(true);
            response.setMessage(ACT_SUCCESSFUL);
            return response;

        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to access {} properties file: {}", file, ex.getLocalizedMessage(), ex);
            return buildSimpleResponse(false, ACT_FAIL);
        }
    }

    private static void addProperties(Properties props, List<PropertyType> viewProps, String propFileName)
        throws PropertyAccessException {

        if (props != null) {
            for (Object key : props.keySet()) {
                PropertyType pt = new PropertyType();
                pt.setPropertyName((String) key);
                pt.setPropertyValue(props.getProperty(pt.getPropertyName()));
                pt.setPropertyText(
                    PropertyAccessor.getInstance().getPropertyComment(propFileName, pt.getPropertyName()));
                viewProps.add(pt);
            }
        }
    }

    @Override
    public SimplePropertyResponseType saveProperty(SavePropertyRequestType propValue) {
        String file = propValue.getFile();
        if (!checkPropertyList(file)) {
            return buildSimpleResponse(false, "Incorrect file name: " + file);
        }
        try {
            PropertyAccessor.getInstance().setProperty(propValue.getFile(), propValue.getPropertyName(),
                propValue.getPropertyValue());
            return buildSimpleResponse(true, ACT_SUCCESSFUL);
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to update {}  in properties file: {}", propValue.getPropertyName(),
                ex.getLocalizedMessage(), ex);
            return buildSimpleResponse(false, ACT_FAIL);
        }
    }

    private static SimplePropertyResponseType buildSimpleResponse(Boolean status, String message) {
        SimplePropertyResponseType retMsg = new SimplePropertyResponseType();
        retMsg.setStatus(status);
        retMsg.setMessage(message);
        return retMsg;
    }

}
