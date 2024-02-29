package com.example.banksystem.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPatchDto {

    private String phoneNumber;
    @Email
    private String email;
}
