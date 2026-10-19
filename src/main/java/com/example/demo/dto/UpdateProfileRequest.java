package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter 
@Getter 
public class UpdateProfileRequest {

    public UpdateProfileRequest() {
    }

    @NotBlank
    @Size(min = 2, max = 50, message = "name has not a good length")
    private String name;

    @NotBlank
    @Size(min = 2, max = 50, message = "name has not a good length")
    private String surname;

}
