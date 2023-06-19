<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create Lead Registry</title>
</head>
<body>
	<h2>Create lead</h2>
	<form action ="saveLead" method="post" style="font-size: 18px;">
	<pre>
		First Name  : <input type="text" name="firstName"/><%//Whatever names we have given in Entity class(Lead.java), exactly that should be in the name attribute %>
		Last Name   : <input type="text" name="lastName"/>
		Email id    : <input type="text" name="email"/>
		Mobile      : <input type="text" name="mobile"/>
		           	 		
		           	 	  <input type="submit" value="save"/>
	</pre>
	</form>
	${msg}<%//This Acts as get attribute, before running it shows error and when we run it, it gets corrected itself %>
</body>
</html>