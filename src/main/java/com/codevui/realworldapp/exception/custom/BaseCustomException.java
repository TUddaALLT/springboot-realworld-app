package com.codevui.realworldapp.exception.custom;

import java.util.HashMap;
import java.util.Map;

import com.codevui.realworldapp.model.CustomError;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCustomException extends Exception {
    Map<String, CustomError> errors = new HashMap<>();

    public BaseCustomException(CustomError errors) {
        this.errors = new HashMap<>();
        this.errors.put("errors", errors);
    }

}
