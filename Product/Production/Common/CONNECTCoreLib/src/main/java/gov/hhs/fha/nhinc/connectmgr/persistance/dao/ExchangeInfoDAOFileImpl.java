/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public final class ExchangeInfoDAOFileImpl extends AbstractConnectionManagerDAO<ExchangeInfoType> {

    private static ExchangeInfoDAOFileImpl instance = null;
    private File file = null;
    private static final Logger LOG = LoggerFactory.getLogger(ExchangeInfoDAOFileImpl.class);
    private static final String EXCHANGE_XML_FILE_NAME = "exchangeInfo.xml";

    private ExchangeInfoDAOFileImpl() {
        super();
        String fileName = getExchangeFileLocation();
        LOG.debug("Reading exchangeInfo from file: " + fileName);
        if (fileName != null) {
            file = new File(fileName);
        }
    }

    public static ExchangeInfoDAOFileImpl getInstance() {
        if (instance == null) {
            instance = new ExchangeInfoDAOFileImpl();
        }
        return instance;
    }

    public String getExchangeFileLocation() {
        if (file == null) {
            String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();
            if (sValue != null && sValue.length() > 0) {
                if (sValue.endsWith(File.separator)) {
                    setFileName(sValue + EXCHANGE_XML_FILE_NAME);
                } else {
                    setFileName(sValue + File.separator + EXCHANGE_XML_FILE_NAME);
                }
            }
        }

        return isFile() ? file.getAbsolutePath() : null;
    }

    private boolean isFile() {
        return file != null && file.exists();
    }

    public ExchangeInfoType loadExchangeInfo() throws ConnectionManagerException {
        if (!isFile()) {
            throw new ConnectionManagerException("Unable to access system variable: nhinc.properties.dir.");
        }
        ExchangeInfoType resp;
        resp = super.loadExchangeInfo(ExchangeInfoType.class, file);
        return resp;
    }

    public void saveExchangeInfo(ExchangeInfoType exchangeInfo) throws ConnectionManagerException {
        saveExchangeInfo(exchangeInfo, file);
    }

    public long getLastModified() {
        if (file.exists()) {
            return file.lastModified();
        } else {
            return 0;
        }
    }

    public void setFileName(String fileName) {
        file = new File(fileName);
    }

    @Override
    public void saveExchangeInfo(ExchangeInfoType exchangeInfo, File file) throws ConnectionManagerException {
        try {
            synchronized (file) {
                final JAXBContext context = JAXBContext.newInstance(ExchangeInfoType.class);
                final ObjectFactory factory = new ObjectFactory();
                final Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(factory.createExchangeInfo(exchangeInfo), file);
            }
        } catch (final JAXBException ex) {
            throw new ConnectionManagerException("Unable to save to Connection Information File " + file.getName(), ex);
        }
        LOG.info("Exchange info saved to " + file.getName());
    }
}
