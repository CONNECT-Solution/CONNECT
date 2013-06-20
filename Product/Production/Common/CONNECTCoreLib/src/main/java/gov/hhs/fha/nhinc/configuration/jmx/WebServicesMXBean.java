/**
 * 
 */
package gov.hhs.fha.nhinc.configuration.jmx;

/**
 * @author msw
 *
 */
public interface WebServicesMXBean {
    
    public void configureInboundImpl(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    public void configureInboundStd() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    public void configureInboundPassthru() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    public boolean isInboundPassthru();
    
    public void configureOutboundImpl(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    public void configureOutboundStd() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    public void configureOutboundPassthru() throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    public boolean isOutboundPassthru();

}
