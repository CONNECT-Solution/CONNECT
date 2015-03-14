
package gov.hhs.fha.nhinc.admingui.client.fhir;

import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hl7.fhir.instance.client.EFhirClientException;
import org.hl7.fhir.instance.client.FeedFormat;
import org.hl7.fhir.instance.client.ResourceAddress;
import org.hl7.fhir.instance.client.ResourceFormat;
import org.hl7.fhir.instance.client.ResourceRequest;
import org.hl7.fhir.instance.client.TagParser;
import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.formats.Parser;
import org.hl7.fhir.instance.formats.XmlParser;
import org.hl7.fhir.instance.model.AtomCategory;
import org.hl7.fhir.instance.model.AtomEntry;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.OperationOutcome;
import org.hl7.fhir.instance.model.Resource;

/**
 *
 * @author jassmit
 */
public class ConformanceClient {

    public static String DEFAULT_CHARSET = "utf-8";
    
    public Conformance getConformanceStatement(String baseServiceUrl) throws URISyntaxException {
        ResourceAddress resourceAddress = new ResourceAddress(baseServiceUrl);
        return (Conformance) issueGetResourceRequest(resourceAddress.resolveMetadataUri(),  ResourceFormat.RESOURCE_XML.getHeader()).getResource();
    }
    
    protected static <T extends Resource> ResourceRequest<T> issueGetResourceRequest(URI resourceUri, String resourceFormat) {
		HttpGet httpget = new HttpGet(resourceUri);
		return issueResourceRequest(resourceFormat, httpget);
	}

    protected static <T extends Resource> ResourceRequest<T> issueResourceRequest(String resourceFormat, HttpUriRequest request) {
        configureFhirRequest(request, resourceFormat);
        HttpResponse response = sendRequest(request);
        
        T resource = unmarshalResource(response, resourceFormat);
        AtomEntry<T> atomEntry = buildAtomEntry(response, resource);
        return new ResourceRequest<T>(atomEntry, response.getStatusLine().getStatusCode());
    }

    protected static void configureFhirRequest(HttpRequest request, String format) {
        request.addHeader("User-Agent", "Java FHIR Client for FHIR");
        request.addHeader("Accept", format);
        request.addHeader("Content-Type", format + ";charset=" + DEFAULT_CHARSET);
        request.addHeader("Accept-Charset", DEFAULT_CHARSET);
    }
    
    protected static HttpResponse sendPayload(HttpEntityEnclosingRequestBase request, byte[] payload) {
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            request.setEntity(new ByteArrayEntity(payload));
            response = httpclient.execute(request);
        } catch (IOException ioe) {
            throw new EFhirClientException("Error sending HTTP Post/Put Payload", ioe);
        }
        return response;
    }
    
    protected static HttpResponse sendRequest(HttpUriRequest request) {
		HttpResponse response = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			response = httpclient.execute(request);
		} catch(IOException ioe) {
			throw new EFhirClientException("Error sending Http Request", ioe);
		}
		return response;
	}
    
    @SuppressWarnings("unchecked")
	protected static <T extends Resource> T unmarshalResource(HttpResponse response, String format) {
		T resource = null;
		InputStream instream = null;
		HttpEntity entity = response.getEntity();
		if (entity != null && entity.getContentLength() > 0) {
			try {
			    instream = entity.getContent();
			    resource = (T)getParser(format).parse(instream);
			} catch(IOException ioe) {
				throw new EFhirClientException("Error unmarshalling entity from Http Response", ioe);
			} catch(Exception e) {
				throw new EFhirClientException("Error parsing response message", e);
			} finally {
                StreamUtils.closeStreamSilently(instream);
			}
		}
		if(resource instanceof OperationOutcome) {
			if(((OperationOutcome) resource).getIssue().size() > 0) {
                throw new EFhirClientException((OperationOutcome)resource);
			} else {
				System.out.println(((OperationOutcome) resource).getText().getDiv().allText());//TODO change to formal logging
			}
		}
		return resource;
	}
    
    protected static Parser getParser(String format) {
		if(StringUtils.isBlank(format)) {
			format = ResourceFormat.RESOURCE_XML.getHeader();
		}
		if(format.equalsIgnoreCase("json") || format.equalsIgnoreCase(ResourceFormat.RESOURCE_JSON.getHeader()) || format.equalsIgnoreCase(FeedFormat.FEED_JSON.getHeader())) {
			return new JsonParser();
		} else if(format.equalsIgnoreCase("xml") || format.equalsIgnoreCase(ResourceFormat.RESOURCE_XML.getHeader()) || format.equalsIgnoreCase(FeedFormat.FEED_XML.getHeader())) {
			return new XmlParser();
		} else {
			throw new EFhirClientException("Invalid format: " + format);
		}
	}
    
    protected static <T extends Resource> AtomEntry<T> buildAtomEntry(HttpResponse response, T resource) {
		AtomEntry<T> entry = new AtomEntry<T>();
		String location = null;
		if(response.getHeaders("location").length > 0) {//TODO Distinguish between both cases if necessary
    		location = response.getHeaders("location")[0].getValue();
    	} else if(response.getHeaders("content-location").length > 0) {
    		location = response.getHeaders("content-location")[0].getValue();
    	}
		if(location != null) {
			entry.getLinks().put("self", location);//TODO Make sure this is right.
		}
		List<AtomCategory> tags = parseTags(response);
		entry.getTags().addAll(tags);

		entry.setResource(resource);
		return entry;
	}
    
    protected static List<AtomCategory> parseTags(HttpResponse response) {
		List<AtomCategory> tags = new ArrayList<AtomCategory>();
		Header[] categoryHeaders = response.getHeaders("Category");
		for(Header categoryHeader : categoryHeaders) {
			if(categoryHeader == null || categoryHeader.getValue().trim().isEmpty()) {
				continue;
			}
			List<AtomCategory> categories = new TagParser().parse(categoryHeader.getValue());
			tags.addAll(categories);
		}
		return tags;
	}
}
