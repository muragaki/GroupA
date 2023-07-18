package com.example.librarySystem.app.signup;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupForm {
	
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 8 ,max = 15)
	private String userId;
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 8 , max = 17)
	private String password;
	@Size(min = 1 , max = 10)
	private String firstName;
	@Size(min = 1 , max = 10)
	private String lastName;
	
}
