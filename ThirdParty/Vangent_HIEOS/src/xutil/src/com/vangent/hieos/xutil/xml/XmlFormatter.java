/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.vangent.hieos.xutil.xml;



public class XmlFormatter {
    private String in;
    private int indent;
    private int indentAmount;
    private StringBuffer buf;
    private int currentPos;
    private int length;
    private int nextParmStart, nextParmEnd;
    StringBuffer diagBuf;
    boolean diagOn;
    boolean xmlHeader;
    boolean noOut;
    boolean useHtmlEscapes;
    String uriPrefix;

    public class ElementParser {
	String tagName;
	boolean closes;
	StringBuffer b;
	int index;

	public String print() {
	    return "ElementParser: tagName = " + tagName + " closes = " + closes;
	}

	ElementParser(StringBuffer b, int index, int direction) {
	    this.b = b;
	    this.index = index;
	    if (direction == -1)
		this.parseBackwards();
	    else
		this.parseForwards();
	}

	void parseBackwards() {
	    int i = index;
	    char c = b.charAt(i);
	    closes = false;
	    while(i > -1 && c != '<') {
		if (c == '/') closes = true;
		i--;
		c = b.charAt(i);
	    }
	    if (b.charAt(i) == '<')
		i++;
	    if (b.charAt(i) == '/')
		i++;
	    int from = i;
	    while (b.charAt(i) != ' ' && b.charAt(i) != '>' && b.charAt(i) != '/') {
		i++;
	    }
	    tagName = b.substring(from, i);
	}
	
	void parseForwards() {
	    int i = index;
	    char c = b.charAt(i);
	    closes = false;
	    tagName = "";
	    while (i < b.length() && c != '>') {
		if (c == '/') closes = true;
		i++;
		if (i>=b.length())
		    return;
		c = b.charAt(i);
	    }
	    int end = i;
	    while (i > -1  && b.charAt(i) != '<')
		i--;
	    i++;
	    if (b.charAt(i) == '/') i++;
	    int start = i;
	    while (b.charAt(i) != ' ' && b.charAt(i) != '/' && b.charAt(i) != '>') i++;
	    tagName = b.substring(start, i);
	}
	
	
    }
    
    public XmlFormatter(String inXml) {
        in = XmlFormatter.normalize(inXml);
        length = in.length();
        buf = new StringBuffer(in.length());
        diagBuf = new StringBuffer();
        indent = 0;
        currentPos = 0;
        nextParmStart = -1;
        nextParmEnd = -1;
        indentAmount = 3;
        diagOn = false;
        noOut = false;
        useHtmlEscapes = false;
        uriPrefix = null;
    }
    
    void out(char s) {
        if (noOut) return;
        buf.append(s);
    }
    
    void out(String s) {
        if (noOut) return;
        buf.append(s);
    }
    
    void diag(String s) {
        if (diagOn)
            diagBuf.append(s);
    }
    
    void diagln(String s) {
        diag(s);
        diag("\n");
    }
    
    char c(int pos) {
        return (cvalid(pos)) ? in.charAt(pos) : ' ';
    }
    
    char co(int offset) {
        return ((currentPos+offset)<length) ? in.charAt(currentPos+offset)
        : ' ';
    }
    
    boolean hasPrefix(String prefix) {
        for (int i=0; i<prefix.length(); i++) {
            if (prefix.charAt(i) != co(i))
                return false;
        }
        return true;
    }
    
    void hyperlinkUuid() {
        if (in.charAt(currentPos+45) != '"')
            return;  // not valid uuid
        String uuid = in.substring(currentPos, currentPos+44); // uuid is 45 chars
        buf.append("<a href=\"" + uriPrefix + uuid + "\">" + uuid + "</a>");
        
        currentPos += 45;
    }
    
    boolean covalid(int offset) {
        return (currentPos+offset >= 0) && (currentPos+offset)<length;
    }
    
    boolean cvalid(int pos) {
        return (pos >= 0) && pos<length;
    }
    
    boolean endOfBuffer() {
        return !(currentPos < length);
    }
    
    void next() {
        currentPos++;
    }
    
    int findNext(int startAt, char ch) {
        for (int i=startAt; cvalid(i); i++) {
            if (c(i) == ch)
                return i;
        }
        return -1;
    }
    
    int findNext(char ch) {
        return findNext(currentPos, ch);
    }
    
    int findPrev(int startAt, char ch) {
        for (int i=startAt; cvalid(i); i--) {
            if (c(i) == ch)
                return i;
        }
        return -1;
    }
    
    int findPrev(char ch) {
        return findPrev(currentPos, ch);
    }
    
    int parmStart(int eq) {
        int i = eq;
        // pass spaces
        while (cvalid(i) && c(i) == ' ') i--;
        if (cvalid(i) && c(i) == '=') {
            i--;
            // pass spaces
            while (cvalid(i) && c(i) == ' ') i--;
        }
        // start of parm name
        while (cvalid(i) && c(i) != ' ' && c(i) != '\t') i--;
        i++;
        return i;
    }
    
