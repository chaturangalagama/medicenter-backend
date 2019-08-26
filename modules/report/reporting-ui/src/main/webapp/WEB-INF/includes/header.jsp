<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:bundle basename="messages_en">
    <nav class="navbar navbar-default navbar-cls-top " role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="home"><fmt:message key="reporting.module.header"/></a>
        </div>
        <div class="top-bar-right-text">
            <sec:authorize access="isAuthenticated()">
                <fmt:message key="reporting.welcome.user.label"/> <sec:authentication property="principal.username"/>
            </sec:authorize> &nbsp;
            <a href="j_spring_cas_security_logout" class="btn btn-sm btn-success square-btn-adjust"><fmt:message
                    key="reporting.sign.out.button.label"/></a>
        </div>
    </nav>
    <!-- /. NAV TOP -->
    <img class="top-banner" src="<c:url value="/resources/img/download.jpg"/>">

    <div class="back-footer-green"></div>
</fmt:bundle>