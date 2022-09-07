package com.codevui.realworldapp.model.user.mapper;

import com.codevui.realworldapp.entity.User;
import com.codevui.realworldapp.model.user.dto.UserDTOCreate;
import com.codevui.realworldapp.model.user.dto.UserDTORespone;

public class UserMapper {
    public static UserDTORespone toUserDTORespone(User user) {
        return UserDTORespone.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

    public static User toUser(UserDTOCreate userCreated) {
        return User.builder().username(userCreated.getUsername()).email(userCreated.getEmail())
                .password(userCreated.getPassword()).build();
    }
}
