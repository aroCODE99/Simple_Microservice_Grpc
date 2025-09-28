package com.AuthService.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class TodoResponse {

    private UUID id;

    private String name;

    private String desc;

    private String createdAt;

    private String updatedAt;
}
