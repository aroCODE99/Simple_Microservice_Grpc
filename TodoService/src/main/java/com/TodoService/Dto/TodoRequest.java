package com.TodoService.Dto;

import com.TodoService.Dto.Validator.TodoUpdateValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TodoRequest {
    @NotBlank(groups = TodoUpdateValidation.class, message = "id is required")
    private String id;

    @NotBlank(message = "Todo name must not be blank")
    @Size(min = 3, max = 100, message = "Todo name must be between 3 and 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String desc;

    @Size(max = 5, message = "Should not exceed > 5")
    private boolean done;

    @NotBlank(message = "user_id should be present in here")
    private String userId;
}
