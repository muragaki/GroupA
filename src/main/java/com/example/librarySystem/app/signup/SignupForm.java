package com.example.librarySystem.app.signup;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupForm {
	
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 4 ,max = 4)
	private String userId;
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 4 , max = 6)
	private String password;
	@Size(min = 1 , max = 10)
	private String firstName;
	@Size(min = 1 , max = 10)
	private String lastName;
	

}
