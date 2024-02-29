package com.example.banksystem.user;

import com.example.banksystem.user.dto.UserPatchDto;
import com.example.banksystem.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("search-user")
    public ResponseEntity<List<UserResponseDto>> searchClients(
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email
    ) {
        List<UserResponseDto> userResponseDto
                = userService.searchClients(dateOfBirth, phoneNumber, fullName, email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUser(Pageable pageable, @RequestParam(required = false) String predicate) {
        Page<UserResponseDto> all = userService.getAll(pageable, predicate);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Integer id) {
        UserResponseDto responseDto = userService.getById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> patchUser(@PathVariable Integer id, @RequestBody UserPatchDto patchDto) throws NoSuchFieldException, IllegalAccessException {
        UserResponseDto responseDto = userService.patch(id, patchDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<String> transferMoney(
            @RequestParam() String sender,
            @RequestParam() String recipient,
            @RequestParam Double amount
    ) {

        String success = userService.transferMoney(sender, recipient, amount);
        return ResponseEntity.ok(success);
    }

}
