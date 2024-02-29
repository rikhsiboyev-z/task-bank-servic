package com.example.banksystem.user;

import com.example.banksystem.account.BankAccountRepository;
import com.example.banksystem.account.entity.BankAccount;
import com.example.banksystem.common.service.GenericCrudService;
import com.example.banksystem.user.dto.*;
import com.example.banksystem.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class UserService extends GenericCrudService<User, Integer, UserCreateDto, UserUpdateDto, UserPatchDto, UserResponseDto>
        implements UserDetailsService {

    private final UserRepository repository;
    private final UserModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final Class<User> entityClass = User.class;
    private final BankAccountRepository bankAccountRepository;
    private final ModelMapper modelMapper;

    @Override
    protected User save(UserCreateDto userCreateDto) {

        BankAccount bankAccount = modelMapper.map(userCreateDto.getAccountCreateDto(), BankAccount.class);
        bankAccountRepository.save(bankAccount);
        User user = mapper.toEntity(userCreateDto);
        user.setAccount(bankAccount);
        bankAccount.setUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    protected User updateEntity(UserUpdateDto userUpdateDto, User user) {
        mapper.update(userUpdateDto, user);
        return repository.save(user);
    }

    public UserResponseDto register(UserCreateDto userCreateDto) {
        return mapper.toResponseDto(save(userCreateDto));
    }

    public UserResponseDto login(UserLoginDto userLoginDto) {

        User user = repository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("user with %s email not found"
                        .formatted(userLoginDto.getEmail())));

        if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {

            return mapper.toResponseDto(user);
        }

        throw new RuntimeException("%s %s password failed".formatted(user.getFirstname(), userLoginDto.getPassword()));
    }


    public List<UserResponseDto> searchClients(LocalDate dateOfBirth, String phoneNumber, String fullName, String email) {

        if (dateOfBirth != null) {
            return repository.findByBirthDateAfter(dateOfBirth).stream().map(mapper::toResponseDto).toList();

        } else if (phoneNumber != null) {
            return repository.findByPhoneNumber(phoneNumber).stream().map(mapper::toResponseDto).toList();

        } else if (fullName != null) {
            return repository.findByFirstnameStartingWith(fullName).stream().map(mapper::toResponseDto).toList();

        } else if (email != null) {
            return repository.findByEmail(email).stream().map(mapper::toResponseDto).toList();

        }
        throw new RuntimeException("user not found ");
    }

    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        List<User> users = repository.findAll();
        for (User user : users) {
            BankAccount account = user.getAccount();
            Double initialBalance = account.getBalance();
            if (initialBalance == 0) {
                initialBalance = 1.0;
            }
            double currentBalance = initialBalance * 1.05;
            if (currentBalance > initialBalance * 2.07) {
                currentBalance = Math.min(currentBalance, initialBalance * 2.07);
            }
            account.setBalance(currentBalance);
            bankAccountRepository.save(account);
            repository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("user with %s email not found".formatted(email)));
    }


    public String transferMoney(String sender, String recipient, Double amount) {

        if (amount <= 0) {
            return "amount > 0";
        }
        User userSender = repository.findByAccount_CardNumber(sender)
                .orElseThrow(() -> new EntityNotFoundException("sender with %s card number not found".formatted(sender)));
        User userRecipient = repository.findByAccount_CardNumber(recipient)
                .orElseThrow(() -> new EntityNotFoundException("recipient with %s card number not found".formatted(recipient)));

        BankAccount senderBalance = userSender.getAccount();
        BankAccount recipientBalance = userRecipient.getAccount();

        senderBalance.setBalance(senderBalance.getBalance() - amount);
        recipientBalance.setBalance(recipientBalance.getBalance() + amount);

        repository.save(userRecipient);
        repository.save(userSender);

        return "success";
    }
}
