package com.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@Getter
public abstract class BaseTime {

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @Column(name = "modified_date", nullable = false)
    private String modifiedDate;

    // 해당 엔티티를 저장하기 이전에 실행
    @PrePersist
    public void onPrePersist(){
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = this.createdDate;
    }

    // 해당 엔티티를 업데이트 하기 이전에 실행
    @PreUpdate
    public void onPreUpdate(){
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
