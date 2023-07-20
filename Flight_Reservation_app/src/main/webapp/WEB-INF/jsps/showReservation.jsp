<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Reservation Details</title>
</head>
<body>
	<h2>Flight Details..</h2>
<form style="font-size: 18px;">
<pre>
	-> Flight Number	 :   ${flight.flightNumber}
	
	-> Operating Airline     :   ${flight.operatingAirlines}
	
	-> Departure City 	 :   ${flight.departureCity}
	
	-> Arrival City		 :   ${flight.arrivalCity}
	
	-> Departure Date	 :   ${flight.dateOfDeparture}
	
	-> Estimated Time	 :   ${flight.estimatedDepartureTime}
</pre>
</form >
	<h2>Enter Passenger Details</h2>
	<form action="confirmReservation" method="post" style="font-size: 18px;">
	<pre>
		First Name   : <input type="text" name="firstName"/>
		Middle Name  : <input type="text" name="middleName"/>
		Last Name    : <input type="text" name="lastName"/>
		Email        : <input type="text" name="email"/>
		Phone  	     : <input type="text" name="phone"/>
					   <input type="hidden" name="flightId" value="${flight.id}"/>
		
	<b># <i>Enter Payment Details...</i></b>
	
		Card Number  : <input type="text" name="cardNumber"/>
		Name on Card : <input type="text" name="nameOfCard"/>
		Cvv 	     : <input type="text" name="cvv"/>
		Expiry Date  : <input type="text" name="expiryDate"/>
		
			 	        <input type="submit" value="Book Ticket"/>
	</pre>
	</form>
</body>
</html>