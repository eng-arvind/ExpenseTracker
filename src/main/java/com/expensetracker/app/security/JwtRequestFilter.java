package com.expensetracker.app.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.expensetracker.app.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 String requestTokenHeader =  request.getHeader("Authorization");

		if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ") ) {
			filterChain.doFilter(request, response); 	
			return;
		}
		
		String jwtToken = null;
		String username = null;
		
		if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken =  requestTokenHeader.substring(7);
			
			try {
				
				username =  jwtTokenUtil.getUsernameFromToken(jwtToken);
				if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					if(userDetails != null &&  jwtTokenUtil.isTokenvalidate(jwtToken)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword() ,userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
				
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Unable to get JWT token");
			}
			catch(ExpiredJwtException e) {
				throw new RuntimeException("Jwt token has expired");
			}
		}
		
		
		filterChain.doFilter(request, response); 
	}

}
