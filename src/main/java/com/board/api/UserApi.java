package com.board.api;

import com.board.dto.UserDto;
import com.board.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    /* 회원가입 */
    @PostMapping("/join")
    public JoinResponse join(@RequestBody @Valid UserDto.Request dto, Errors errors) {

        JoinResponse joinResponse = new JoinResponse();

        if (errors.hasErrors()) {
            /* 회원가입 실패시 입력 데이터 값을 유지 */
            joinResponse.setUserDto(dto);

            /* 유효성 통과 못한 필드와 메시지를 핸들링 */
            Map<String, String> validatorResult = userService.validateHandling(errors);
            joinResponse.setValidatorResult(validatorResult);

            joinResponse.setMsg("validateError");
        }else{
            if (userService.join(dto)) {
                joinResponse.setMsg("success!!");
            } else {
                joinResponse.setMsg("duplicateError");
            }
        }

        return joinResponse;
    }

    @Data
    public class JoinResponse{
        private String msg;
        private UserDto.Request userDto;
        private Map<String, String> validatorResult;
    }


}