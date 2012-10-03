package gov.hhs.fha.nhinc.connectmgr.uddi.proxy;

import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.GetBusinessDetail;

public interface UDDIFindBusinessProxy {

    /**
     * Override in implementation class
     *
     * @return list of businesses from UDDI
     * @throws UDDIFindBusinessException
     */
    public abstract BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException;

    public abstract BusinessDetail getBusinessDetail(GetBusinessDetail searchParams) throws UDDIFindBusinessException;

}