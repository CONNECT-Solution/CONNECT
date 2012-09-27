/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

/**
 * This class is a POJO class that is used to access properties within a property file.
 */
public class PropertyAccessor implements IPropertyAcessor {
    private static final String CACHE_REFRESH_DURATION = "CacheRefreshDuration";
    
    private static PropertyAccessor instance;
    
    private PropertyFileRefreshHandler refreshHandler;    
    private PropertyFileDAO propertyFileDAO;    
    private PropertyAccessorFileUtilities fileUtilities;
    private String propertyFileName;
    
    /**
     * Default constructor.
     */
    protected PropertyAccessor() {
        refreshHandler = createPropertyFileRefreshHandler();   
        propertyFileDAO = createPropertyFileDAO();
        fileUtilities = createPropertyAccessorFileUtilities();   
    }
    
    public static PropertyAccessor getInstance() {
        if (instance == null) {
            instance = new PropertyAccessor();            
        }
        return instance;
    }
    
    public static PropertyAccessor getInstance(String propertyFileName) {
        instance = getInstance();
        instance.setPropertyFile(propertyFileName);
        return instance;
    } 
    
    /**
     * @param propertyFile The name of the property file. This is the name of the file without a path and without the
     *            ".properties" extension. Examples of this would be "connection" or "gateway".
     **/
    public synchronized void setPropertyFile(String propertyFileName) {
        this.propertyFileName = propertyFileName;
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
    public synchronized String getProperty(String propertyFile, String propertyName) throws PropertyAccessException {                
        validateInput(propertyFile, propertyName);
        checkForRefreshAndLoad(propertyFile);

        return propertyFileDAO.getProperty(propertyFile, propertyName);
    }

    /**
     * This method returns the value of the given property that is located within the given property file. If the
     * properties have been cached and the cache is still fresh, then it will return the value from the cache. If the
     * properties are cached, but the cache is not fresh, then the cache will be updated with the current values in the
     * properties file and then the property will be returned. If the properties for that file are not cached at all,
     * the property will be retrieved from the properties file and returned.
     * 
     * @param propertyName This is the name of the property within the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    @Override
    public synchronized String getProperty(String propertyName) throws PropertyAccessException {
        return getProperty(propertyFileName, propertyName);
    }
    
    public synchronized boolean getPropertyBoolean(String propertyName) throws PropertyAccessException {
    	return getPropertyBoolean(propertyFileName, propertyName);
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
    public synchronized boolean getPropertyBoolean(String propertyFile, String propertyName) throws PropertyAccessException {
        validateInput(propertyFile, propertyName);
        checkForRefreshAndLoad(propertyFile);

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
    public synchronized long getPropertyLong(String propertyFile, String propertyName) throws PropertyAccessException {
        validateInput(propertyFile, propertyName);
        checkForRefreshAndLoad(propertyFile);

        return propertyFileDAO.getPropertyLong(propertyFile, propertyName);
    }

    /**
     * This method returns the set of keys in a property file.
     * 
     * @param propertyFile The name of the property file.
     * @return An enumeration of property keys in the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized final Set<String> getPropertyNames(String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        checkForRefreshAndLoad(propertyFile);

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
    public synchronized final Properties getProperties(String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        checkForRefreshAndLoad(propertyFile);

        return propertyFileDAO.getProperties(propertyFile);
    }

    /**
     * This will return the in milliseconds the refresh duration on the property file. A setting of -1 means it never
     * refreshes.
     * 
     * @param propertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized int getRefreshDuration(String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        checkForRefreshAndLoad(propertyFile);

        return refreshHandler.getRefreshDuration(propertyFile);
    }

    /**
     * This will return the duration in milliseconds before the next refresh of the properties file. A value of -1
     * indicates that no refresh will occur.
     * 
     * @param propertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized int getDurationBeforeNextRefresh(String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        checkForRefreshAndLoad(propertyFile);
        
        return refreshHandler.getDurationBeforeNextRefresh(propertyFile);
    }

    /**
     * If a property file has been cached, this will force a refresh of the property file. If a property file is not
     * cached, then this operation will do nothing.
     * 
     * @param propertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public synchronized void forceRefresh(String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        
        loadPropertyFile(propertyFile);
    }
   
    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public synchronized String getPropertyFileLocation() {
        return fileUtilities.getPropertyFileLocation();
    }
    
    public synchronized String getPropertyFileLocation(String propertyFileName) {
        return fileUtilities.getPropertyFileLocation(propertyFileName);
    }

    public synchronized void setPropertyFileLocation(String propertyFileDirAbsolutePath) {
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
    public void dumpPropsToLog(String propertyFile) throws PropertyAccessException {
        validateInput(propertyFile);
        
        refreshHandler.printToLog(propertyFile);
        propertyFileDAO.printToLog(propertyFile);
    }
    
    protected void validateInput(String... propertyStrings) throws PropertyAccessException {
        for (String propertyString: propertyStrings) {
            isPropertyStringValid(propertyString);
        }    
    }
    
    protected PropertyFileRefreshHandler createPropertyFileRefreshHandler() {
        return new PropertyFileRefreshHandler();
    }
    
    protected PropertyFileDAO createPropertyFileDAO() {
        return new PropertyFileDAO();
    }
    
    protected PropertyAccessorFileUtilities createPropertyAccessorFileUtilities() {
        return new PropertyAccessorFileUtilities();
    }
    
    /**
     * This method loads the property file and sets the refresh time. If the property: "CacheRefreshDuration" is found
     * in the property file, then it will set it as follows: If the value is "-1", then
     * 
     * @param propertyFile The name of the property file to be loaded.
     * @param refreshInfo The refresh information that we already know - this may be null if this is the first time the file
     *            is being loaded.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException This exception is thrown if it cannot load the
     *             property file for some reason.
     */
    private synchronized void loadPropertyFile(String propertyFile) throws PropertyAccessException {
        String propFilePathAndName = fileUtilities.getPropertyFileLocation(propertyFile);
               
        File propertyFileLocation = new File(propFilePathAndName);        
  //      propertyFileDAO.loadPropertyFile(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile), propertyFile);
        propertyFileDAO.loadPropertyFile(propertyFileLocation, propertyFile);
        String cacheRefreshDuration = propertyFileDAO.getProperty(propertyFile, CACHE_REFRESH_DURATION);
        refreshHandler.addRefreshInfo(propertyFile, cacheRefreshDuration);
    }

    /**
     * This method will check to see if the property file needs to be refreshed and if it does, it will reload it.
     * Otherwise it will leave it as is.
     * 
     * @param propertyFile The name of the property file that is being checked and possibly loaded.
     * @throws PropertyAccessException If an error occurs during the load process, this exception is thrown.
     */
    private synchronized void checkForRefreshAndLoad(String propertyFile) throws PropertyAccessException {        
        if (refreshHandler.needsRefresh(propertyFile)) {
            loadPropertyFile(propertyFile);
        }
    }
  
    private void isPropertyStringValid(String property) throws PropertyAccessException {        
        if (NullChecker.isNullish(property)) {
            throw new PropertyAccessException("Invalid property name value: " + property);
        }        
    }

}
