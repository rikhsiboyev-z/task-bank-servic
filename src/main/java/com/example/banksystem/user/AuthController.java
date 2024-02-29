package com.example.banksystem.user;

import com.example.banksystem.jwt.JwtService;
import com.example.banksystem.user.dto.UserCreateDto;
import com.example.banksystem.user.dto.UserLoginDto;
import com.example.banksystem.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @CrossOrigin(exposedHeaders = "Authorization")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserCreateDto userCreateDto) {

        UserResponseDto userResponseDto = userService.register(userCreateDto);

        String token = jwtService.generateToken(userResponseDto.getEmail(), Collections.emptyMap());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(userResponseDto);
    }

    @PostMapping("/login")
    @CrossOrigin(exposedHeaders = "Authorization")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {

        UserResponseDto userResponseDto = userService.login(userLoginDto);
        String token = jwtService.generateToken(userResponseDto.getEmail(), Collections.emptyMap());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(userResponseDto);
    }
}
