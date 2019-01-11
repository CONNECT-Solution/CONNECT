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
package gov.hhs.fha.nhinc.transform.marshallers;

import java.util.Hashtable;
import javax.xml.bind.JAXBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to manage JAXB contexts. Once loaded a JAXB context is kept in static memory so that it does not
 * need to be loaded again when used again.
 *
 * @author Les Westberg
 *
 */
public class JAXBContextHandler {
    private static final Logger LOG = LoggerFactory.getLogger(JAXBContextHandler.class);

    // Contexts that are being managed. The name will be the context.
    // ----------------------------------------------------------------
    private static Hashtable<String, JAXBContext> hContexts = new Hashtable<>();

    /**
     * This method returns the JAXB context
     *
     * @param sContextName The name of the context. (i.e. "org.hl7.v3").
     * @return The JAXB context for that context name.
     * @throws javax.xml.bind.JAXBException
     */
    public JAXBContext getJAXBContext(final String sContextName) throws javax.xml.bind.JAXBException {
        if (sContextName != null && sContextName.length() > 0) {
            JAXBContext oContext = hContexts.get(sContextName);
            if (oContext == null) {
                LOG.debug("Loading JAXB Context for '" + sContextName + "'.");
                oContext = JAXBContext.newInstance(sContextName);
                hContexts.put(sContextName, oContext);
                LOG.debug("Finished loading JAXB Context for '" + sContextName + "'.");
            } else {
                LOG.debug("Reusing JAXB Context for '" + sContextName + "'.");
            }
            return oContext;
        } else {
            LOG.debug("Request for JAXB Context without a valid name.");
            return null;
        }
    }

    /**
     * This method returns the JAXB context
     *
     * @param oClass1 The object factory class for the first context.
     * @param oClass2 The object factory class for the second context.
     * @return The JAXB context for that context name.
     * @throws javax.xml.bind.JAXBException
     */
    public JAXBContext getJAXBContext(final Class oClass1, final Class oClass2) throws javax.xml.bind.JAXBException {
        JAXBContext oContext = null;
        String sContextName = "";
        if (oClass1 != null && oClass2 != null) {
            sContextName = oClass1.getPackage().getName() + "_" + oClass2.getPackage().getName();
        } else if (oClass1 != null) {
            sContextName = oClass1.getPackage().getName();
        } else if (oClass2 != null) {
            sContextName = oClass2.getPackage().getName();
        }

        if (sContextName != null && sContextName.length() > 0) {
            oContext = hContexts.get(sContextName);
            if (oContext == null) {
                LOG.debug("Loading JAXB Context for '" + sContextName + "'.");
                if (oClass1 != null && oClass2 != null) {
                    oContext = JAXBContext.newInstance(oClass1, oClass2);
                } else if (oClass1 != null) {
                    oContext = JAXBContext.newInstance(oClass1);
                } else if (oClass2 != null) {
                    oContext = JAXBContext.newInstance(oClass2);
                }
                LOG.debug("Finished loading JAXB Context for '" + sContextName + "'.");

                if (oContext != null) {
                    hContexts.put(sContextName, oContext);
                }
            } else {
                LOG.debug("Reusing JAXB Context for '" + sContextName + "'.");
            }
        } else {
            LOG.debug("Request for JAXB Context without object factory classes.");
        }

        return oContext;
    }

    /**
     * This method returns the JAXB context
     *
     * @param oClass The object factory class for the first context.
     * @return The JAXB context for that context name.
     * @throws javax.xml.bind.JAXBException
     */
    public JAXBContext getJAXBContext(final Class oClass) throws javax.xml.bind.JAXBException {
        JAXBContext oContext = null;
        String sContextName = oClass.getPackage().getName();

        if (sContextName != null && sContextName.length() > 0) {
            oContext = hContexts.get(sContextName);
            if (oContext == null) {
                LOG.debug("Loading JAXB Context for '" + sContextName + "'.");
                oContext = JAXBContext.newInstance(oClass);
                LOG.debug("Finished loading JAXB Context for '" + sContextName + "'.");

                if (oContext != null) {
                    hContexts.put(sContextName, oContext);
                }
            } else {
                LOG.debug("Reusing JAXB Context for '" + sContextName + "'.");
            }
        } else {
            LOG.debug("Request for JAXB Context without object factory classes.");
        }

        return oContext;
    }

}
