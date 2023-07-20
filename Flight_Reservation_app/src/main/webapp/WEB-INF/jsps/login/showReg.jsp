<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>New Registration..</title>
</head>
<body>
	<h1>Create New Account</h1>
	<form action="saveReg" method="post" style="font-size: 18px;">
<pre>
	First Name <input type="text" name="firstName" />
	
	Middle Name<input type="text" name="middleName" />
	
	Last Name  <input type="text" name="lastName" />
	
	Email      <input type="text" name="email" />
	
	Password   <input type="text" name="password" />
	
				     <input type="submit" value="save" />
</pre>
	</form>
</body>
</html>