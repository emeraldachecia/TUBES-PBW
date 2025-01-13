package com.runtracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.runtracker.models.User;

import java.io.IOException;

@Component
public class CustomSessionAuthenticationFilter extends OncePerRequestFilter {

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {
      String requestURI = request.getRequestURI();

      Object dataSession = request.getSession().getAttribute("dataSession");

      if (dataSession != null && SecurityContextHolder.getContext().getAuthentication() == null) {
         User user = (User) dataSession;

         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
               user, null, null);
         authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

         SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      filterChain.doFilter(request, response);
   }

}
