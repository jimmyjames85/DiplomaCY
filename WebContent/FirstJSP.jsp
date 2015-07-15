<%@page import="edu.iastate.cs309.r16.diplomacy.examples.ServletOne"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<br>
	<div id="idIntegerCount"></div><br>
	<div id="idUpVote">upVote</div><br>
	<div id="idDownVote">downVote</div><br>
	<div id="idTotalVotes">totalVotes</div><br>


	<script>
		var source = new EventSource("VoteCounter");
		
		source.addEventListener('up_event', function(event){
			document.getElementById("idUpVote").innerHTML = "Total # of up votes=" + event.data + "<br>";
		},false);

		source.addEventListener('down_event', function(event){
			document.getElementById("idDownVote").innerHTML = "Total # of down votes=" + event.data + "<br>";
		},false);

		source.addEventListener('total_votes', function(event){
			document.getElementById("idTotalVotes").innerHTML = "Total # of votes=" + event.data + "<br>Notice this refreshes about every 4 seconds<br>";
		},false);
	</script>
<br>
<font color="#00f0a0">This page has been visited <%=ServletOne.getTotalIntegers() %> times!!</font> 

</body>
</html>