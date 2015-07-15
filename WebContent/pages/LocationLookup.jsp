<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<img id="map" src="../images/NorthAmericaMap/NorthAmericaGameMap.png">
<script src="../scripts/jquery-2.1.1.js"></script>
<script>

	var output = "";
	$("#canvasLocation").click(function(e)
	{
		// on click give coordinates
		//alert("you clicked the map!");
		
		var x = (e.pageX);
		var y = (e.pageY);
		output += "this.positions." + country + " = [" + x + "," + y + "]\n";
		alert(output);
		//alert((e.pageX - this.offsetLeft) + "," + (e.pageY - this.offsetTop));
		return null;
	});

	var output = "";

	$(document).ready(function()
	{
		$('#map').click(function(e)
		{
			var country = prompt("please enter a country");
			var offset = $(this).offset();
			var x= (e.clientX - offset.left)+document.documentElement.scrollLeft;
			var y=(e.clientY - offset.top)+document.body.scrollTop;
			
			
			output += "this.positions." + country + " = [" + x + "," + y + "];\n";
			alert(output);
		});
	});
</script>
</body>
</html>