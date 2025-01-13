package com.example.productmaster.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RoleDto {
    @NotEmpty
    private Long roleId;
    @NotEmpty
    private String roleName;
    @NotEmpty
    private Long userId;
}
