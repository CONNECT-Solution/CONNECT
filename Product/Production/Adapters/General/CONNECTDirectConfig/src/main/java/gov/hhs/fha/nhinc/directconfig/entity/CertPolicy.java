package gov.hhs.fha.nhinc.directconfig.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;

import org.nhindirect.policy.PolicyLexicon;

@Entity
@Table(name = "certpolicy")
public class CertPolicy 
{
    private long id;
    private String policyName;
    private PolicyLexicon lexicon;
    private byte[] policyData;
    private Calendar createTime;      
    
    public CertPolicy()
    {
        
    }
    
    /**
     * Get the value of id.
     * 
     * @return the value of id.
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() 
    {
        return id;
    }
    
    /**
     * Set the value of id.
     * 
     * @param id
     *            The value of id.
     */
    public void setId(long id) 
    {
        this.id = id;
    } 
    
    @Enumerated
    @Column(name = "lexicon", nullable = false)
    @XmlAttribute
    public PolicyLexicon getLexicon()
    {
        return lexicon;
    }
    
    public void setLexicon(PolicyLexicon lexicon)
    {
        this.lexicon = lexicon;
    }
    
    /**
     * Get the value of policyName.
     * 
     * @return the value of policyName.
     */
    @Column(name = "policyName", unique = true)
    public String getPolicyName() 
    {
        return policyName;
    }    

    
    /**
     * Gets the value of policyName.
     * @param policyName Get the value of policyName.
     */
    public void setPolicyName(String policyName)
    {
        this.policyName = policyName;
    }
    
    @Column(name = "data", nullable = false, length=204800)
    @Lob
    public byte[] getPolicyData()
    {
        return policyData;
    }
    
    public void setPolicyData(byte[] policyData)
    {
        this.policyData = policyData;
    }
    
    /**
     * Get the value of createTime.
     * 
     * @return the value of createTime.
     */
    @Column(name = "createTime", nullable = false)    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getCreateTime() 
    {
        return createTime;
    }

    /**
     * Set the value of createTime.
     * 
     * @param timestamp
     *            The value of createTime.
     */
    public void setCreateTime(Calendar timestamp) 
    {
        createTime = timestamp;
    }       
}
