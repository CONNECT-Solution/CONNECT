/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.ObjectFactory;

/**
 *
 * @author mweaver
 */
public class ConnectionManagerDAOBase {

    private static final String CONTEXT_PATH = "org.uddi.api_v3";
    private Log log = null;

    protected BusinessDetail loadBusinessDetail(File file) throws JAXBException {
        BusinessDetail resp = null;
        synchronized (file) {
            JAXBContext context = JAXBContext.newInstance(CONTEXT_PATH);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            resp = ((JAXBElement<BusinessDetail>) unmarshaller.unmarshal(file)).getValue();
        }
        return resp;
    }

    protected void saveBusinessDetail(BusinessDetail BusinessDetail, File file) {
        try {
            synchronized (file) {
                JAXBContext context = JAXBContext.newInstance(CONTEXT_PATH);
                ObjectFactory factory = new ObjectFactory();
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(factory.createBusinessDetail(BusinessDetail), file);
            }
        } catch (JAXBException ex) {
            throw new RuntimeException("Unable to save to Connection Information File " + file.getName(), ex);
        }
        getLogger().info("Connection info saved to " + file.getName());
    }

    protected Log getLogger() {
        if (log == null) {
            setLogger(LogFactory.getLog(this.getClass()));
        }
        return log;
    }

    protected void setLogger(Log log) {
        this.log = log;
    }
}
