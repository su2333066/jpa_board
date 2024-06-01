package com.board.service;

import com.board.dto.UserDto;
import com.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(UserDto.Request dto) {
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userRepository.save(dto.toEntity());
    }

    public void checkUsernameDuplication(String username) {
        if(userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }
    }

    public void checkNicknameDuplication(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }
    }


    /* 회원가입 시, 유효성 검사 */
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }


}
