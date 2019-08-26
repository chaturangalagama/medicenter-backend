<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
    <title><fmt:message key="reporting.detailed.transactional.report"/></title>
</head>
<body>
<jsp:include page="../includes/header.jsp"/>
<jsp:include page="../includes/menubar.jsp"/>
<div id="page-wrapper">
    <div id="page-inner">
        <form:form commandName="transactionForm" method="post" action="test-report">
            <br/>
            <button onclick='clearForm()' type="button" class="btn btn-default"><fmt:message
                    key="reporting.reset.button.label"/></button>
            <button type="submit" class="btn btn-success"><fmt:message key="reporting.submit.button.label"/></button>

        </form:form>
    </div>
    <!-- /. PAGE INNER  -->
</div>
<!-- /. PAGE WRAPPER  -->
<script type="text/javascript">
    function clearForm() {
        document.location = "transaction-report.html";
    }

    document.getElementById("transaction-report").className = "active-menu";
</script>
</body>
</html>
