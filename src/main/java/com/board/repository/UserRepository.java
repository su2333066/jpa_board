package com.board.repository;

import com.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /* 유효성 검사 - 중복 체크
     * 중복 : true
     * 중복이 아닌 경우 : false
     */
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    // username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Optional<User> findByUsername(String username);

}
