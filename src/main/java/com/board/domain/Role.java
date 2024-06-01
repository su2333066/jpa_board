package com.board.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public enum Role {
    ROLE_USER, ROLE_ADMIN
}
