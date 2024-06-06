package com.board.service;

import com.board.dto.AuthenticationRequest;
import com.board.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public ResponseEntity<String> login(AuthenticationRequest request) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            if (!bCryptPasswordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtil.createJwt(userDetails.getUsername(), userDetails.getAuthorities().toArray()[0].toString(), 1000 * 60 * 5L);

            return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
