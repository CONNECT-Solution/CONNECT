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
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ObjectFactory;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public abstract class BaseExchangeDAO extends AbstractConnectionManagerDAO<ExchangeInfoType> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseExchangeDAO.class);

    @Override
    public void saveExchangeInfo(ExchangeInfoType exchangeInfo, File file) throws ConnectionManagerException {
        try {
            synchronized (file) {
                final JAXBContext context = JAXBContext.newInstance(ExchangeInfoType.class);
                final ObjectFactory factory = new ObjectFactory();
                final Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                DOMResult domResult = new DOMResult();
                marshaller.marshal(factory.createExchangeInfo(exchangeInfo), domResult);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.transform(new DOMSource(domResult.getNode()), new StreamResult(file));

            }
        } catch (final JAXBException | TransformerException ex) {
            throw new ConnectionManagerException("Unable to save to Connection Information File " + file.getName(), ex);
        }
        LOG.info("Exchange info saved to {}", file.getName());
    }

    public ExchangeInfoType loadExchangeInfo() throws ExchangeManagerException {
        if (!isFile()) {
            throw new ExchangeManagerException("Unable to access system variable: nhinc.properties.dir.");
        }
        ExchangeInfoType resp;
        try {
            resp = super.loadExchangeInfo(ExchangeInfoType.class, getFile());
        } catch (ConnectionManagerException ex) {
            throw new ExchangeManagerException(ex);
        }
        return resp;
    }

    public void saveExchangeInfo(ExchangeInfoType exchangeInfo) throws ExchangeManagerException {
        try {
            saveExchangeInfo(exchangeInfo, getFile());
        } catch (ConnectionManagerException ex) {
            throw new ExchangeManagerException(ex);
        }
    }

    protected boolean isFile() {
        return getFile() != null && getFile().exists();
    }

    protected abstract File getFile();

    public long getLastModified() {
        if (getFile().exists()) {
            return getFile().lastModified();
        } else {
            return 0;
        }
    }
}
