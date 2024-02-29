package com.example.banksystem.user;

import com.example.banksystem.common.repository.GenericSpecificationRepository;
import com.example.banksystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends GenericSpecificationRepository<User, Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    List<User> findByBirthDateAfter(LocalDate date);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByFirstnameStartingWith(String firstname);

    Optional<User> findByAccount_CardNumber(String cardNumber);
}
