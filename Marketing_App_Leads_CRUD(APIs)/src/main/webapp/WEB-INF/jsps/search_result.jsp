<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>All Registrations...</h2>
	<table border='1'>
		<tr>
			<th>FirstName</th>
			<th>LastName</th>
			<th>Email</th>
			<th>Mobile</th>
			<th>Action</th>
		</tr>
		<c:forEach var="lead" items="${leads}">
		<tr>
			<td>${lead.firstName}</td><%//Don't change the casing of letters as lead.firstName it should be The table name what we entered in the Table name in Java code (Lead.java) %>
			<td>${lead.lastName}</td>
			<td>${lead.email}</td>
			<td>${lead.mobile}</td>
			<td><%//In the same td we are writing both delete & Update %>
				<a href="delete?id=${lead.id}">Delete</a>
				<a href="update?id=${lead.id}">Update</a>  <%//Based on the id first get the id, then update. %>
			</td><%//Earlier we had written down, expression tag request.getAttributeEmail, Instead we just write the ${} expression and Id gets stored by lead.id in '?id' & t goes to te controller layer %>
			<%//Above a href makes the delete word in to a actional linked word(Blue Underlined word) when we write the word in <a href_ _>delete</a>, here a href tag refers to action tag.. %>
		</tr>
		</c:forEach>
	</table>
	
</body>
</html>