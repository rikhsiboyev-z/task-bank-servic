package com.example.banksystem.account.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountCreateDto {
    @Min(0)
    private Double balance;
    private String cardNumber;
}
