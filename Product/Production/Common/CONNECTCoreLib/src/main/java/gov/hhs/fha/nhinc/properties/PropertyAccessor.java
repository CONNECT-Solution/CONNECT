/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.properties;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.io.File;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a POJO class that is used to access properties within a property file.
 *
 * @author msw
 */
public class PropertyAccessor implements IPropertyAcessor {

    private final PropertyFileDAO propertyFileDAO;
    private final PropertyAccessorFileUtilities fileUtilities;

    private static final Logger LOG = LoggerFactory.getLogger(PropertyAccessor.class);

    /**
     * Default constructor.
     */
    protected PropertyAccessor() {
        propertyFileDAO = createPropertyFileDAO();
        fileUtilities = createPropertyAccessorFileUtilities();
    }

    private static class SingletonHolder {

        public static final PropertyAccessor INSTANCE = new PropertyAccessor();
    }

    // singleton
    public static PropertyAccessor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * @param propertyFileName The name of the property file. This is the name of the file without a path and without
     *            the ".properties" extension. Examples of this would be "connection" or "gateway".
     *
     */
    public synchronized void setPropertyFile(final String propertyFileName) {
        if (!propertyFileDAO.containsPropFile(propertyFileName)) {
            try {
                loadPropertyFile(propertyFileName);
            } catch (final PropertyAccessException ex) {
                LOG.warn("Unable to load property file: " + propertyFileName, ex);
            }
        }
    }

    /**
     * This method returns the value of the given property that is located within the given property file. If the
     * properties have been cached and the cache is still fresh, then it will return the value from the cache. If the
     * properties are cached, but the cache is not fresh, then the cache will be updated with the current values in the
     * properties file and then the property will be returned. If the properties for that file are not cached at all,
     * the property will be retrieved from the properties file and returned.
     *
     * @param propertyFile The name of the property file. This is the name of the file without a path and without the
     *            ".properties" extension. Examples of this would be "connection" or "gateway".
     * @param propertyName This is the name of the property within the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    @Override
    public synchronized String getProperty(final String propertyFile, final String propertyName) throws PropertyAccessException {
        validateInput(propertyFile, propertyName);
        loadPropertyFile(propertyFile);

        return propertyFileDAO.getProperty(propertyFile, propertyName);
    }

    /**
     * Sets a property.
     *
     * @param propertyFileName the property file name
     * @param key the property key
     * @param value the property value
     * @throws PropertyAccessException the property access exception
     */
    @Override
    public void setProperty(final String propertyFileName, final String key, final String value) throws PropertyAccessException {
        loadPropertyFile(propertyFileName);

        propertyFileDAO.setProperty(propertyFileName, key, value);
    }

    @Override
    public String getPropertyComment(final String propertyFileName, final String key) throws PropertyAccessException {
        loadPropertyFile(propertyFileName);

        return propertyFileDAO.getPropertyComment(propertyFileName, key);
    }

    /**
     * This will return true if the property value is: T, t, or any case combination of "TRUE" and it will return false
     * for all other values.
     *
     * @param propertyFile The name of the property file.
     * @param propertyName The name of the property that contains a boolean value. This will return true if the value
     *            is: T, t, or any case combination of "TRUE" and it will return false for all other values.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    @Override
    public synchronized boolean getPropertyBoolean(final String propertyFile, final String propertyName)
            throws PropertyAccessException {
        validateInput(propertyFile, propertyName);
        loadPropertyFile(propertyFile);

        return propertyFileDAO.getPropertyBoolean(propertyFile, propertyName);
    }

    /**
     * This will return the long value conversion of the property. If the property value cannot be converted to a long,
     * an exception will be thrown.
     *
     * @param propertyFile The name of the property file.
     * @param propertyName The name of the property that contains a boolean value.
     * @return This will return the long representation of the value.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized long getPropertyLong(final String propertyFile, final String propertyName) throws PropertyAccessException {
        validateInput(propertyFile, propertyName);
        loadPropertyFile(propertyFile);

        return propertyFileDAO.getPropertyLong(propertyFile, propertyName);
    }

    /**
     * This method returns the set of keys in a property file.
     *
     * @param propertyFile The name of the property file.
     * @return An enumeration of property keys in the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized final Set<String> getPropertyNames(final String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        loadPropertyFile(propertyFile);

        return propertyFileDAO.getPropertyNames(propertyFile);
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
     * @param propertyFile The name of the properties file without the path or extension.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized final Properties getProperties(final String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        loadPropertyFile(propertyFile);

        return propertyFileDAO.getProperties(propertyFile);
    }

    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public synchronized String getPropertyFileLocation() {
        return fileUtilities.getPropertyFileLocation();
    }

    public synchronized String getPropertyFileLocation(final String propertyFileName) {
        return fileUtilities.getPropertyFileLocation(propertyFileName);
    }

    public synchronized void setPropertyFileLocation(final String propertyFileDirAbsolutePath) {
        fileUtilities.setPropertyFileLocation(propertyFileDirAbsolutePath);
    }

    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public synchronized String getPropertyFileURL() {
        return fileUtilities.getPropertyFileURL();
    }

    /**
     * This method dumps the properties and associated values for a properties file to the log file.
     *
     * @param propertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public void dumpPropsToLog(final String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        loadPropertyFile(propertyFile);

        propertyFileDAO.printToLog(propertyFile);
    }

    protected void validateInput(final String... propertyStrings) throws PropertyAccessException {
        for (final String propertyString : propertyStrings) {
            isPropertyStringValid(propertyString);
        }
    }

    protected PropertyFileDAO createPropertyFileDAO() {
        return new PropertyFileDAO();
    }

    protected PropertyAccessorFileUtilities createPropertyAccessorFileUtilities() {
        return new PropertyAccessorFileUtilities();
    }

    private void isPropertyStringValid(final String property) throws PropertyAccessException {
        if (NullChecker.isNullish(property)) {
            throw new PropertyAccessException("Invalid property name value: " + property);
        }
    }

    private synchronized void loadPropertyFile(final String propertyFile) throws PropertyAccessException {
        if (!propertyFileDAO.containsPropFile(propertyFile)) {
            final String propFilePathAndName = fileUtilities.getPropertyFileLocation(propertyFile);

            final File propertyFileLocation = new File(propFilePathAndName);
            propertyFileDAO.loadPropertyFile(propertyFileLocation, propertyFile);
        }
    }

}
