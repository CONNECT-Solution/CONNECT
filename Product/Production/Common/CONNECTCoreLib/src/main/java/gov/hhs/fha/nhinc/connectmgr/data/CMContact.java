package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:57 PM
 */
public class CMContact
{

    private CMContactDescriptions descriptions = null;
    private CMPersonNames personNames = null;
    private CMPhones phones = null;
    private CMEmails emails = null;
    private CMAddresses addresses = null;

    /**
     * Default Constructor.
     */
    public CMContact()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        descriptions = null;
        personNames = null;
        phones = null;
        emails = null;
        addresses = null;
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMContact oCompare)
    {
        if (!this.descriptions.equals(oCompare.descriptions))
        {
            return false;
        }
        
        if (!this.personNames.equals(oCompare.personNames))
        {
            return false;
        }
        
        if (!this.phones.equals(oCompare.phones))
        {
            return false;
        }
        
        if (!this.emails.equals(oCompare.emails))
        {
            return false;
        }
        
        if (!this.addresses.equals(oCompare.addresses))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    

    /**
     * Returns the list of addresses for this contact.
     * 
     * @return The list of addresses for this contact.
     */
    public CMAddresses getAddresses()
    {
        return addresses;
    }

    /**
     * Sets the list of addresses for this contact.
     * 
     * @param addresses The list of addresses for this contact.
     */
    public void setAddresses(CMAddresses addresses)
    {
        this.addresses = addresses;
    }

    /**
     * Returns the set of descriptions for this contact.
     * 
     * @return The set of descriptions for this contact.
     */
    public CMContactDescriptions getDescriptions()
    {
        return descriptions;
    }

    /**
     * Sets the set of descriptions for this contact.
     * 
     * @param descriptions The set of descriptions for this contact.
     */
    public void setDescriptions(CMContactDescriptions descriptions)
    {
        this.descriptions = descriptions;
    }

    /**
     * Returns the email addresses for this contact.
     * 
     * @return The email addresses for this contact.
     */
    public CMEmails getEmails()
    {
        return emails;
    }

    /**
     * Sets the email addresses for this contact.
     * 
     * @param emails The email addresses for this contact.
     */
    public void setEmails(CMEmails emails)
    {
        this.emails = emails;
    }

    /**
     * Returns the names for this contact.
     * 
     * @return The names for this contact.
     */
    public CMPersonNames getPersonNames()
    {
        return personNames;
    }

    /**
     * Sets the names for this contact.
     * 
     * @return The names for this contact.
     */
    public void setPersonNames(CMPersonNames personNames)
    {
        this.personNames = personNames;
    }

    /**
     * Returns the phone numbers for this contact.
     * 
     * @return The phone numbers for this contact.
     */
    public CMPhones getPhones()
    {
        return phones;
    }

    /**
     * Sets the phone numbers for this contact.
     * 
     * @param phones The phone numbers for this contact.
     */
    public void setPhones(CMPhones phones)
    {
        this.phones = phones;
    }
}