    int parmEnd(int eq) {
        int i = eq;
        // pass spaces
        while (cvalid(i) && c(i) != '"') i++;
        // pass "
        i++;
        // find end of quote
        while (cvalid(i) && c(i) != '"') i++;
        return i;
    }
    
    String snippet(int centeredAt) {
        int amount = 2;
        if ( !cvalid(centeredAt-amount) || !cvalid(centeredAt+amount))
            return " ";
        return in.substring(centeredAt-amount, centeredAt+amount+1);
    }
    
    void findNextParm(int startingAt) {
        diagln("strt@ " + snippet(startingAt));
        nextParmStart = -1;
        int nextEq = findNext(startingAt, '=');
        if ( !cvalid(nextEq) )
            return;
        diagln("=@ " + snippet(nextEq));
        int nextClose = findNext(startingAt, '>');
        diagln(">@ " + snippet(nextClose));
        if (nextClose < nextEq)
            return;
        nextParmStart = parmStart(nextEq);
        nextParmEnd = parmEnd(nextEq);
        diagln("a@ " + snippet(nextParmStart));
        diagln("z@ " + snippet(nextParmEnd));
        diagln("parm " + in.substring(nextParmStart, nextParmEnd+1));
    }
    
    void findNextParm() {
        findNextParm(currentPos);
    }
    
    boolean atParmStart() {
        return nextParmStart == currentPos;
    }
    
    void doIndent() {
        if (useHtmlEscapes)
            out("<br/>");
        else
            out('\n');
        //	out("(");
        //	out(indent);
        //	out(")");
        for (int j=0; j<indent; j++) 
            if (useHtmlEscapes)
                out("&nbsp;");
            else
                out(' ');
    }
    
    void handleUri() {
        int start = findNext('"');
        int end = findNext(start+1,'"');
        start++;
        String uri = in.substring(start,end);
        if (!in.substring(start, start+9).equals("urn:uuid:"))
            return;
        diagln("uri is " + uri);
        char c;
        while ((c=co(0))!='"') {
            out(c);
            next();
        }
        out(co(0)); // output "
        next();
        
        buf.append("<a target=\"mainFrame\" href=\"" + uriPrefix + uri +"\">");
        while ((c=co(0))!='"') {
            out(c);
            next();
        }
        
        buf.append("</a>");
        
        out(co(0)); // output "
        next();
        
    }
    
    boolean isWhite(char c) {
        if (c == ' ') return true;
        if (c == '\n') return true;
        if (c == '\t') return true;
        if (c == '\r') return true;
        return false;
    }
    
    boolean prevIsTextNode() {
        for (int i= -1; ; i--) {
            if (!covalid(i))
                return false;
            if (co(i) == '>')
                return false;
            if (!isWhite(co(i)))
                return true;
        }
    }
    
    void doParmStart() {
        doIndent();
        if (uriPrefix != null)
            handleUri();
        findNextParm(nextParmEnd);
    }
    
    void newOpen() {
        findNextParm();
    }
    
    void newClose() {
    }
    
    String run() {
        char cc;
        while( ! endOfBuffer() ) {
            cc = co(0);
            if (atParmStart())
                doParmStart();
            if (co(0) == '<') {
                if (co(1) == '/') {
                    if (prevIsTextNode()) {
                        indent -= indentAmount;
                    } else {
                        indent -= indentAmount;
                        doIndent();
                        newClose();
                    }
                } else if (co(1) == '?') {
                    doIndent();
                    if (!xmlHeader)
                        noOut = true;
                } else {
                    doIndent();
                    indent += indentAmount;
                    newOpen();
                    
                }
                out(useHtmlEscapes ? "&lt;" : "<");
            } else if (co(0) == '>') {
                out(useHtmlEscapes ? "&gt;" : ">");
                noOut = false;
                if (co(-1) == '/')
                    indent -= indentAmount;
            } else if (co(0) == '&') {
                out("&amp;");
            } else
                out(co(0));
            next();
        }
        if (useHtmlEscapes) 
            return buf.toString() + "<br/><br/>" + diagBuf.toString();
        return buf.toString() + "\n\n" + diagBuf.toString();
    }
    
