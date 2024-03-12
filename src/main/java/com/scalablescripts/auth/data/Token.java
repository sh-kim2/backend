package com.scalablescripts.auth.data;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

public record Token(String refreshToken, LocalDateTime issueAt, LocalDateTime expiredAt) {
}
