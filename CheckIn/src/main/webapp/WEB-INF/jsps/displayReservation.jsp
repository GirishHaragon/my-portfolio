<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Reservation Details</title>
</head>
<body>
	<h1>Reservation Details...</h1>
	<form style="font-size: 18px;">
	<pre>
	<b>Passenger Name</b>   : ${reservation.getPassenger().firstName}
	
	<b>Passenger Email</b>  : ${reservation.getPassenger().email}
	
	<b>Passenger Phone</b>  : ${reservation.getPassenger().phone}
	
	<b>Flight Number</b>    : ${reservation.getFlight().flightNumber}
	
	<b>Operating AirLine</b>: ${reservation.getFlight().operatingAirlines}
	
	<b>Departure City</b>   : ${reservation.getFlight().departureCity}
	
	<b>Arrival City</b>     : ${reservation.getFlight().arrivalCity}
	
	<b>Date of Departure</b>: ${reservation.getFlight().dateOfDeparture}
	
	<b>Estd. Departure</b>  : ${reservation.getFlight().estimatedDepartureTime}
	</pre>
	</form>
	<h1>Update No. of Bags & Status..</h1>
	<form action="proceedToCheckIn" method="post" style="font-size: 18px;">
	<pre>
		Reservation Id  : <input type="text" name="id" value="${reservation.id}" readonly/>
		
		Number of Bags  : <input type="text" name="numberOfBags"/>
		
		                             <input type="submit" value="confirm"/>
	</pre>
	</form>
</body>
</html>