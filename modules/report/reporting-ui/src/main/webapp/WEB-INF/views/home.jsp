<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
    <title><fmt:message key="reporting.module.welcome.header"/></title>
</head>
<body>

<jsp:include page="../includes/header.jsp"/>
<jsp:include page="../includes/menubar.jsp"/>

<div id="page-wrapper">
    <div id="page-inner">
        <div class="row">
            <img style="width: 100%;" src="<c:url value="/resources/img/main-banner.jpg"/>">
        </div>
        <!-- /. ROW  -->
    </div>
    <!-- /. PAGE INNER  -->
</div>
<!-- /. PAGE WRAPPER  -->
</body>
</html>