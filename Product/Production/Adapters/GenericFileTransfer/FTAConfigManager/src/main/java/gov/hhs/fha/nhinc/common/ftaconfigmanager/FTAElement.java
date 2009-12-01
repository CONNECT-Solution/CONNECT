/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.common.ftaconfigmanager;

/**
 *
 * @author dunnek
 */
public class FTAElement
{
    private String name = "";
    private String val = "";

    public FTAElement()
    {
        
    }
    public FTAElement(String elementName, String elementValue)
    {
        name = elementName;
        val = elementValue;
    }
    public String getName()
    {
        return name;
    }
    public String getValue()
    {
        return val;
    }
    public void setName(String value)
    {
        name = value;
    }
    public void setValue(String value)
    {
        val = value;
    }
}
