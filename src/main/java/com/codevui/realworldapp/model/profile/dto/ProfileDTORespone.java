package com.codevui.realworldapp.model.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTORespone {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}