    static public String normalize(String inXml) {
	if (inXml.length() == 0)
	    return inXml;
	StringBuffer b = new StringBuffer(inXml);
	b.append("     ");
	boolean inElement = false;
	boolean inString=false;
	boolean isClosed=false;
	if (b.charAt(0) == '<')
	    inElement = true;
	for (int i=0; i<b.length()-5; ) {
	    char c_1 = (i == 0) ? 'z' : b.charAt(i-1);
	    char c = b.charAt(i);
	    char c1 = b.charAt(i+1);
	    char c2 = b.charAt(i+2);
	    if (c == '\n') {
		b.setCharAt(i, ' ');
		continue;
	    }
	    if (c == '\t') {
		b.setCharAt(i, ' ');
		continue;
	    }
	    if (c == ' ' && c1 == ' ' && ! isTextNode(b, i)) {
		b.deleteCharAt(i);
		continue;
	    }
	    if (c_1 == '>' && c == ' ') {
		b.deleteCharAt(i);
		continue;
	    }
	    if (c == ' ' && c1 == '<') {
		b.deleteCharAt(i);
		continue;
	    }
	    if (c_1 == '>' && c == '<' && c1 == '/') {
		if (tagName(b, i-1).equals(tagName(b, i))) {
		    deleteSimpleElement(b, i);
		    b.insert(i-1, '/');
		    continue;
		}
	    }

	    isClosed = isClosed(b, i);
	    inString = (c == '"') ? false : true;

	    i++;
	    c = b.charAt(i);
	    if (c == '<') inElement = true;
	    else if (c == '>') inElement = false;
	    else if (c == '"')	inString = (inString) ? false : true;
	}
	/*	for (int i=0; i<b.length(); i++ ) {
	    if (b.charAt(i) == '>' && b.charAt(i+1) == ' ' && b.charAt(i+2) == '<') {
		b.deleteCharAt(i+1);
	    }
	}
	for (int i=0; i<b.length(); i++ ) {
	    if (b.charAt(i) == '>' && b.charAt(i+1) == '<' && b.charAt(i+2) == '/') {
		if (tagName(b, i).equals(tagName(b, i+1))) {
		    deleteSimpleElement(b, i+1);
		    b.insert(i, '/');
		}
	    }
	    } */
	for (int i=b.length()-1; b.charAt(i) ==' '; i=b.length()-1) {
	    b.deleteCharAt(i);
	}
	return b.toString();
    }

    XmlFormatter() {}

    static public boolean isTextNode(StringBuffer b, int index) {
	XmlFormatter xf = new XmlFormatter();
	return xf.isaTextNode(b, index);
    }

    private boolean isaTextNode(StringBuffer b, int index) {
	int i = index;

	ElementParser left = new ElementParser(b, index, -1);
	ElementParser right = new ElementParser(b, index, 1);
	//	System.out.println(".....................");
	//	System.out.println(left.print());
	//	System.out.println(right.print());
	if (left.tagName.equals(right.tagName) && left.closes == false && right.closes == true)
	    return true;

	return false;
    }

    static private boolean isClosed(StringBuffer b, int index) {
	while(index > -1) {
	    if (b.charAt(index) == '/')
		return true;
	    if (b.charAt(index) == '<')
		return false;
	    index--;
	}
	return false;
    }

    static public void deleteSimpleElement(StringBuffer b, int index) {
	int start = index;

	while (b.charAt(start) != '<' && start > 0)
	    start--;

	int end = index;
	while (b.charAt(end) != '>')
	    end++;
	b.delete(start, end+1);
    }

    static public String tagName(StringBuffer b, int index) {
	int start = index;

	while (b.charAt(start) != '<' && start > 0)
	    start--;
	start++;

	if (b.charAt(start) == '/')
	    start++;

	int end = start;
	while (b.charAt(end) != '/' && b.charAt(end) != '>' && b.charAt(end) != ' ')
	    end++;
	end--;
	return b.substring(start, end+1);
    }
    
    static public String htmlize(String inXml, boolean xmlHeader) {
        XmlFormatter f = new XmlFormatter(inXml);
        f.xmlHeader = xmlHeader;
        f.useHtmlEscapes = true;
        return f.run();
    }
    
    static public String htmlize(String inXml) {
        return htmlize(inXml, true);
    }
    
    static public String format(String inXml, boolean xmlHeader) {
        XmlFormatter f = new XmlFormatter(inXml);
        f.xmlHeader = xmlHeader;
        f.useHtmlEscapes = false;
        return f.run();
    }
    
    static public String format(String inXml, boolean xmlHeader, String uriPrefix) {
        XmlFormatter f = new XmlFormatter(inXml);
        f.xmlHeader = xmlHeader;
        f.useHtmlEscapes = true;
        f.uriPrefix = uriPrefix;
        return f.run();
    }
    
    static public void main(String[] argvs) {
        //        String inXml = "<foo a=\"urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42a\"><bar/><a>Hi</a></foo>";
        String inXml = "<foo><a>Hi</a></foo>";
        XmlFormatter f = new XmlFormatter(inXml);
        f.xmlHeader = false;
        f.useHtmlEscapes = false;
        f.diagOn = true;
        
        f.uriPrefix = "http://localhost:8084/hl7services/get?id=";
        
        System.out.println(f.run());
        
        
    }
    
}
