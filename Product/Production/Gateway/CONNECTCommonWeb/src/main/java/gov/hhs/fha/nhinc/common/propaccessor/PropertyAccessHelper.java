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
import gov.hhs.fha.nhinc.common.propertyaccess.PropertiesType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileResponseType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyFileManager;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This class is used by the PropertyAccessService to do the actual work of this web service. It keeps the web service
 * class as a simple wrapper.
 *
 * @author Les Westberg
 */
public class PropertyAccessHelper {

    private PropertyAccessHelper() {
    }

    /**
     * This method returns the value of the given property that is located within the given property file. If the
     * properties have been cached and the cache is still fresh, then it will return the value from the cache. If the
     * properties are cached, but the cache is not fresh, then the cache will be updated with the current values in the
     * properties file and then the property will be returned. If the properties for that file are not cached at all,
     * the property will be retrieved from the properties file and returned.
     *
     * @param input The input parameters - Property File and Property Name.
     * @return The value for the property.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    public static GetPropertyResponseType getProperty(GetPropertyRequestType input) throws PropertyAccessException {
        GetPropertyResponseType oOutput = new GetPropertyResponseType();

        if (input != null && StringUtils.isNotEmpty(input.getPropertyFile())
            && StringUtils.isNotEmpty(input.getPropertyName())) {
            String sPropertyFile = input.getPropertyFile();
            String sPropertyName = input.getPropertyName();

            String sValue = PropertyAccessor.getInstance().getProperty(sPropertyFile, sPropertyName);
            if (sValue != null) {
                oOutput.setPropertyValue(sValue);
            }
        }

        return oOutput;
    }

    /**
     * This will return true if the property value is: T, t, or any case combination of "TRUE" and it will return false
     * for all other values.
     *
     * @param input The property file and property name.
     * @return TRUE if the property is true and false if it is not.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    public static GetPropertyBooleanResponseType getPropertyBoolean(GetPropertyBooleanRequestType input)
        throws PropertyAccessException {
        GetPropertyBooleanResponseType oOutput = new GetPropertyBooleanResponseType();

        if (input != null && StringUtils.isNotEmpty(input.getPropertyFile())
            && StringUtils.isNotEmpty(input.getPropertyName())) {
            String sPropertyFile = input.getPropertyFile();
            String sPropertyName = input.getPropertyName();

            boolean bValue = PropertyAccessor.getInstance().getPropertyBoolean(sPropertyFile, sPropertyName);
            oOutput.setPropertyValue(bValue);
        }

        return oOutput;
    }

    /**
     * This method returns the set of keys in a property file.
     *
     * @param input The name of the property file.
     * @return The list of property names in the property file.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    public static GetPropertyNamesResponseType getPropertyNames(GetPropertyNamesRequestType input)
        throws PropertyAccessException {
        GetPropertyNamesResponseType oOutput = new GetPropertyNamesResponseType();

        if (input != null && StringUtils.isNotEmpty(input.getPropertyFile())) {
            String sPropertyFile = input.getPropertyFile();

            Set<String> setKeys = PropertyAccessor.getInstance().getPropertyNames(sPropertyFile);
            if (setKeys != null) {
                Iterator<String> iterKeys = setKeys.iterator();
                while (iterKeys.hasNext()) {
                    String sKey = iterKeys.next();
                    oOutput.getPropertyName().add(sKey);
                }
            }
        }

        return oOutput;
    }

    /**
     * This method returns the properties that are located within the given property file. If the properties have been
     * cached and the cache is still fresh, then it will return the values from the cache. If the properties are cached,
     * but the cache is not fresh, then the cache will be updated with the current values in the properties file and
     * then the property values will be returned. If the properties for that file are not cached at all, the property
     * will be retrieved from the properties file and returned.
     * <p>
     * NOTE: THIS IS AN EXPENSIVE OPERATION. IT WILL CREATE A DEEP COPY OF THE PROPERTIES AND RETURN IT. THAT MEANS IT
     * WILL CREATE AN EXACT REPLICA WITH ALL DATA. THIS IS A PROTECTION TO MAKE SURE THAT A PROPERTY IS NOT
     * INADVERTANTLY CHANGED OUTSIDE OF THIS CLASS.
     *
     * @param input Name of the property file.
     * @return Returns all of the properties and values in the property file.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    public static GetPropertiesResponseType getProperties(GetPropertiesRequestType input)
        throws PropertyAccessException {
        GetPropertiesResponseType oOutput = new GetPropertiesResponseType();
        PropertiesType oProperties = new PropertiesType();
        boolean bHasProps = false;

        if (input != null && input.getPropertyFile() != null && input.getPropertyFile().length() > 0) {
            String sPropertyFile = input.getPropertyFile();

            Properties oProps = PropertyAccessor.getInstance().getProperties(sPropertyFile);
            if (oProps != null) {
                Set<String> setKeys = oProps.stringPropertyNames();
                if (setKeys != null) {
                    Iterator<String> iterKeys = setKeys.iterator();
                    while (iterKeys.hasNext()) {
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

        if (bHasProps) {
            oOutput.setProperties(oProperties);
        }

        return oOutput;
    }

    /**
     * This method will return the location of the property files. Essentially it will return the value in the
     * nhinc.properties.dir system variable.
     *
     * @param input Nothing important - just need this unique for document literal binding.
     * @return The path and location of the property files.
     */
    public static GetPropertyFileLocationResponseType getPropertyFileLocation(GetPropertyFileLocationRequestType input) {
        GetPropertyFileLocationResponseType oOutput = new GetPropertyFileLocationResponseType();

        String sLocation = PropertyAccessor.getInstance().getPropertyFileLocation();
        oOutput.setLocation(sLocation);

        return oOutput;
    }

