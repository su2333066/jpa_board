package com.board.controller;

import com.board.dto.UserDto;
import com.board.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    @PostMapping("/join/check")
    public JoinResponse checkValidate(@RequestBody @Valid UserDto.Request dto, Errors errors) {

        JoinResponse joinResponse = new JoinResponse();
        joinResponse.setMsg("success");

        if (errors.hasErrors()) {
            joinResponse.setMsg("fail");

            Map<String, String> validatorResult = userService.validateHandling(errors);
            joinResponse.setValidatorResult(validatorResult);
        }

        return joinResponse;
    }

    @PostMapping("/join")
    public JoinResponse join(@RequestBody @Valid UserDto.Request dto, Errors errors){
        JoinResponse joinResponse = new JoinResponse();
        joinResponse.setMsg("success");

        if (errors.hasErrors()) {
            joinResponse.setMsg("fail");
            return joinResponse;
        }

        userService.join(dto);
        return joinResponse;
    }

    @GetMapping("/join/username/{username}")
    public boolean checkUsernameDuplicate(@PathVariable("username") String username){
        return userService.checkUsernameDuplication(username);
    }

    @GetMapping("/join/nickname/{nickname}")
    public boolean checkNicknameDuplicate(@PathVariable("nickname") String nickname){
        return userService.checkNicknameDuplication(nickname);
    }

    @Data
    public class JoinResponse{
        private String msg;
        private Map<String, String> validatorResult;
    }


}