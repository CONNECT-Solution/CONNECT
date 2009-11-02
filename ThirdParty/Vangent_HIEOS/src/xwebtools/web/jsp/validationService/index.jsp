<!--
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
-->
<html>
    <head>
        <title>XDS Tools</title>
    </head>
    <body>
        <center>
            <h1>XDS Tools</h1>
        </center>
        <p>These tools give access to parts of the Public Registry internals for validating your
            inputs via web interface.</p>
        <h2>Metadata</h2>
        <p>This service runs your XDS metadata through these forms of validation: <ul>
                <li>ebXML Registry Schema</li>
                <li>XDS Metadata Validator</li>
            </ul>
        </p>
        <form method="post" action="/xwebtools/servlet" enctype="multipart/form-data">
            <input type="hidden" name="action" value="metadata" />
            <table>
                <tr>
                    <td>
                        <b>Metadata Type</b>
                    </td>
                    <td>
                        <input type="radio" name="metadataType" id="XDS.a" value="XDS.a" />
                        <label for="XDS.a">Register Transaction</label>
                        <input type="radio" name="metadataType" id="XDS.b" value="XDS.b" />
                        <label for="XDS.b">Register Transaction-b</label>
                        <input type="radio" name="metadataType" id="XDM" value="XDM" />
                        <label for="XDM">XDM</label>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b>Metadata File</b>
                    </td>
                    <td>
                        <input type="file" name="metadata" size="70" />
                    </td>
                </tr>
            </table>
            <input type="submit" name="submit" value="Submit..." />
        </form>
        <hr />
        <h2>Document</h2>
        <p>Submit a document here and get back the size and hash value as well as a copy of the document base64 encoded. This is useful when testing
            your Document Repository (.a or .b). The hash and base64 encoding algorithms used only operate correctly on US ASCII.  This service will upgraded to
            operate on full UTF-8 in the Fall of 2008.</p>
        <form method="post" action="/xwebtools/servlet" enctype="multipart/form-data">
            <input type="hidden" name="action" value="document" />
            <p>Choose a file or enter a URI which references the document.</p>
            <table>
                <tr>
                    <td>File</td>
                    <td>
                        <input type="file" name="document" size="70" />
                    </td>
                </tr>
                <tr>
                    <td>URI</td>
                    <td>
                        <input type="text" name="document_uri" size="70" />
                    </td>
                </tr>
            </table>
            <input type="submit" name="submit" value="Submit..." />
        </form>
    </body>
</html>
