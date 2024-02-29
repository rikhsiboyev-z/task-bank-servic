package com.example.banksystem.user;

import com.example.banksystem.common.service.GenericDtoMapper;
import com.example.banksystem.user.dto.UserCreateDto;
import com.example.banksystem.user.dto.UserResponseDto;
import com.example.banksystem.user.dto.UserUpdateDto;
import com.example.banksystem.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelMapper extends GenericDtoMapper<User, UserCreateDto, UserUpdateDto, UserResponseDto> {

    private final ModelMapper mapper;

    @Override
    public User toEntity(UserCreateDto userCreateDto) {
        return mapper.map(userCreateDto, User.class);
    }

    @Override
    public UserResponseDto toResponseDto(User user) {
        return mapper.map(user, UserResponseDto.class);
    }

    @Override
    public void update(UserUpdateDto userUpdateDto, User user) {
        mapper.map(userUpdateDto, user);
    }

    @Override
    public UserCreateDto toCreateDto(User user) {
        return mapper.map(user, UserCreateDto.class);
    }
}
