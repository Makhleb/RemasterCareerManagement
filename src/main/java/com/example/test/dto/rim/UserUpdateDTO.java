package com.example.test.dto.rim;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateDTO {
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userBirth;
    private String userGender;
} 