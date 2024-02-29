package com.example.banksystem.user.dto;

import com.example.banksystem.account.dto.AccountResponseDto;
import com.example.banksystem.account.entity.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {

    private Integer id;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String password;
    private AccountResponseDto account;

}
