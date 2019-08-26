<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar-default navbar-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav" id="main-menu">
            <sec:authorize access="hasAnyRole('ROLE_REPORT_TRANSACTION')">
                <li id="transaction-report">
                    <a href="transaction-report"><i class="fa fa-list"></i> <fmt:message
                            key="reporting.detailed.transactional.report"/></a>
                </li>
            </sec:authorize>
            <li id="transaction-report">
                <a href="transaction-report"><i class="fa fa-list"></i> <fmt:message
                        key="reporting.detailed.transactional.report"/></a>
            </li>

        </ul>
    </div>
</nav>