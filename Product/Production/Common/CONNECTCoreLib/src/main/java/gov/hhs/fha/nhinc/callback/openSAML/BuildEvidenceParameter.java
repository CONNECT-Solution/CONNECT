package gov.hhs.fha.nhinc.callback.openSAML;

import java.util.List;
import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Subject;

public class BuildEvidenceParameter {

    private CallbackProperties properties;
    private String evAssertionID;
    private DateTime issueInstant = null;
    private String format = null;
    private DateTime beginValidTime = null;
    private DateTime endValidTime = null;
    private String issuer = null;
    private List<AttributeStatement> statements;
    private Subject subject;

    public BuildEvidenceParameter(String evAssertionID, String issuer, CallbackProperties properties,
        List<AttributeStatement> statements, Subject subject) {
        this.evAssertionID = evAssertionID;
        this.properties = properties;
        this.statements = statements;
        this.subject = subject;
        this.issuer = issuer;
        setProperties(properties);
    }

    public CallbackProperties getProperties() {
        return properties;
    }

    public void setProperties(CallbackProperties properties) {
        setEvAssertionID(evAssertionID);
        issueInstant = properties.getEvidenceInstant();
        setIssueInstant(issueInstant);
        format = properties.getEvidenceIssuerFormat();
        setFormat(format);
        beginValidTime = properties.getEvidenceConditionNotBefore();
        setBeginValidTime(beginValidTime);
        endValidTime = properties.getEvidenceConditionNotAfter();
        setEndValidTime(endValidTime);
        setIssuer(issuer);
        this.properties = properties;
    }

    public String getEvAssertionID() {
        return evAssertionID;
    }

    public void setEvAssertionID(String evAssertionID) {
        this.evAssertionID = evAssertionID;
    }

    public DateTime getIssueInstant() {
        return issueInstant;
    }

    public void setIssueInstant(DateTime issueInstant) {
        this.issueInstant = issueInstant;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public DateTime getBeginValidTime() {
        return beginValidTime;
    }

    public void setBeginValidTime(DateTime beginValidTime) {
        this.beginValidTime = beginValidTime;
    }

    public DateTime getEndValidTime() {
        return endValidTime;
    }

    public void setEndValidTime(DateTime endValidTime) {
        this.endValidTime = endValidTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public List<AttributeStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<AttributeStatement> statements) {
        this.statements = statements;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
