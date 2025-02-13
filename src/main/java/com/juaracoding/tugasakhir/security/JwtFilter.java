package com.juaracoding.tugasakhir.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 09/02/2025 13:07
@Last Modified 09/02/2025 13:07
Version 1.0
*/

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.service.impl.AppUserDetailService;
import com.juaracoding.tugasakhir.util.LoggingFile;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AppUserDetailService appUserDetailService;

    @Autowired
    private JwtUtility jwtUtility ;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        authorization = authorization == null ? "":authorization;
        String token = null;
        String userName = null;

//        TOKEN
        try{
//            String strContentType = request.getContentType()==null?"":request.getContentType();
            if(!"".equals(authorization) && authorization.startsWith("Bearer ") && authorization.length()>7)
            {
                token = authorization.substring(7);//memotong setelah kata Bearer+spasi = 7 digit
                token = Crypto.performDecrypt(token);
                userName = jwtUtility.getUsernameFromToken(token);
                if(userName != null &&
                        SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    if(jwtUtility.validateToken(token))
                    {
                        final UserDetails userDetails = appUserDetailService.loadUserByUsername(userName);
                        final UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null,
                                        userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            LoggingFile.logException("JwtFilter","doFilterInternal", ex, OtherConfig.getEnableLogFile());
        }
        filterChain.doFilter(request,response);

    }
}