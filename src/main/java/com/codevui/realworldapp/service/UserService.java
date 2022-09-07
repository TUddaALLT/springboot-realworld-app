package com.codevui.realworldapp.service;

import java.util.Map;

import com.codevui.realworldapp.exception.custom.CustomBadRequestException;
import com.codevui.realworldapp.exception.custom.CustomNotFoundException;
import com.codevui.realworldapp.model.profile.dto.ProfileDTORespone;
import com.codevui.realworldapp.model.user.dto.UserDTOCreate;
import com.codevui.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.codevui.realworldapp.model.user.dto.UserDTORespone;

public interface UserService {

    public Map<String, UserDTORespone> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException;

    public Map<String, UserDTORespone> registerUser(Map<String, UserDTOCreate> userDTOCreate);

    public Map<String, UserDTORespone> getCurrentUser() throws CustomNotFoundException;

    public Map<String, ProfileDTORespone> getProfile(String username) throws CustomNotFoundException;

    public Map<String, ProfileDTORespone> getFollowUser(String username) throws CustomNotFoundException;

    public Map<String, ProfileDTORespone> unFollowUser(String username) throws CustomNotFoundException;

}
