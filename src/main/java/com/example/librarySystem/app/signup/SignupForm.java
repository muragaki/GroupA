package com.example.librarySystem.app.signup;

import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupForm {
	
	@Size(min = 4 ,max = 4)
	private String userId;
	@Size(min = 4 , max = 6)
	private String password;
	@Size(min = 1 , max = 10)
	private String firstName;
	@Size(min = 1 , max = 10)
	private String lastName;
	

}
