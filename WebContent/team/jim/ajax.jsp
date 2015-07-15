<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body onload="giveQueryFocus()">

	<style>
	 
#tb1{
	border-style:solid;
	border-color:#000000;
	border-width: 1px; 
	background-color:#333333;
	color: #FFFFFF;
	width: 100%;
	
}	
	
#myDiv {
	font-size: large;
}
</style>


	<script>
		var x = 1;
		
		function giveQueryFocus()
		{
			document.getElementById("tb1").focus();
		}

		function query()
		{
			var q = document.getElementById("tb1").value;
			loadXMLDoc(q);
			//document.getElementById("myDiv").innerHTML = 
		}

		function loadXMLDoc(theQuery)
		{
			var xmlhttp;
			if (window.XMLHttpRequest)
			{// code for IE7+, Firefox, Chrome, Opera, Safari
				xmlhttp = new XMLHttpRequest();
			}
			else
			{// code for IE6, IE5
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlhttp.onreadystatechange = function()
			{
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
				{
					document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
				}
			}

			xmlhttp.open("POST", "../dbServlet", true);
			xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			xmlhttp.send("q=" + theQuery);
		}
		

		</script>
	</script>
</head>
<body>

	<h2>AJAX Instant results</h2>
	<br>

	<br>
	Enter SQL cmd:<input type="text" name="tb1" id="tb1" onkeyup="query();">

	<div id="myDiv"></div>










</body>


</html>