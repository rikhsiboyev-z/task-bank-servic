package com.example.banksystem.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountResponseDto {

    private Integer id;
    private Double balance;
    private String cardNumber;
}
