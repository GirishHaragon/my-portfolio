package com.inn.cafe.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
//We need to use the annotation to make spring boot to understand that this class is acting as a service class logic.
public class JwtUtil {

    //We have to define a secret key, on the basis of the secret key it our JSON Web Token gets generated.
    //So we r going to use a variable.
    private String secret = "btechdays";

    //So first we need to extract the Expiration and username from the token,,
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);//We are going to set the username, and the key will be the subject that's why getSubject.
    }

    //Here we r extracting the expiration Date,,
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    //These methods we r using are going to use in order to extract the data from the JSON to create JSON, we need these methods.
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);//We are creating a final variable of Claims from io.jsonwebtoken  that calls the below method
        return claimsResolver.apply(claims);//So we are returning the claims in which the token is being checked..
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();//We r setting the secret key into JSON token.
    }                                              //From here if anybody tampers with the token then we have to check the token & exception should be thrown..

    //We are Checking the expiry status of token,,
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //To validate a token again based on Expiry, and checking 2 conditions, username and isTokenExpired,
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);//We are extracting username from token..
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));//If these 2 conditions satisfy then only we should allow users to access.
    }

    //We still have 2 more stuffs to do,, to generate the token by method createToken() & we need to put the roles of users.
    private String createToken(Map<String, Object> claims, String subject) {//To pass values inside this method we need another method generateToken()
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)//subject(variable) is the username/email in our case.
                .setIssuedAt(new Date(System.currentTimeMillis()))//We r setting the token as issued current system date&time for checking.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))//This is the calculation for 10hrs for expiring the token.
                .signWith(SignatureAlgorithm.HS256, secret).compact();//Here we r setting the signature for the token. HS256 is a encryption algo. And when we use compact the token gets generated.
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); //Here we created a Map collection of objects containing "role" as key and role as object.
        return createToken(claims, username);
    }

}
