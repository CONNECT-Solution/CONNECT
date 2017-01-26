package gov.hhs.fha.nhinc.callback.openSAML;

import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Subject;

public class BuildEvidenceParameter {
	private String evAssertionID;
	private DateTime issueInstant;
	private String format;
	private DateTime beginValidTime;
	private DateTime endValidTime;
	private final String issuer;
	private final List<AttributeStatement> statements;
	private final Subject subject;

	public BuildEvidenceParameter(String evAssertionID, DateTime issueInstant, String format, DateTime beginValidTime,
			DateTime endValidTime, String issuer, List<AttributeStatement> statements, Subject subject) {
		this.evAssertionID = evAssertionID;
		this.issueInstant = issueInstant;
		this.format = format;
		this.beginValidTime = beginValidTime;
		this.endValidTime = endValidTime;
		this.issuer = issuer;
		this.statements = statements;
		this.subject = subject;
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

	public List<AttributeStatement> getStatements() {
		return statements;
	}

	public Subject getSubject() {
		return subject;
	}
}