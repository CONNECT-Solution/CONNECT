package gov.hhs.fha.nhinc.docquery.nhin.proxy;

public interface NhinDocQueryProxyFactory {

    /**
     * @return Bean Instantiate NhinDocQuery Bean to send DocQuery Requests.
     */
    public NhinDocQueryProxy getNhinDocQueryProxy();

}