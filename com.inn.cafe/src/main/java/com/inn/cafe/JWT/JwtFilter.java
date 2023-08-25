package com.inn.cafe.JWT;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUserDetailsService service;

    Claims claims = null;//For now we are making the variables null.
    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //So first thing in this method is to by pass few steps that is user login/signup/forgot password apis.
        //So first thing we will check the thing
        if (httpServletRequest.getServletPath().matches("/user/login|/user/signup|/user/forgotPassword")) {
            //if this is the case then we have to simply use
            filterChain.doFilter(httpServletRequest, httpServletResponse);//it means that we have to just let it go into httpServletResponse and pass it to httpServletResponse.
        } else {//if that is not the case then we need to do the filtration, like validating the tokens by filtering.
            //so we are creating 2 variables of type Claims = null and String userName = null.
            String authorizationHeader = httpServletRequest.getHeader("Authorization");//The header name Authorization we r passing here.
            String token = null;
            //First thing in the authorizationHeader we have to check that if token contains bearer. Because if Jwt token is observed then we see bearer concatinated by default. So we will check if it contains Bearer in token or not.
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                //If this is the case then we have to extract the token
                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUsername(token);//In jwtUtil we have built the extractUsername method using token and now we r storing it into username here variable.
                claims = jwtUtil.extractAllClaims(token);//Again we are calling a method in jwtUtil and supplying token and storing the return value in claim variable.
            }

            //Now we have extracted the value.
            //And here we have to check for the valid value and also for session.
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //Then we have to extract the username from DB.
                UserDetails userDetails = service.loadUserByUsername(userName);
                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    //We have to check if the user is admin or just a user.
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser() {
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser() {
        return userName;//This is the username extracted from the user itself.
    }
}
