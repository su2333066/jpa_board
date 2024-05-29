package com.board.service;

import com.board.domain.User;
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
    public boolean join(UserDto.Request dto) {

        String username = dto.getUsername();
        String password = dto.getPassword();

        boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            return false;
        }

        dto.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(dto.toEntity());
        return true;
    }

    /* 회원가입 시, 유효성 검사 및 중복 체크 */
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사, 중복 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }


}
