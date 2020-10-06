package com.cloudstorage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileObject {

    private String email;
    private String oldPassword;
    private String newPassword;
}
