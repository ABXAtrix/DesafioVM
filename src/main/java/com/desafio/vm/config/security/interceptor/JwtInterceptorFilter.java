package com.desafio.vm.config.security.interceptor;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.desafio.vm.config.security.UserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de interceptação de requisições. 
 * Verifica a presença e validade do Token JWT no cabeçalho Authorization.
 */
@Component
public class JwtInterceptorFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtInterceptorFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Valida o token e recupera o UserDetail usando o JwtService
                UserDetail userDetail = jwtService.validateTokenAndGetUser(jwt);

                if (userDetail != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetail,
                            null,
                            userDetail.getAuthorities()
                    );
                    
                    // Adiciona detalhes da requisição
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Define o usuário como autenticado no Spring Security
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Continua o fluxo da requisição para os próximos filtros ou para o Controller
        filterChain.doFilter(request, response);
    }
}