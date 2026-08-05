package com.gl.user_service.dto;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    private String profileImage;

    private String bio;

    private String address;

}
