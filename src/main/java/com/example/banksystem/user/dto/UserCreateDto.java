package com.example.banksystem.user.dto;

import com.example.banksystem.account.dto.AccountCreateDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDto {

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotNull
    private LocalDate birthDate;
    @Pattern(regexp = "^998[0-9]{9}$")
    private String phoneNumber;
    @Email
    private String email;
    private String password;

    private AccountCreateDto accountCreateDto;
}
