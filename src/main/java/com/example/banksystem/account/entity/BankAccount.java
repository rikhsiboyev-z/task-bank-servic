package com.example.banksystem.account.entity;

import com.example.banksystem.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @DecimalMin(value = "0.0", message = "Баланс не может быть отрицательным")
    private Double balance;
    @Column(unique = true, nullable = false)
    private String cardNumber;


    @OneToOne
    private User user;
}
