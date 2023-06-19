<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Update Lead Registry</title>
</head>
<body>
	<h2>Update lead</h2>
	<form action ="updateLead" method="post" style="font-size: 18px;">
	<%//When we click on the link it will fetch the data, will display in the form then update the data in the form, submit it and that will go to the DB. %>
	<%//One thing to keep in mind that in update feature we won't be saving the record, if we do so the new id tries to auto-generate, to keep the same id no and update the details then we need to update the record based on Id no, to keep the id no same we use include input tag. %>
	<input type="hidden" name="id" value="${lead.id}"/><%//But if we don't want to display the id no then change type= text to hidden, this makes the field hidden in UI, but takes action in the backend.  %>
	<pre>
		First Name  : <input type="text" name="firstName" value="${lead.firstName}"/><%//Whatever names we have given in Entity class(Lead.java), exactly that should be in the name attribute %>
		Last Name   : <input type="text" name="lastName" value="${lead.lastName}"/>
		Email id    : <input type="text" name="email" value="${lead.email}"/>
		Mobile      : <input type="text" name="mobile" value="${lead.mobile}"/>
		           	 		
		           	 	  <input type="submit" value="update"/>
	</pre>
	</form>
	${msg}<%//This Acts as get attribute, before running it shows error and when we run it, it gets corrected itself %>
</body>
</html>