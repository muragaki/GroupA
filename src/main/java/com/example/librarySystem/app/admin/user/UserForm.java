package com.example.librarySystem.app.admin.user;

import java.util.List;

import com.example.librarySystem.domain.model.RoleName;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
	
	@Pattern(regexp="[a-zA-Z0-9]*")
	@Size(min = 8 ,max = 15)
	private String username;
	@NotBlank
	@Size(min=1, max=10)
	private String firstname;
	@NotBlank
	@Size(min=1, max=10)
	private String lastname;
	private RoleName rolename;
	private List<RoleName> roleNameList;
	
}
