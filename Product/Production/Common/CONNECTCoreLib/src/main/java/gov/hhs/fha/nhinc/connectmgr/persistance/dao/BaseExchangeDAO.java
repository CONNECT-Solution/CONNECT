/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
                marshaller.marshal(factory.createExchangeInfo(exchangeInfo), file);
            }
        } catch (final JAXBException ex) {
            throw new ConnectionManagerException("Unable to save to Connection Information File " + file.getName(), ex);
        }
        LOG.info("Exchange info saved to " + file.getName());
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
