package com.board.service;

import com.board.domain.User;
import com.board.jwt.JWTUtil;
import com.board.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public ResponseEntity<String> login(Map<String, String> user, HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.get("username"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.get("password"), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User findUser = userRepository.findByUsername(user.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!bCryptPasswordEncoder.matches(user.get("password"), findUser.getPassword())){
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        String jwtToken = jwtUtil.createJwt(findUser.getUsername(), findUser.getRole(), 3600L);

        return ResponseEntity.ok(jwtToken);
    }

}
