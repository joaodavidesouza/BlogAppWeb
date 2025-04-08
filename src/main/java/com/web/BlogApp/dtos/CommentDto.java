package com.web.BlogApp.dtos;

import jakarta.validation.constraints.NotBlank;

public record CommentDto(@NotBlank String author, @NotBlank String content) {
}