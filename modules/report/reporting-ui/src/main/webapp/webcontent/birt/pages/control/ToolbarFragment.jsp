<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.resource.BirtResources,
                 org.eclipse.birt.report.utility.ParameterAccessor" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request"/>
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request"/>

<%-----------------------------------------------------------------------------
	Toolbar fragment
-----------------------------------------------------------------------------%>
<link href="birt/styles/index.css" rel="stylesheet" type='text/css'>
<link href="birt/styles/style.css" rel="stylesheet" type='text/css'>


<div style="margin-bottom: 10px;padding-left: 40px;font-size: 22px; margin-top: 1px">
    <a href="<%= request.getContextPath()%>" style="text-decoration:none">
        <img width="40px" style="margin-bottom: -10px" src="birt/images/main_logo.png"/>
        Reporting Module
    </a>
</div>

<TR
        <%
            if (attributeBean.isShowToolbar()) {
        %>
        HEIGHT="20px"
        <%
        } else {
        %>
        style="display:none"
        <%
            }
        %>
        >
    <TD COLSPAN='2'>
        <DIV ID="toolbar">
            <TABLE CELLSPACING="1px" CELLPADDING="1px" WIDTH="100%" CLASS="birtviewer_toolbar">
                <TR>
                    <TD WIDTH="6px"/>
                    <TD WIDTH="25px">
                        <INPUT TYPE="image" NAME='export' SRC="birt/images/csv-icon.png"
                               TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export" )%>"
                               ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export" )%>"
                               CLASS="birtviewer_clickable">
                    </TD>
                    <TD WIDTH="6px" hidden/>
                    <TD WIDTH="15px" hidden>
                        <INPUT TYPE="image" NAME='exportReport' SRC="birt/images/export_data.png"
                               TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>"
                               ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>"
                               CLASS="birtviewer_clickable">
                    </TD>
                    <TD WIDTH="6px"/>
                    <TD ALIGN='right'>
                    </TD>
                </TR>
            </TABLE>
        </DIV>
    </TD>
</TR>
