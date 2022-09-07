package com.codevui.realworldapp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codevui.realworldapp.entity.User;
import com.codevui.realworldapp.exception.custom.CustomBadRequestException;
import com.codevui.realworldapp.exception.custom.CustomNotFoundException;
import com.codevui.realworldapp.model.CustomError;
import com.codevui.realworldapp.model.profile.dto.ProfileDTORespone;
import com.codevui.realworldapp.model.user.dto.UserDTOCreate;
import com.codevui.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.codevui.realworldapp.model.user.dto.UserDTORespone;
import com.codevui.realworldapp.model.user.mapper.UserMapper;
import com.codevui.realworldapp.repository.UserRepository;
import com.codevui.realworldapp.service.UserService;
import com.codevui.realworldapp.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, UserDTORespone> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException {
        UserDTOLoginRequest userLoginRequest = userLoginRequestMap.get("user");
        Optional<User> userOptional = userRepository.findByEmail(userLoginRequest.getEmail());
        boolean isAuthenticated = false;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
                isAuthenticated = true;
            }
        }
        if (!isAuthenticated) {
            throw new CustomBadRequestException(CustomError.builder().code("400").message("wrong password").build());

        }
        return buidRespone(userOptional.get());
    }

    @Override
    public Map<String, UserDTORespone> registerUser(Map<String, UserDTOCreate> userDTOCreate) {
        UserDTOCreate userCreated = userDTOCreate.get("user");
        User user = UserMapper.toUser(userCreated);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return buidRespone(user);
    }

    public Map<String, UserDTORespone> buidRespone(User user) {
        Map<String, UserDTORespone> wrapper = new HashMap<String, UserDTORespone>();
        UserDTORespone value = UserMapper.toUserDTORespone(user);
        value.setToken(jwtTokenUtil.generateToken(user, 24 * 60 * 60));
        wrapper.put("user", value);
        return wrapper;
    }

    @Override
    public Map<String, UserDTORespone> getCurrentUser() throws CustomNotFoundException {
        User user = getUserLoggedIn();
        if (user != null) {
            return buidRespone(user);
        }
        throw new CustomNotFoundException(CustomError.builder().code("404").message("not found").build());

    }

    @Override
    public Map<String, ProfileDTORespone> getProfile(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new CustomNotFoundException(CustomError.builder().code("404").message("not found").build());
        } else {
            System.out.println("Information");
        }
        User user = userOptional.get();
        List<User> followers = user.getFollowers();
        boolean isFollowing = false;
        for (User u : followers) {
            if (u.getId() == (userLoggedIn.getId())) {
                isFollowing = true;
                break;
            }
        }
        return buidProfileRespone(user, isFollowing);

    }

    public User getUserLoggedIn() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle instanceof UserDetails) {
            String email = ((UserDetails) principle).getUsername();
            User user = userRepository.findByEmail(email).get();
            return user;
        }
        return null;
    }

    private Map<String, ProfileDTORespone> buidProfileRespone(User user, boolean isFollowing) {
        Map<String, ProfileDTORespone> wrapped = new HashMap<>();
        ProfileDTORespone profile = ProfileDTORespone.builder().username(user.getUsername())
                .bio(user.getBio()).image(user.getImage()).following(isFollowing)
                .build();
        wrapped.put("profile", profile);
        return wrapped;
    }

    @Override
    public Map<String, ProfileDTORespone> getFollowUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new CustomNotFoundException(CustomError.builder().code("404").message("not found").build());
        }
        User user = userOptional.get();
        List<User> followers = user.getFollowers();
        boolean isFollowing = false;
        for (User u : followers) {
            if (u.getId() == (userLoggedIn.getId())) {
                isFollowing = true;
                break;
            }
        }
        if (!isFollowing) {
            isFollowing = true;
            user.getFollowers().add(userLoggedIn);
            user = userRepository.save(user);
            isFollowing = true;
        }
        return buidProfileRespone(userOptional.get(), isFollowing);
    }

    @Override
    public Map<String, ProfileDTORespone> unFollowUser(String username) throws CustomNotFoundException {
        // TODO Auto-generated method stub
        User userLoggedIn = getUserLoggedIn();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new CustomNotFoundException(CustomError.builder().code("404").message("not found").build());
        }
        User user = userOptional.get();
        List<User> followers = user.getFollowers();
        boolean isFollowing = false;
        for (User u : followers) {
            if (u.getId() == (userLoggedIn.getId())) {
                isFollowing = true;
                break;
            }
        }
        if (isFollowing) {
            isFollowing = false;
            user.getFollowers().remove(userLoggedIn);
            user = userRepository.save(user);
            isFollowing = false;
        }
        return buidProfileRespone(userOptional.get(), isFollowing);
    }
}
