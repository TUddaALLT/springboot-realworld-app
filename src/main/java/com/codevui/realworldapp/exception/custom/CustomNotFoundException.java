package com.codevui.realworldapp.exception.custom;

import com.codevui.realworldapp.model.CustomError;

public class CustomNotFoundException extends BaseCustomException {

    public CustomNotFoundException(CustomError errors) {
        super(errors);
        // TODO Auto-generated constructor stub
    }

}
