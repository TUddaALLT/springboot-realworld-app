package com.codevui.realworldapp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codevui.realworldapp.exception.custom.CustomBadRequestException;
import com.codevui.realworldapp.exception.custom.CustomNotFoundException;
import com.codevui.realworldapp.model.user.dto.UserDTOCreate;
import com.codevui.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.codevui.realworldapp.model.user.dto.UserDTORespone;
import com.codevui.realworldapp.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userservice;

    @PostMapping("/users/login")
    public Map<String, UserDTORespone> login(@RequestBody Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException {
        return userservice.authenticate(userLoginRequestMap);
    }

    @PostMapping("/users")
    public Map<String, UserDTORespone> registerUser(@RequestBody Map<String, UserDTOCreate> userDTOCreate) {
        return userservice.registerUser(userDTOCreate);
    }

    @GetMapping("/users")
    public Map<String, UserDTORespone> getCurrentUser() throws CustomNotFoundException {
        return userservice.getCurrentUser();
    }
}
