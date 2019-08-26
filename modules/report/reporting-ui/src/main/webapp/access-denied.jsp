<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.2//EN"
"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd">


<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>403</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/custom.css"/>" rel="stylesheet">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'/>
</head>
<body>


<div id="wrapper" style="background-color: #F3F3F3;text-align: center">
    <%@ include file="/WEB-INF/includes/header.jsp" %>
    <br>
    <br>
    <label style="font-size: 50px; text-align: center;font-weight: 700;">403</label>
    <br>
    <br>
    <label style="font-size: 20px; text-align: center;font-weight: 700;">Access is Denied!</label>
    <br>
    <br>
    <br>
    <a href="j_spring_cas_security_logout">Go Home</a>
    <br>
    <br>
    <br>
    <br>

</div>

</body>
</html>
