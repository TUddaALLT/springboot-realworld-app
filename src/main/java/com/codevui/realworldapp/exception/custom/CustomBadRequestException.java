package com.codevui.realworldapp.exception.custom;

import com.codevui.realworldapp.model.CustomError;

public class CustomBadRequestException extends BaseCustomException {

    public CustomBadRequestException(CustomError errors) {
        super(errors);
        // TODO Auto-generated constructor stub
    }

}