    /**
     * This method dumps the properties and associated values for a properties file to the log file.
     *
     * @param input the name of the property file.
     * @return Nothing - it simply always returns true.
     * @throws PropertyAccessException
     */
    public static DumpPropsToLogResponseType dumpPropsToLog(DumpPropsToLogRequestType input)
        throws PropertyAccessException {
        DumpPropsToLogResponseType oOutput = new DumpPropsToLogResponseType();

        if (input != null && StringUtils.isNotEmpty(input.getPropertyFile())) {
            String sPropertyFile = input.getPropertyFile();

            PropertyAccessor.getInstance().dumpPropsToLog(sPropertyFile);

            // If we got here without an exception, then we refreshed.
            // ----------------------------------------------------------
            oOutput.setCompleted(true);
        }

        return oOutput;
    }

    /**
     * This method writes out the given properties as the specified properties file. Note: It does not merge
     * information. It will completely overwrite the current file with the new properties information. If the file does
     * not exist, it will create it. This writes the property file to the NHINC properties directory.
     * <p>
     * NOTE THAT THERE MUST BE AT LEAST ONE PROPERTY TO WRITE.
     *
     * @param part1 The name of the property file and the properties to write.
     * @return True if this succeeds.
     * @throws PropertyAccessException
     */
    public static WritePropertyFileResponseType writePropertyFile(WritePropertyFileRequestType part1)
        throws PropertyAccessException {
        WritePropertyFileResponseType oOutput = new WritePropertyFileResponseType();

        if (part1 != null
            && StringUtils.isNotEmpty(part1.getPropertyFile())
            && part1.getProperties() != null
            && CollectionUtils.isNotEmpty(part1.getProperties().getProperty())) {
            String sPropertyFile = part1.getPropertyFile();

            java.util.Properties oPropsToStore = new java.util.Properties();

            for (PropertyType oInputProp : part1.getProperties().getProperty()) {
                if (StringUtils.isEmpty(oInputProp.getPropertyName())) {
                    throw new PropertyAccessException(
                        "Found a property without a name.  All properties must have a name.");
                }

                if (oInputProp.getPropertyValue() == null) {
                    throw new PropertyAccessException(
                        "The property value cannot be null.  Property: " + oInputProp.getPropertyName());
                }

                String sPropName = oInputProp.getPropertyName();
                String sPropValue = oInputProp.getPropertyValue();

                oPropsToStore.setProperty(sPropName, sPropValue);
            }

            PropertyFileManager.writePropertyFile(sPropertyFile, oPropsToStore);
        } else {
            String sErrorMessage = "Failed to write property file.  There must be both a valid "
                + "file name without the '.properties' extension and at least " + "one property to write.";
            throw new PropertyAccessException(sErrorMessage);
        }

        oOutput.setSuccess(true); // If we got here we were successful
        return oOutput;
    }

    /**
     * This method deletes the specified properties file. Note: It will completely delete the file from the NHINC
     * properties directory.
     *
     *
     * @param part1 The name of the property file to be deleted without the ".properties" extension.
     * @return True if this succeeds.
     * @throws PropertyAccessException
     */
    public static DeletePropertyFileResponseType deletePropertyFile(DeletePropertyFileRequestType part1)
        throws PropertyAccessException {
        DeletePropertyFileResponseType oOutput = new DeletePropertyFileResponseType();

        if (part1 != null && StringUtils.isNotEmpty(part1.getPropertyFile())) {
            String sPropertyFile = part1.getPropertyFile();
            PropertyFileManager.deletePropertyFile(sPropertyFile);
        }

        oOutput.setSuccess(true); // If we got here we were successful
        return oOutput;
    }
}
