package com.example.librarySystem.app.admin.user;

import java.util.List;

import com.example.librarySystem.domain.model.RoleName;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
	@Size(min=4, max=4)
	private String username;
	@Size(min=1, max=8)
	private String firstname;
	@Size(min=1, max=8)
	private String lastname;
	private RoleName rolename;
	private List<RoleName> roleNameList;
	
}
