<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MySql DBAccessor</title>
</head>
<body>
	<form action="../dbServlet" method="post">
		Query:<input type="text" name="q"> <br>
		Parm1:<input type="text" name="parm1"> <br> 
		Parm2:<input type="password" name="parm2"> <br> 
		<input type="submit"  value="POST">
	</form>
	
	<form action="../dbServlet" method="get">
		Query:<input type="text" name="q"> <br>
		Parm1:<input type="text" name="parm1"> <br>
		Parm2:<input type="password" name="parm2"> <br> 
		 <input	type="submit"  value="GET">
	</form>
	
	<br>
	<br>
	<p id="demo">Click me.</p>


	<script>
		document.getElementById("demo").onclick = function(){sendQuery()};
		
		function sendQuery()
		{
			document.getElementById("demo").innerHTML = "HEY!!!!<br>You pressed a button!!!!!!!";
		}
	</script>


</body>
</html>