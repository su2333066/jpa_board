package com.board.dto;

import com.board.domain.Role;
import com.board.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;


public class UserDto {

    /** 회원 Service 요청(Request) DTO 클래스 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long id;

        @Pattern(regexp = "^[a-z0-9-_]{8,20}$", message = "아이디는 영문 대문자, 한글, 특수문자를 제외한 8~20자를 입력하세요.")
        private String username;

        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        private Role role;

        /* DTO -> Entity */
        public User toEntity() {
            User user = User.builder()
                    .id(id)
                    .username(username)
                    .password(password)
                    .role(Role.ROLE_USER)
                    .build();
            return user;
        }
    }

    /**
     * 인증된 사용자 정보를 세션에 저장하기 위한 클래스
     * 세션을 저장하기 위해 User 엔티티 클래스를 직접 사용하게 되면 직렬화를 해야 하는데,
     * 엔티티 클래스에 직렬화를 넣어주면 추후에 다른 엔티티와 연관관계를 맺을시
     * 직렬화 대상에 다른 엔티티까지 포함될 수 있어 성능 이슈 우려가 있기 때문에
     * 세션 저장용 Dto 클래스 생성
     * */
    @Getter
    public static class Response implements Serializable {

        private final Long id;
        private final String username;
        private final Role role;
        private final String modifiedDate;

        /* Entity -> dto */
        public Response(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.role = user.getRole();
            this.modifiedDate = user.getModifiedDate();
        }
    }
}