package com.cloudstorage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserProfile {

    private String email;
    private String username;
    private long diskLimitation;
    private long takenSpace;



    public UserProfile(String email, String username, long diskLimitation, long takenSpace) {
        this.email = email;
        this.username = username;
        this.diskLimitation = diskLimitation;
        this.takenSpace = takenSpace;
    }




}
