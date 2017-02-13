package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.PrescriptionClassSearch;
import gov.hhs.fha.nhinc.rx.Rxclassdata;
import gov.hhs.fha.nhinc.rx.Rxclassdata.RxclassDrugInfoList;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jassmit
 */
@Service
public class PrescriptionClassSearchImpl implements PrescriptionClassSearch {

    private final static Logger logger = LoggerFactory.getLogger(PrescriptionClassSearch.class);
    private static final String serviceUrl = "https://rxnav.nlm.nih.gov/REST/rxclass/class/byDrugName.xml?drugName=";

    @Override
    public String searchForDrugClass(String drugName) {
        WebClient restClient = WebClient.create(serviceUrl + drugName)
                .accept(MediaType.APPLICATION_XML);
        Rxclassdata response = restClient.get(Rxclassdata.class);

        String className = getDrugClassName(response);
        logger.debug("Input Drug {} ,ClassName: {}", drugName, className);

        return className;
    }

    private String getDrugClassName(final Rxclassdata drugInfor) {
        String className = ""; // default value;
        // Retrieve first in the list
        RxclassDrugInfoList rxclassDrugInfoList = drugInfor.getRxclassDrugInfoList();
        if (rxclassDrugInfoList != null
                && rxclassDrugInfoList.getRxclassDrugInfo().get(0).getRxclassMinConceptItem() != null) {
            className = drugInfor.getRxclassDrugInfoList().getRxclassDrugInfo().get(0).getRxclassMinConceptItem()
                    .getClassName();
        }

        return className;
    }

}
