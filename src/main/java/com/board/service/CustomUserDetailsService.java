package com.board.service;

import com.board.domain.User;
import com.board.dto.CustomUserDetails;
import com.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회
        Optional<User> optional = userRepository.findByUsername(username);

        if (!optional.isPresent()) {
            // 존재하지 않으면
            throw new UsernameNotFoundException(username + " 사용자 없음");
        }else{
            // 존재하면 SecurityUser로 변환하여 전달
            User user = optional.get();
            return new CustomUserDetails(user);
        }
    }
}
