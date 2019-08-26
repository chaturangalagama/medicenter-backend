<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.2//EN"
"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd">
<fmt:bundle basename="messages">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <title><decorator:title default='Welcome to Reporting Module'/></title>
        <link rel="icon" type="image/ico" href="<c:url value="/resources/img/favicon.ico"/>"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/jquery.datetimepicker.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/css/custom.css"/>" rel="stylesheet">
        <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'/>
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        <decorator:head/>
    </head>
    <body>
    <div id="wrapper">
        <decorator:body/>

        <%@ include file="/WEB-INF/includes/footer.jsp" %>
    </div>
    <script src="<c:url value="/resources/js/jquery.js"/>" type="text/javascript"></script>

    <script type="text/javascript">
        $(document).ready(function(){
            $("#select_all").change(function(){
                $(".checkbox_class").prop("checked", $(this).prop("checked"));
            });
        });
    </script>

    <script type="text/javascript">
        $(function () {

            $('#serviceCode').on('change keyup paste', function () {

                $.ajax({
                    type: "GET",
                    url: "/reporting/get-classification",
                    contentType: "application/json; charset=utf-8",
                    data: "serviceCode=" + $(this).val(),
                    dataType: "json",
                    success: function (data) {
                        console.log("Successfully reached." + data);
                        var $el = $("#classification");
                        $el.empty(); // remove old options
                        $.each(data, function(value, key) {
                            $el.append($("<option></option>").attr("value", key).text(key));
                        });
                    },
                    error: function (req, status, error) {
                        console.log("ERROR:" + error.toString() + " " + status + " " + req.responseText);
                    }
                });

                $.ajax({
                    type: "GET",
                    url: "/reporting/get-description",
                    contentType: "application/json; charset=utf-8",
                    data: "serviceCode=" + $(this).val(),
                    dataType: "json",
                    success: function (data) {
                        console.log("Menu Description : " + data);
                        var $el = $("#description");
                        $el.empty(); // remove old options
                        $.each(data, function(value, key) {
                            $el.append($("<option></option>").attr("value", key).text(key));
                        });
                    },
                    error: function (req, status, error) {
                        console.log("ERROR:" + error.toString() + " " + status + " " + req.responseText);
                    }
                });


            });

        });
    </script>

    <script src="<c:url value="/resources/js/bootstrap.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery.datetimepicker.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/jquery.metisMenu.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/js/custom.js"/>" type="text/javascript"></script>

    <script type="text/javascript">
        $('#startDate').datetimepicker({
            yearStart: 2014,
            yearEnd: 2018,
            closeOnDateSelect: true,
            timepicker: false,
            format: 'Y-m-d',
            maxDate: '+1969/12/31'
        });
        $('#endDate').datetimepicker({
            yearStart: 2014,
            yearEnd: 2018,
            closeOnDateSelect: true,
            timepicker: false,
            format: 'Y-m-d',
            maxDate: '+1970/01/01'
        });
        $('#startDateTime').datetimepicker({
            yearStart: 2014,
            yearEnd: 2018,
            closeOnTimeSelect: true,
            timepicker: true,
            format: 'Y-m-d H:i',
            maxDate: '+1970/01/01'
        });
        $('#endDateTime').datetimepicker({
            yearStart: 2014,
            yearEnd: 2018,
            closeOnTimeSelect: true,
            timepicker: true,
            format: 'Y-m-d H:i',
            maxDate: '+1970/01/01'
        });
        $('#startDateTimeT').datetimepicker({
            yearStart: 2014,
            yearEnd: 2018,
            closeOnTimeSelect: true,
            timepicker: true,
            format: 'Y-m-d H:i',
            maxDate: '+1970/01/01'
        });
        $('#endDateTimeT').datetimepicker({
            yearStart: 2014,
            yearEnd: 2018,
            closeOnTimeSelect: true,
            timepicker: true,
            format: 'Y-m-d H:i',
            maxDate: '+1970/01/01'
        });
        $('#startMonth').datetimepicker({
            yearStart: 2012,
            yearEnd: 2018,
            closeOnDateSelect: true,
            timepicker: false,
            format: 'Y/m',
            maxDate: '+1969/12/31'
        });
        $('#endMonth').datetimepicker({
            yearStart: 2012,
            yearEnd: 2018,
            closeOnDateSelect: true,
            timepicker: false,
            format: 'Y/m',
            maxDate: '+1969/12/31'
        });
    </script>
    </body>
    </html>

</fmt:bundle>
