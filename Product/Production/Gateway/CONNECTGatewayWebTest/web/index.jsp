<%@ page import="gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>CONNECT Request Gateway Test</title>
    </head>
    <body>
        <p><b><font size="5">CONNECTGatewayWeb Fanout Test Page</font></b></p>
        <p>Executor Pool Size=<%=ExecutorServiceHelper.getExecutorPoolSize() %></p>
        <p>Large Job Executor Pool Size=<%=ExecutorServiceHelper.getLargeJobExecutorPoolSize() %></p>

        <table border="0">
        <tr>
        <td>
            <b>Test PD</b>
        </td>
        </tr>
        </table>

        <form action="testpdfanout" method="GET">
            <input type="hidden" name="runpd" value="true">
            <table border="1">
            <tr>
            <td>
            <p><input size="30" type="text" name="patientid"> Patient Id (Local)</p>
            </td>
            </tr>
            <tr>
            <td>
            <p><input size="30" type="text" name="fname"> First Name</p>
            </td>
            </tr>
            <tr>
            <td>
            <p><input size="30" type="text" name="lname"> Last Name</p>
            </td>
            </tr>
            <tr>
            <td>
            <p><input size="30" type="text" name="dob"> DOB (mm-dd-yyyy)</p>
            </td>
            </tr>
            <tr>
            <td>
            <p><input size="10" type="text" name="gender"> Gender</p>
            </td>
            </tr>
            <tr>
            <td>
            <p><input type="submit" name="run pd" value="run pd"></p>
            </td>
            </tr>
            </table>
        </form>



        <table border="0">
        <tr>
        <td>
            <b>Test DQ</b>
        </td>
        </tr>
        </table>
        <form action="testdqfanout" method="GET">
            <input type="hidden" name="rundq" value="true">
            <table border="1">
            <tr>
            <td>
            <p><input size="30" type="text" name="patientid"> Local Patient Id</p>
            </td>
            </tr>
            <tr>
            <td>
            <p><input type="submit" name="run dq" value="run dq"></p>
            </td>
            </tr>
            </table>
        </form>




        <table border="0">
        <tr>
        <td>
            <b>DQ Simulator</b>
        </td>
        </tr>
        </table>
        <form action="testdqfanout" method="GET">
            <input type="hidden" name="rundqtest" value="true">
            <table border="1">
            <tr>
            <td>
            <p><input size="3" type="text" name="requestcount"> Number of DQ Requests</p>
            </td>
            <td>
            <p><input size="30" type="text" name="targethcid" value="1.1"> Simulator Home Id</p>
            <p>Note that homeid is used to get simulator service url from internalConnectionInfo.xml</p>
            </td>
            <td>
            <p><input type="submit" name="simulate dq requests" value="simulate dq requests"></p>
            </td>
            </tr>
            </table>
        </form>

        <table border="0">
        <tr>
        <td>
            <b>PD Simulator</b>
        </td>
        </tr>
        </table>
        <form action="testpdfanout" method="GET">
            <input type="hidden" name="runpdtest" value="true">
            <table border="1">
            <tr>
            <td>
            <p><input size="3" type="text" name="requestcount"> Number of PD Requests</p>
            </td>
            <td>
            <p><input size="30" type="text" name="targethcid" value="1.1"> Simulator Home Id</p>
            <p>Note that homeid is used to get simulator service url from internalConnectionInfo.xml</p>
            </td>
            <td>
            <p><input type="submit" name="simulate pd requests" value="simulate pd requests"></p>
            </td>
            </tr>
            </table>
        </form>
    </body>
</html>

