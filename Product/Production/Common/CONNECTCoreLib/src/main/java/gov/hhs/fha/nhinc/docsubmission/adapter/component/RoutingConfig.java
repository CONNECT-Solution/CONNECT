/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.adapter.component;

/**
 *
 * @author dunnek
 */
public class RoutingConfig {
    private String recipient;
    private String bean;

    public String getRecepient()
    {
        return recipient;
    }
    public String getBean()
    {
        return bean;
    }
    public void setRecepient(String value)
    {
        recipient = value;
    }
    public void setBean(String value)
    {
        bean = value;
    }


}
