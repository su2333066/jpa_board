package com.board.controller;

import com.board.dto.UserDto;
import com.board.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    @Data
    public class validationResponse{
        private boolean ok;
        private Map<String, String> validatorResult;
    }

    @PostMapping("/join/check")
    public validationResponse checkValidate(@RequestBody @Valid UserDto.Request dto, Errors errors) {

        validationResponse joinResponse = new validationResponse();
        joinResponse.setOk(true);

        if (errors.hasErrors()) {
            joinResponse.setOk(false);

            Map<String, String> validatorResult = userService.validateHandling(errors);
            joinResponse.setValidatorResult(validatorResult);
        }

        return joinResponse;
    }

    @PostMapping("/join")
    public boolean join(@RequestBody @Valid UserDto.Request dto, Errors errors) {

        if (errors.hasErrors()) {
            return false;
        }

        userService.join(dto);
        return true;
    }

    @GetMapping("/join/username/{username}")
    public ResponseEntity<String> checkUsernameDuplicate(@PathVariable("username") String username){
        try {
            userService.checkUsernameDuplication(username);
            return new ResponseEntity<String>("사용가능한 아이디입니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/join/nickname/{nickname}")
    public ResponseEntity<String> checkNicknameDuplicate(@PathVariable("nickname") String nickname){
        try {
            userService.checkNicknameDuplication(nickname);
            return new ResponseEntity<String>("사용가능한 닉네임입니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}