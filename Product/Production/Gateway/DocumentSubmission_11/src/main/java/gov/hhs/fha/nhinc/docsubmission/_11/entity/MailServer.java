package gov.hhs.fha.nhinc.docsubmission._11.entity;


import java.io.Serializable;


public class MailServer implements Serializable {


    private int id;

    private String attributename;

    private String attributevalue;

    private String type;

    public int getid() {
        return id;
    }

    public void setid(int rowid) {
	 this.id = rowid;
    }

    public String getattributename() {
        return attributename;
    }

    public void setattributename(String name) {
        this.attributename = name;
    }

    public String getattributevalue() {
        return attributevalue;
    }

    public void setattributevalue(String value) {
        this.attributevalue = value;
    }

    public String gettype() {
        return type;
    }

    public void settype(String storeType) {
        this.type = storeType;
    }

}
