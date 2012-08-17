/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.properties;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author A22387
 */
public class PropertyLoader
{
    private static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
    private static String sPropertyFile = "";
    private static String sPropertyFileName = "adapter_common_datalayer.properties";
    private final static Logger logger = Logger.getLogger(PropertyLoader.class.getName());
    static
    {
        //set local variable to the NHINC_PROPERTIES_DIR environment variable
        String sysValue = System.getenv(NHINC_PROPERTIES_DIR);

        //check environment variable for contents
        if ((sysValue != null) && (sysValue.length() >0))
        {
            logger.info("Retrieving adapter_common_datalayer properties file...");

            //ensure the environment variable ends with a file seperator
            if ((sysValue.endsWith("/")) || (sysValue.endsWith("\\")))
            {
                sPropertyFile = sysValue;
            }
            else
            {
                String sFileSeperator = System.getProperty("file.separator");
                sPropertyFile = sysValue + sFileSeperator + sPropertyFileName;
            }
            logger.info("Retrieved adapter_common_datalayer properties file successfully.");
        }
        else
        {
            logger.severe("Path to adapter_common_datalayer property file not found");
        }
    }

    /**
     * Default Constructor
     */
    private PropertyLoader()
    {
        super();
    }

   

     /**
     * Loads the properties from the location specified
     * by the parameters provided
     *
     * WARNING: The package name must match the package of
     * this class for some application servers.
     * You have been warned!!!
     *
     * @param sPackageName Package name of the file
     * @param sPropertiesFileName Name of the properties file
     * @return Properties Properties contained in the file
     * @throws PropertiesException
     */
    public static Properties getProperties(String sPackageName,
        String sPropertiesFileName) throws PropertiesException
    {
        //declare local variable
        Properties oProperties = null;
        FileReader frPropFile = null;

        try
        {
            //instantiate Properties
            oProperties = new Properties();

            //create File object
            File fPropFile = new File(sPropertyFile);

            //retrieve and load properties file
            frPropFile = new FileReader(fPropFile);
            oProperties.load(frPropFile);

        }
        catch (Exception ex)
        {
            logger.severe("Error in getProperties: " + ex);
        }
        return oProperties;
    }

    
}