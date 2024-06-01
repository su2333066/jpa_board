package com.board.service;

import com.board.domain.User;
import com.board.dto.CustomUserDetails;
import com.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        //DB에서 조회
        Optional<User> findUser = userRepository.findByUsername(username);

        if (!findUser.isPresent()) {
            // 존재하지 않으면
            throw new IllegalArgumentException("가입되지 않은 아이디입니다.");
        }else{
            // 존재하면 CustomUserDetails로 변환하여 전달
            User user = findUser.get();
            return new CustomUserDetails(user);
        }
    }
}
